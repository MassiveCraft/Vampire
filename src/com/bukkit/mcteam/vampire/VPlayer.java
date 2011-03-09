package com.bukkit.mcteam.vampire;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.server.Packet8UpdateHealth;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.bukkit.mcteam.gson.reflect.TypeToken;
import com.bukkit.mcteam.util.DiscUtil;
import com.bukkit.mcteam.util.EntityUtil;
import com.bukkit.mcteam.util.GeometryUtil;

/**
 * The VPlayer is a "skin" for a normal player.
 * Through this skin we can reach the player plus extra plugin specific data and functionality.
 */
public class VPlayer {
	public static transient Map<String, VPlayer> VPlayers = new HashMap<String, VPlayer>();
	public static transient File file = new File(Vampire.instance.getDataFolder(), "players.json");
	
	private transient String playername;
	private boolean isVampire = false;
	private double blood = 100;
	private double infection = 0; // 0 means no infection. If infection reaches 100 the player will turn to vampire.
	private long timeAsVampire = 0; // The total amount of milliseconds this player has been vampire.
	private long truceBreakTimeLeft = 0; // How many milliseconds more will the monsters be hostile?
	private transient double healthAccumulator = 0;
	public transient long regenDelayLeftMilliseconds = 0;

	public VPlayer(Player player) {
		this.playername = player.getName();
	}
	
	public VPlayer(String playername) {
		this.playername = playername;
	}
	
	// GSON need this noarg constructor.
	public VPlayer() {
	}
	
	public Player getPlayer() {
		return Vampire.instance.getServer().getPlayer(playername);
	}
	
	public String getPlayerName() {
		return this.playername;
	}
	
	// -------------------------------------------- //
	// Online / Offline State Checking
	// -------------------------------------------- //
	
	public boolean isOnline() {
		return Vampire.instance.getServer().getPlayer(playername) != null;
	}
	
	public boolean isOffline() {
		return ! isOnline();
	}
	
	// -------------------------------------------- //
	// The Each Second Tick
	// -------------------------------------------- //
	public void advanceTime(long milliseconds) {
		if (this.isVampire()) {
			this.timeAsVampire += milliseconds;
			this.combustAdvanceTime(milliseconds);
			this.thirstAdvanceTime(milliseconds);
			this.regenAdvanceTime(milliseconds);
			this.truceBreakAdvanceTime(milliseconds);
		} else if (this.isInfected()) {
			this.infectionAdvanceTime(milliseconds);
		}
	}
	
	// -------------------------------------------- //
	// Vampire
	// -------------------------------------------- //
	
	public boolean isVampire() {
		return this.isVampire;
	}
	
	public void turn() {
		this.isVampire = true;
		this.infectionSet(0);
		this.sendMessage(Conf.turnMessages);
		Vampire.log(this.playername + " turned into a vampire.");
		VPlayer.save();
	}
	
	public void cure() {
		this.isVampire = false;
		this.infectionSet(0);
		this.sendMessage(Conf.cureMessages);
		Vampire.log(this.playername + " was cured and is no longer a vampire.");
		VPlayer.save();
	}
	
	public boolean isExvampire() {
		return ( ! this.isVampire() && this.timeAsVampire > 0);
	}
	
	// -------------------------------------------- //
	// Blood :P
	// -------------------------------------------- //
	
	public double bloodGet() {
		return this.blood;
	}
	public void bloodSet(double amount) {
		this.blood = this.limitDouble(amount);
	}
	public void bloodAlter(double delta) {
		this.bloodSet(this.bloodGet() + delta);
	}
	
	public void bloodAlter(double delta, String reason) {
		bloodAlter(delta);
		this.sendMessage(String.format(Conf.messageBloodMeterWithDiffAndReason, this.bloodGet(), delta, reason));
	}
	
	public void bloodDrink(double amount, String source) {
		if (this.bloodGet() == 100D) {
			return;
		}
		this.bloodAlter(amount, String.format(Conf.messageBloodDrinkReason, source));
	}
	
	public void bloodSendMeterMessage() {
		this.sendMessage(String.format(Conf.messageBloodMeter, this.bloodGet()));
	}
	
	// -------------------------------------------- //
	// Monster Truce Feature (Passive)
	// -------------------------------------------- //
	public boolean truceIsBroken() {
		return this.truceBreakTimeLeft != 0;
	}
	
	public void truceBreak() {
		if ( ! this.truceIsBroken()) {
			this.sendMessage(Conf.messageTruceBroken);
		}
		this.truceBreakTimeLeftSet(Conf.truceBreakTime);
	}
	
	public void truceRestore() {
		this.sendMessage(Conf.messageTruceRestored);
		this.truceBreakTimeLeftSet(0);
		
		Player me = this.getPlayer();
		
		// Untarget the player.
		for (LivingEntity entity : this.getPlayer().getWorld().getLivingEntities()) {
			if ( ! (entity instanceof Creature)) {
				continue;
			}
			
			if ( ! Conf.creatureTypeTruceMonsters.contains(EntityUtil.creatureTypeFromEntity(entity))) {
				continue;
			}
			
			Creature creature = (Creature)entity;
			LivingEntity target = creature.getTarget();
			if ( ! (target != null && creature.getTarget().equals(me))) {
				continue;
			}
			
			creature.setTarget(null);
		}
	}
	
	public void truceBreakAdvanceTime(long milliseconds) {
		if ( ! this.truceIsBroken()) {
			return;
		}
		
		this.truceBreakTimeLeftAlter(-milliseconds);
		
		if ( ! this.truceIsBroken()) {
			this.truceRestore();
		}
	}
	
	public long truceBreakTimeLeftGet() {
		return this.truceBreakTimeLeft;
	}
	
	private void truceBreakTimeLeftSet(long milliseconds) {
		if (milliseconds < 0) {
			this.truceBreakTimeLeft = 0;
		} else {
			this.truceBreakTimeLeft = milliseconds;
		}
	}
	
	private void truceBreakTimeLeftAlter(long delta) {
		this.truceBreakTimeLeftSet(this.truceBreakTimeLeftGet() + delta);
	}
	
	// -------------------------------------------- //
	// Dash Ability
	// -------------------------------------------- //
	
	// TODO Create array of blocks you can stand in.
	public void dash() {
		Player player = this.getPlayer();
		List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, Conf.dashMaxLength);
		
		if (lastTwoTargetBlocks.get(1).getType() == Material.AIR) {
			this.sendMessage(Conf.dashMessageToLong);
			return;
		}
		
		Block targetBlock = lastTwoTargetBlocks.get(0);
		
		if (targetBlock.getFace(BlockFace.UP).getType() != Material.AIR) {
			this.sendMessage(Conf.dashMessageTargetToSmall);
			return;
		}
		
		Location playerLocation = player.getLocation();
		Location targetLocation = new Location(targetBlock.getWorld(), targetBlock.getX(), targetBlock.getY(), targetBlock.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
		double x = playerLocation.getX() - targetLocation.getX();
		double y = playerLocation.getY() - targetLocation.getY();
		double z = playerLocation.getZ() - targetLocation.getZ();
		double length = Math.sqrt(x*x + y*y + z*z);
		double bloodRequired = Conf.dashBloodPerBlock * length;
		
		if (this.bloodGet() < bloodRequired) {
			this.sendMessage(Conf.dashMessageNotEnoughBlood);
			return;
		}
		
		player.teleportTo(targetLocation);
		this.bloodAlter(-bloodRequired);
		this.sendMessage(String.format(Conf.messageBloodMeterWithDiffAndReason, this.bloodGet(), -bloodRequired, "Dash!"));
	}
	
	// -------------------------------------------- //
	// Thirst Feature (Passive)
	// -------------------------------------------- //
	
	public void thirstAdvanceTime(long milliseconds) {
		// There is a small blood loss over time.
		this.bloodAlter(Conf.bloodDecreasePerMinute * (milliseconds / 1000D / 60D));
		
		// If thirsty we loose health.
		boolean strong = false;
		if (this.bloodGet() < Conf.thirstStrongUnderBlood) {
			strong = true;
			this.healthAccumulator -= Conf.thirstStrongDamagePerSecond * milliseconds / 1000D;
		} else if (this.bloodGet() < Conf.thirstUnderBlood) {
			this.healthAccumulator -= Conf.thirstDamagePerSecond * milliseconds / 1000D;
		} else {
			return;
		}
		
		//String msg = "AccBefore: "+this.healthAccumulator+" ";
		Integer delta = this.applyHealthAccumulator();
		//msg += "delta: "+delta+" AccAfter: "+this.healthAccumulator;
		//Vampire.log(msg);
		if (delta == null) {
			this.sendMessage(Conf.thirstDeathMessage);
		} else if (delta < 0) {
			// We took damage
			if (strong) {
				this.sendMessage(Conf.thirstStrongMessages.get(Vampire.random.nextInt(Conf.thirstStrongMessages.size())));
			} else {
				this.sendMessage(Conf.thirstMessages.get(Vampire.random.nextInt(Conf.thirstMessages.size())));
			}
		}
	}
	
	// -------------------------------------------- //
	// Regenerate Feature (Passive)
	// -------------------------------------------- //
	public void regenAdvanceTime(long milliseconds) {
		Player player = this.getPlayer();
		int currentHealth = player.getHealth();
		
		// Only regenerate if hurt.
		if (currentHealth == 20) {
			return;
		}
		
		// Can't regenerate if thisty
		// Can't regenerate into a state of thirstiness (thus the equals)
		if (this.bloodGet() <= Conf.thirstUnderBlood) {
			return;
		}
		
		// Check the delay
		if (this.regenDelayLeftMilliseconds > 0) {
			this.regenDelayLeftMilliseconds -= milliseconds;
			
			if (this.regenDelayLeftMilliseconds < 0) {
				this.regenDelayLeftMilliseconds = 0;
			}
			
			if (this.regenDelayLeftMilliseconds > 0) {
				return;
			}
			
			this.sendMessage(Conf.regenStartMessage);
		}
		
		// Calculate blood and health deltas
		double deltaSeconds = milliseconds / 1000D;
		double deltaHeal = deltaSeconds * Conf.regenHealthPerSecond;
		double deltaBlood = - deltaHeal * Conf.regenBloodPerHealth;
		
		// But we can't regenerate into a state of thirstiness
		if (this.bloodGet() + deltaBlood < Conf.thirstUnderBlood) {
			deltaBlood = Conf.thirstUnderBlood - this.bloodGet();
			deltaHeal = -deltaBlood / Conf.regenBloodPerHealth;
		}
		
		this.healthAccumulator += deltaHeal;
		this.bloodAlter(deltaBlood);
		
		// Use as much of the accumulator as possible
		this.applyHealthAccumulator();
	}
	
	// -------------------------------------------- //
	// Common for Thirst and Regen: The Health Accumulator applier
	// -------------------------------------------- //
	
	public Integer applyHealthAccumulator() {
		if (this.healthAccumulator > 0 && this.healthAccumulator < 1) {
			return 0;
		}
		
		Integer deltaHealth = (int)Math.floor(this.healthAccumulator);
		this.healthAccumulator = this.healthAccumulator - deltaHealth;
		
		Player player = this.getPlayer();
		int targetHealth = player.getHealth() + deltaHealth;
		this.setHealth(targetHealth);
		
		if (targetHealth <=0) {
			return null; // Death signal.
		}
		
		if (deltaHealth < 0 ) {
			this.damageVisualize();
		}
		
		return deltaHealth;
	}
	
	// -------------------------------------------- //
	// Combustion
	// -------------------------------------------- //
	public boolean combustAdvanceTime(long milliseconds) {
		if ( ! this.standsInSunlight()) {
			return false;
		}
		
		// We assume the next tick will be in milliseconds milliseconds.
		
		int ticksTillNext = (int) (milliseconds / 1000D * 20D); // 20 minecraft ticks is a second.
		ticksTillNext += 5; // just to be on the safe side.
		
		Player player = this.getPlayer();
		if (player.getFireTicks() <= 0) {
			this.sendMessage(Conf.combustMessage);
		}
		
		player.setFireTicks(ticksTillNext + Conf.combustFireExtinguishTicks);
		
		return true;
	}
	
	public boolean standsInSunlight() {
		Player player = this.getPlayer();
		
		if (player.getWorld().getEnvironment() == Environment.NETHER) {
			return false;
		}
		
		if (this.worldTimeIsNight()) {
			return false;
		}
		
		if (this.isUnderRoof()) {
			return false;
		}
		
		// No need to set on fire if the water will put the fire out at once.
		Material material = player.getLocation().getBlock().getType();
		if (material == Material.STATIONARY_WATER || material == Material.WATER) {
			return false;
		}
		
		return true;
	}
	
	public boolean isUnderRoof() {
		/*
		We start checking opacity 2 blocks up.
		As Max Y is 127 there CAN be a roof over the player if he is standing in block 125:
		127 Solid Block
		126 
		125 Player
		However if he is standing in 126 there is no chance.
		*/
		Block blockCurrent = this.getPlayer().getLocation().getBlock();
		if (blockCurrent.getY() >= 126) {
			return false;
		}
		
		blockCurrent = blockCurrent.getFace(BlockFace.UP, 1); // I said 2 up yes. Another 1 is added in the beginning of the loop.
				
		double opacityAccumulator = 0;
		Double opacity;
		while (blockCurrent.getY() + 1 <= 127) {
			blockCurrent = blockCurrent.getRelative(BlockFace.UP);
			
			opacity = Conf.materialOpacity.get(blockCurrent.getType());
			if (opacity == null) {
				return true; // Blocks not in that map have opacity 1;
			}
			
			opacityAccumulator += opacity;
			if (opacityAccumulator >= 1) {
				return true;
			}
		}
		return false;
	}
	
	public boolean worldTimeIsNight() {
		long time = this.getPlayer().getWorld().getTime() % 24000;
		
		if (time < Conf.combustFromTime || time > Conf.combustToTime) {
			return true;
		}
		
		return false; 
	}
	
	// -------------------------------------------- //
	// Infection 
	// -------------------------------------------- //
	public boolean isInfected() {
		return this.infection > 0D && this.isVampire == false;
	}
	
	public double infectionGet() {
		return this.infection;
	}
	public void infectionSet(double infection) {
		double previousinfection = this.infectionGet();
		this.infection = this.limitDouble(infection, 0D, 100D);
		
		// Wan't to save if someone was uninfected:
		// We want to save if someone was infected.
		if ((previousinfection != 0 && this.infection == 0) || (previousinfection == 0 && this.infection != 0))
		{
			VPlayer.save();
		}
	}
	public void infectionAlter(double infection) {
		this.infectionSet(this.infectionGet() + infection);
	}
	
	public void infectionHeal(double amount) {
		if (this.isVampire()) {
			return;
		}
		
		double current = this.infectionGet();
		
		if (current == 0D ) {
			// The player is already completely healthy
			return;
		}
		
		current -= amount; 
		
		if (current <= 0D) {
			this.infectionSet(0D);
			this.sendMessage(Conf.infectionMessageCured);
			return;
		}
		
		this.infectionSet(current);
		this.sendMessage(Conf.infectionMessageHeal);
	}
	
	public void infectionAdvanceTime(long milliseconds) {
		this.infectionAdvance(Conf.infectionProgressPerSecond * milliseconds / 1000D );
	}
	
	public void infectionAdvance(double amount) {
		if (this.isVampire()) {
			return;
		}
		
		int oldMessageIndex = this.infectionGetMessageIndex();
		this.infectionAlter(amount);
		int newMessageIndex = this.infectionGetMessageIndex();
		
		if (this.infectionGet() == 100) {
			this.turn();
			return;
		}
		
		if (oldMessageIndex != newMessageIndex) {
			this.damageTake(1);
			this.damageVisualize();
			this.sendMessage(Conf.infectionMessagesProgress.get(newMessageIndex));
			this.sendMessage(Conf.infectionBreadHintMessages.get(Vampire.random.nextInt(Conf.infectionBreadHintMessages.size())));
		}
	}
	
	public int infectionGetMessageIndex() {
		return (int)((Conf.infectionMessagesProgress.size()+1) * this.infectionGet() / 100D) - 1;
	}
	
	public void infectionRisk() {
		//Vampire.log(this.playername + " risked infection.");
		if (Vampire.random.nextDouble() <= Conf.infectionCloseCombatRisk) {
			Vampire.log(this.playername + " contracted vampirism infection.");
			this.infectionAdvance(Conf.infectionCloseCombatAmount);
		}
	}
	
	// -------------------------------------------- //
	// Altar Usage
	// -------------------------------------------- //
	
	public void useAltarInfect(Block centerBlock) {
		// The altar must be big enough
		int count = GeometryUtil.countNearby(centerBlock, Conf.altarInfectMaterialSurround, Conf.altarInfectMaterialSurroundRadious);
		if (count == 0) {
			return;
		} else if (count < Conf.altarInfectMaterialSurroundCount) {
			this.sendMessage(Conf.altarInfectToSmall);
			return;
		}
		
		// Always examine first
		this.sendMessage(Conf.altarInfectExamineMsg);
		
		// Is Vampire
		if (this.isVampire()) {
			this.sendMessage(Conf.altarInfectExamineMsgNoUse);
			return;
		}
		
		// Is Infected
		if (this.isInfected()) {
			this.sendMessage(Conf.altarInfectExamineWhileInfected);
			return;
		}
		
		// Is healthy and thus can be infected...
		if (Conf.altarInfectRecipe.inventoryContainsEnough(this.getPlayer().getInventory())) {
			this.sendMessage(Conf.altarUseIngredientsSuccess);
			this.sendMessage(Conf.altarInfectRecipe.getRecipeLine());
			this.sendMessage(Conf.altarInfectUse);
			Conf.altarInfectRecipe.removeFromInventory(this.getPlayer().getInventory());
			this.infectionAlter(3D);
		} else {
			this.sendMessage(Conf.altarUseIngredientsFail);
			this.sendMessage(Conf.altarInfectRecipe.getRecipeLine());
		}
	}
	
	public void useAltarCure(Block centerBlock) {
		// The altar must be big enough;
		int count = GeometryUtil.countNearby(centerBlock, Conf.altarCureMaterialSurround, Conf.altarCureMaterialSurroundRadious);
		if (count == 0) {
			return;
		} else if (count < Conf.altarCureMaterialSurroundCount) {
			this.sendMessage(Conf.altarCureToSmall);
			return;
		}
		
		// Always examine first
		this.sendMessage(Conf.altarCureExamineMsg);
		
		// If healthy
		if ( ! this.isInfected() && ! this.isVampire()) {
			this.sendMessage(Conf.altarCureExamineMsgNoUse);
			return;
		}
		
		// If Infected
		if (this.isInfected()) {
			this.sendMessage(Conf.altarCureExamineWhileInfected);
			this.infectionSet(0);
			this.sendMessage(Conf.infectionMessageCured);
			return;
		}
		
		// Is vampire and thus can be cured...
		if (Conf.altarCureRecipe.inventoryContainsEnough(this.getPlayer().getInventory())) {
			this.sendMessage(Conf.altarUseIngredientsSuccess);
			this.sendMessage(Conf.altarCureRecipe.getRecipeLine());
			this.sendMessage(Conf.altarCureUse);
			Conf.altarCureRecipe.removeFromInventory(this.getPlayer().getInventory());
			this.cure();
		} else {
			this.sendMessage(Conf.altarUseIngredientsFail);
			this.sendMessage(Conf.altarCureRecipe.getRecipeLine());
		}
	}
	
	// -------------------------------------------- //
	// Damage methods (Yes I use CraftBukkit here :P)
	// -------------------------------------------- //
	
	public void damageTake(int damage) {
		Player player = this.getPlayer();
		this.setHealth(player.getHealth() - damage);
	}
	
	public void setHealth(int health) {
		if (health < 0 ) {
			health = 0;
		} else if (health > 20) {
			health = 20;
		}
		this.getPlayer().setHealth(health);
	}
	
	public void damageVisualize() {
		Player player = this.getPlayer();
		CraftPlayer cPlayer = (CraftPlayer)this.getPlayer();
		int currentHealth = player.getHealth();
		cPlayer.getHandle().a.b(new Packet8UpdateHealth(20));
		cPlayer.getHandle().a.b(new Packet8UpdateHealth(19));
		cPlayer.getHandle().a.b(new Packet8UpdateHealth(currentHealth));
	}
	
	// -------------------------------------------- //
	// Commonly used limiter of double
	// -------------------------------------------- //
	public double limitDouble(double d, double min, double max) {
		if (d < min) {
			return min;
		}
		
		if (d > max) {
			return max;
		}
		
		return d;
	}
	
	public double limitDouble(double d) {
		return this.limitDouble(d, 0, 100);
	}
	
	// -------------------------------------------- //
	// VPlayer search queries
	// -------------------------------------------- //
	
	public static Set<VPlayer> findAllOnlineInfected() {
		Set<VPlayer> vplayers = new HashSet<VPlayer>();
		for (VPlayer vplayer : VPlayer.VPlayers.values()) {
			if (vplayer.isOnline() && vplayer.isInfected()) {
				vplayers.add(vplayer);
			}
		}
		return vplayers;
	}
	
	public static Set<VPlayer> findAllOnlineVampires() {
		Set<VPlayer> vplayers = new HashSet<VPlayer>();
		for (VPlayer vplayer : VPlayer.VPlayers.values()) {
			if (vplayer.isOnline() && vplayer.isVampire()) {
				vplayers.add(vplayer);
			}
		}
		return vplayers;
	}
	
	public static Set<VPlayer> findAllOnline() {
		Set<VPlayer> vplayers = new HashSet<VPlayer>();
		for (Player player : Vampire.instance.getServer().getOnlinePlayers()) {
			vplayers.add(VPlayer.get(player));
		}
		return vplayers;
	}
	
	public static Collection<VPlayer> findAll() {
		// Make sure all players get VPlayer entries.
		findAllOnline();
		return VPlayer.VPlayers.values();
	}
	
	// -------------------------------------------- //
	// Get VPlayer
	// You can only get a VPlayer "skin" for online players.
	// The same VPlayer object is always return for the same player.
	// This means you can use the == operator. No .equals method necessary.
	// -------------------------------------------- //
	public static VPlayer get(String playername) {
		if (VPlayers.containsKey(playername)) {
			return VPlayers.get(playername);
		}
		
		VPlayer vplayer = new VPlayer(playername);
		VPlayers.put(playername, vplayer);
		return vplayer;
	}
	
	// You should use this one to be sure you do not spell the player name wrong.
	public static VPlayer get(Player player) {
		return get(player.getName());
	}
	
	// -------------------------------------------- //
	// Messages
	// -------------------------------------------- //
	public void sendMessage(String message) {
		this.getPlayer().sendMessage(Conf.colorSystem + message);
	}
	
	public void sendMessage(List<String> messages) {
		for(String message : messages) {
			this.sendMessage(message);
		}
	}
	
	// -------------------------------------------- //
	// Persistance
	// -------------------------------------------- //
	
	public boolean shouldBeSaved() {
		return this.isVampire() || this.isInfected() || this.isExvampire();
	}
	
	public static boolean save() {
		Vampire.log("Saving players to disk");
		
		// We only wan't to save the vplayers with non default values
		Map<String, VPlayer> vplayersToSave = new HashMap<String, VPlayer>();
		for (Entry<String, VPlayer> entry : VPlayers.entrySet()) {
			if (entry.getValue().shouldBeSaved()) {
				vplayersToSave.put(entry.getKey(), entry.getValue());
			}
		}
		
		try {
			DiscUtil.write(file, Vampire.gson.toJson(vplayersToSave));
		} catch (IOException e) {
			Vampire.log("Failed to save the players to disk.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean load() {
		if ( ! file.exists()) {
			Vampire.log("No players to load from disk. Creating new file.");
			save();
			return true;
		}
		
		try {
			Type type = new TypeToken<Map<String, VPlayer>>(){}.getType();
			VPlayers = Vampire.gson.fromJson(DiscUtil.read(file), type);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		fillPlayernames();
			
		return true;
	}
	
	public static void fillPlayernames() {
		for(Entry<String, VPlayer> entry : VPlayers.entrySet()) {
			entry.getValue().playername = entry.getKey();
		}
	}
}
