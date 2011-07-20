package com.massivecraft.vampire;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.massivecraft.vampire.config.*;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.vampire.util.DiscUtil;
import com.massivecraft.vampire.util.EntityUtil;
import com.massivecraft.vampire.util.GeometryUtil;

/**
 * The VPlayer is a "skin" for a normal player.
 * Through this skin we can reach the player plus extra plugin specific data and functionality.
 */
public class VPlayer {
	public static transient Map<String, VPlayer> VPlayers = new HashMap<String, VPlayer>();
	public static transient File file = new File(P.instance.getDataFolder(), "players.json");
	
	private transient String playername;
	private boolean isVampire = false;
	private boolean isTrueBlood = false;
	private double blood = 100;
	private double infection = 0; // 0 means no infection. If infection reaches 100 the player will turn to vampire.
	private long timeAsVampire = 0; // The total amount of milliseconds this player has been vampire.
	private long truceBreakTimeLeft = 0; // How many milliseconds more will the monsters be hostile?
	private transient double healthAccumulator = 0;
	private long lastVampireDeath = 0;
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
		return P.instance.getServer().getPlayer(playername);
	}
	
	public String getPlayerName() {
		return this.playername;
	}
	
	// -------------------------------------------- //
	// Online / Offline State Checking
	// -------------------------------------------- //
	
	public boolean isOnline() {
		return P.instance.getServer().getPlayer(playername) != null;
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
			this.regenAdvanceTime(milliseconds);
			this.thirstAdvanceTime(milliseconds);
			this.truceBreakAdvanceTime(milliseconds);
			this.spreadNecrosisAdvanceTime(milliseconds);
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
	
	public boolean isTrueBlood() {
		return this.isTrueBlood;
	}
	
	public void setIsTrueBlood(boolean val) {
		this.isTrueBlood = val;
	}
	
	public void turn() {
		this.isVampire = true;
		this.infectionSet(0);
		
		if(this.isTrueBlood)
		{
			this.sendMessage(Lang.turnTrueBloodMessages);
			P.log(this.playername + " turned into a TrueBlood vampire.");
		}
		else
		{
			this.sendMessage(Lang.turnMessages);
			P.log(this.playername + " turned into a common vampire.");
		}
		VPlayer.save();
	}
	
	public void cure() {
		this.isVampire = false;
		this.isTrueBlood = false;
		this.infectionSet(0);
		this.sendMessage(Lang.cureMessages);
		//Vampire.log(this.playername + " was cured and is no longer a vampire.");
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
		this.sendMessage(String.format(Lang.messageBloodMeterWithDiffAndReason, this.bloodGet(), delta, reason));
	}
	
	public void bloodDrink(double amount, String source) {
		if (this.bloodGet() == 100D) {
			return;
		}
		this.bloodAlter(amount, String.format(Lang.messageBloodDrinkReason, source));
	}
	
	public void bloodSendMeterMessage() {
		if(this.isTrueBlood) this.sendMessage(Lang.messageTrueBloodVampire);
		else  this.sendMessage(Lang.messageBasicVampire);
		this.sendMessage(String.format(Lang.messageBloodMeter, this.bloodGet()));
	}
	
	// -------------------------------------------- //
	// Monster Truce Feature (Passive)
	// -------------------------------------------- //
	public boolean truceIsBroken() {
		return this.truceBreakTimeLeft != 0;
	}
	
	public void truceBreak() {
		if ( ! this.truceIsBroken()) {
			this.sendMessage(Lang.messageTruceBroken);
		}
		this.truceBreakTimeLeftSet(Conf.truceBreakTime);
	}
	
	public void truceRestore() {
		this.sendMessage(Lang.messageTruceRestored);
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
	// Jump ability
	// -------------------------------------------- //
	public void jump(double deltaSpeed, boolean upOnly) {
		Player player = this.getPlayer();
		
		if (this.bloodGet() - Conf.jumpBloodCost <= Conf.thirstUnderBlood) {
			this.sendMessage(Lang.jumpMessageNotEnoughBlood);
			return;
		}
		
		this.bloodAlter(-Conf.jumpBloodCost, "Jump!");
		
		Vector vjadd;
		if (upOnly) {
			vjadd = new Vector(0, 1, 0);
		} else {
			vjadd = player.getLocation().getDirection();
			vjadd.normalize();
		}
		vjadd.multiply(deltaSpeed);
		vjadd.setY(vjadd.getY() / 2.5D); // Compensates for the "in air friction" that not applies to y-axis.
		
		player.setVelocity(player.getVelocity().add(vjadd));
	}
	
	// -------------------------------------------- //
	// Necrosis Feature (Passive)
	// When a vampire cross the world, all the world becom death : grass becom dirt, flower become dust etc..
	// -------------------------------------------- //
	
	public void spreadNecrosisAdvanceTime(long milliseconds)
	{
		if(this.isTrueBlood && !TrueBloodConf.allowSpreadingNecrosis) return;
		else if (!CommonConf.allowSpreadingNecrosis) return;
		
		Player player = this.getPlayer();
		ArrayList<Block> blocks;
			
		if(this.isTrueBlood) blocks = GeometryUtil.getCubeBlocksAroundPlayer(player.getLocation().getBlock(), TrueBloodConf.radiusSpreadingNecrosis);
		else blocks = GeometryUtil.getCubeBlocksAroundPlayer(player.getLocation().getBlock(), CommonConf.radiusSpreadingNecrosis);
				
		for(Block b : blocks)
		{
			try
			{
				if(b.getType() == Material.SNOW) b.setType(Material.AIR);
				else if(b.getType() == Material.GRASS) b.setType(Material.DIRT);
				else if(b.getType() == Material.YELLOW_FLOWER) b.setType(Material.AIR);
			}
			catch (Exception e)
			{
				log("[Vampire error] :" + e.getMessage());
				break;
			}				
		}
	}
	
	// -------------------------------------------- //
	// Thirst Feature (Passive)
	// -------------------------------------------- //
	
	public void thirstAdvanceTime(long milliseconds)
	{
		if(this.isTrueBlood && !TrueBloodConf.allowBloodLoss) return;
		else if(!CommonConf.allowBloodLoss) return;
		
		// There is a small blood loss over time.
		this.bloodAlter(-Conf.bloodDecreasePerSecond);
		
		// If thirsty we loose health.
		//boolean strong = false;
		if (this.bloodGet() <= Conf.thirstStrongUnderBlood) 
		{
			//strong = true;
			this.healthAccumulator -= Conf.thirstStrongDamagePerSecond * milliseconds / 1000D;
		}
		else if (this.bloodGet() <= Conf.thirstUnderBlood) 
		{
			this.healthAccumulator -= Conf.thirstDamagePerSecond * milliseconds / 1000D;
		}
		
		// Use as much of the accumulator as possible
		this.applyHealthAccumulator();
		
		return;
		
			/*String msg = "AccBefore: "+this.healthAccumulator+" ";
			Integer delta = this.applyHealthAccumulator();
			msg += "delta: "+delta+" AccAfter: "+this.healthAccumulator;
			Vampire.log(msg);
			if (delta == null) {
				this.sendMessage(Conf.thirstDeathMessage);
			} else if (delta < 0) {
				// We took damage
				if (strong) {
					this.sendMessage(Conf.thirstStrongMessages.get(Vampire.random.nextInt(Conf.thirstStrongMessages.size())));
				} else {
					this.sendMessage(Conf.thirstMessages.get(Vampire.random.nextInt(Conf.thirstMessages.size())));
				}
			}*/
	}
	
	// -------------------------------------------- //
	// Regenerate Feature (Passive)
	// -------------------------------------------- //
	public void regenAdvanceTime(long milliseconds) {
		
		Player player = this.getPlayer();
		int currentHealth = player.getHealth();
		
		// Only regenerate if hurt and we're > blood
		if ( (currentHealth == 20) && (this.bloodGet() > Conf.thirstUnderBlood) ) {
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
			
			this.sendMessage(Lang.regenStartMessage);
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
		
		// If targetHealth < 0 let's set it at 0
		//if ( targetHealth <= 0 )
			//targetHealth = 0;
		
		int playerHealth = player.getHealth();
		//Vampire.log("getHealth: " + player.getHealth() + " deltaHealth: " + deltaHealth + " = targetHealth: " + targetHealth);
		
		// If user's health is already <= 0, we dont need to set it again
		if (playerHealth > 0)
		{
			//Vampire.log("Killing user (by setting targetHealth: " + targetHealth + ")");
			this.setHealth(targetHealth);
		}
		
		if (targetHealth <=0) {
			// We need to make note of the last time this user died from vampire regeneration. Used to help avoid the double-death (aka title bug)
			this.lastVampireDeath = System.currentTimeMillis();
			
			return null; // Death signal.
		}
		
		if (deltaHealth < 0 ) {
			
		}
		
		return deltaHealth;
	}
	
	// -------------------------------------------- //
	// Combustion
	// -------------------------------------------- //
	public boolean combustAdvanceTime(long milliseconds) {
		if (!this.standsInSunlight())	return false;
		
		if(this.isTrueBlood && !TrueBloodConf.burnAtSunlight) return false;
		else if(!CommonConf.burnAtSunlight) return false;
		
		// We assume the next tick will be in milliseconds milliseconds.
		
		int ticksTillNext = (int) (milliseconds / 1000D * 20D); // 20 minecraft ticks is a second.
		ticksTillNext += 5; // just to be on the safe side.
		
		Player player = this.getPlayer();
		if (player.getFireTicks() <= 0) {
			this.sendMessage(Lang.combustMessage);
		}
		
		player.setFireTicks(ticksTillNext + Conf.combustFireExtinguishTicks);
		
		return true;
	}
	
	public boolean standsInSunlight() {
		Player player = this.getPlayer();
		
		// No need to set on fire if the water will put the fire out at once.
		Material material = player.getLocation().getBlock().getType();
		World playerWorld = player.getWorld();
		
		if ((player.getWorld().getEnvironment() == Environment.NETHER) 
				|| this.worldTimeIsNight() || this.isUnderRoof() || material == Material.STATIONARY_WATER
				|| material == Material.WATER || playerWorld.hasStorm() || playerWorld.isThundering())
		{
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
		boolean retVal = false;
		Block blockCurrent = this.getPlayer().getLocation().getBlock();

		if (this.getPlayer().getLocation().getY() >= 126)
		{
			retVal = false;
		}
		else
		{
			blockCurrent = blockCurrent.getRelative(BlockFace.UP, 1); // I said 2 up yes. Another 1 is added in the beginning of the loop.
				
			double opacityAccumulator = 0;
			Double opacity;
		
			while (blockCurrent.getY() + 1 <= 127) 
			{
				blockCurrent = blockCurrent.getRelative(BlockFace.UP);
			
				opacity = Conf.materialOpacity.get(blockCurrent.getType());
				if (opacity == null) {
					retVal = true; // Blocks not in that map have opacity 1;
					break;
				}
			
				opacityAccumulator += opacity;
				if (opacityAccumulator >= 1.0D) {
					retVal = true;
					break;
				}
			}
		}
		return retVal;
	}
	
	public boolean worldTimeIsNight() {
		long time = this.getPlayer().getWorld().getTime() % 24000;
		
		if (time < Conf.combustFromTime || time > Conf.combustToTime) return true;
		
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
			this.sendMessage(Lang.infectionMessageCured);
			return;
		}
		
		this.infectionSet(current);
		this.sendMessage(Lang.infectionMessageHeal);
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
			this.sendMessage(Lang.infectionMessagesProgress.get(newMessageIndex));
			this.sendMessage(Lang.infectionBreadHintMessages.get(P.random.nextInt(Lang.infectionBreadHintMessages.size())));
		}
	}
	
	public int infectionGetMessageIndex() {
		return (int)((Lang.infectionMessagesProgress.size()+1) * this.infectionGet() / 100D) - 1;
	}
	
	public void infectionRisk() {
		//Vampire.log(this.playername + " risked infection.");
		if (P.random.nextDouble() <= Conf.infectionCloseCombatRisk) {
			P.log(this.playername + " contracted vampirism infection.");
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
		}
		
		this.sendMessage(" ");
		
		if (count < Conf.altarInfectMaterialSurroundCount) {
			this.sendMessage(Lang.altarInfectToSmall);
			return;
		}
		
		// Always examine first
		this.sendMessage(Lang.altarInfectExamineMsg);
		
		// Is Vampire
		if (this.isVampire()) {
			this.sendMessage(Lang.altarInfectExamineMsgNoUse);
			return;
		}
		
		// Is Infected
		if (this.isInfected()) {
			this.sendMessage(Lang.altarInfectExamineWhileInfected);
			return;
		}
		
		// Is healthy and thus can be infected...
		if (Conf.altarInfectRecipe.playerHasEnough(this.getPlayer())) {
			this.sendMessage(Lang.altarUseIngredientsSuccess);
			this.sendMessage(Conf.altarInfectRecipe.getRecipeLine());
			this.sendMessage(Lang.altarInfectUse);
			P.log(this.playername + " was infected by an evil altar.");
			Conf.altarInfectRecipe.removeFromPlayer(this.getPlayer());
			this.infectionAlter(3D);
			this.isTrueBlood = true;
		} else {
			this.sendMessage(Lang.altarUseIngredientsFail);
			this.sendMessage(Conf.altarInfectRecipe.getRecipeLine());
		}
	}
	
	public void useAltarCure(Block centerBlock) {
		// The altar must be big enough;
		int count = GeometryUtil.countNearby(centerBlock, Conf.altarCureMaterialSurround, Conf.altarCureMaterialSurroundRadious);
		if (count == 0) {
			return;
		}
		
		this.sendMessage(" ");
		
		if (count < Conf.altarCureMaterialSurroundCount) {
			this.sendMessage(Lang.altarCureToSmall);
			return;
		}
		
		// Always examine first
		this.sendMessage(Lang.altarCureExamineMsg);
		
		// If healthy
		if ( ! this.isInfected() && ! this.isVampire()) {
			this.sendMessage(Lang.altarCureExamineMsgNoUse);
			return;
		}
		
		// If Infected
		if (this.isInfected()) {
			this.sendMessage(Lang.altarCureExamineWhileInfected);
			this.infectionSet(0);
			this.sendMessage(Lang.infectionMessageCured);
			return;
		}
		
		// Is vampire and thus can be cured...
		if(this.isTrueBlood && TrueBloodConf.altarCureRecipe.playerHasEnough(this.getPlayer()))
		{
			this.sendMessage(Lang.altarUseIngredientsSuccess);
			this.sendMessage(TrueBloodConf.altarCureRecipe.getRecipeLine());
			this.sendMessage(Lang.altarCureUse);
			TrueBloodConf.altarCureRecipe.removeFromPlayer(this.getPlayer());
			P.log(this.playername + " was cured from being a TrueBlood vampire by a healing altar.");
			this.cure();
		} 
		else if(CommonConf.altarCureRecipe.playerHasEnough(this.getPlayer()))
		{
			this.sendMessage(Lang.altarUseIngredientsSuccess);
			this.sendMessage(CommonConf.altarCureRecipe.getRecipeLine());
			this.sendMessage(Lang.altarCureUse);
			CommonConf.altarCureRecipe.removeFromPlayer(this.getPlayer());
			P.log(this.playername + " was cured from being a Common vampire by a healing altar.");
			this.cure();
		}
		else
		{
			this.sendMessage(Lang.altarUseIngredientsFail);
			if(this.isTrueBlood) this.sendMessage(TrueBloodConf.altarCureRecipe.getRecipeLine());
			else this.sendMessage(CommonConf.altarCureRecipe.getRecipeLine());
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
		for (Player player : P.instance.getServer().getOnlinePlayers()) {
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
		P.log("Saving players to disk");
		
		// We only wan't to save the vplayers with non default values
		Map<String, VPlayer> vplayersToSave = new HashMap<String, VPlayer>();
		for (Entry<String, VPlayer> entry : VPlayers.entrySet()) {
			if (entry.getValue().shouldBeSaved()) {
				vplayersToSave.put(entry.getKey(), entry.getValue());
			}
		}
		
		try {
			DiscUtil.write(file, P.gson.toJson(vplayersToSave));
		} catch (IOException e) {
			P.log("Failed to save the players to disk.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean load() {
		if ( ! file.exists()) {
			P.log("No players to load from disk. Creating new file.");
			save();
			return true;
		}
		
		try {
			Type type = new TypeToken<Map<String, VPlayer>>(){}.getType();
			VPlayers = P.gson.fromJson(DiscUtil.read(file), type);
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
	
	public static void log(String msg) {
		Logger.getLogger("Minecraft").log(Level.INFO, msg);
	}
}
