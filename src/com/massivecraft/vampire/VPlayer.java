package com.massivecraft.vampire;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.util.Vector;

import com.massivecraft.mcore1.MCore;
import com.massivecraft.mcore1.cmd.MCommand;
import com.massivecraft.mcore1.persist.IClassManager;
import com.massivecraft.mcore1.persist.PlayerEntity;
import com.massivecraft.vampire.config.*;
import com.massivecraft.vampire.util.EntityUtil;
import com.massivecraft.vampire.util.SmokeUtil;

/**
 * The VPlayer is a "skin" for a normal player.
 * Through this skin we can reach the player plus extra plugin specific data and functionality.
 */
public class VPlayer extends PlayerEntity<VPlayer>
{
	@Override
	public IClassManager<VPlayer> getManager()
	{
		return VPlayers.i;
	}
	
	@Override
	protected VPlayer getThis()
	{
		return this;
	}
	
	public static transient P p = P.p;
	
	// Is the player a vampire?
	private boolean vampire = false;
	public boolean isVampire() { return this.vampire; }
	public void setIsVampire(boolean isVampire) { this.vampire = isVampire; this.updateVampPermission(); }
	public boolean isExvampire() { return this.isVampire() == false && this.timeAsVampire > 0; }
		
	// 0 means no infection. If infection reaches 100 the player will turn to vampire.
	private double infection = 0; 
	public double getInfection() { return this.infection; }
	public void setInfection(double infection) { this.infection = limitNumber(infection, 0D, 100D); }	
	public void alterInfection(double infection) { this.setInfection(this.getInfection() + infection); }
	public boolean isInfected() { return this.infection > 0D; }
	
	// Used for the infec and accept commands.
	private transient VPlayer infectionOfferedFrom;
	private transient long infectionOfferedAtTicks;
	
	public boolean isHealthy()
	{
		return ! this.isVampire() && ! this.isInfected();
	}
	
	// Vampires may choose their combat style. Do they intend to infect others in combat or do they not?
	private boolean intendingToInfect = false;
	public boolean isIntendingToInfect() { return intendingToInfect; }
	public void setIntendingToInfect(boolean intendingToInfect) { this.intendingToInfect = intendingToInfect; }

	private String makerId;
	// TODO: Extend the maker and turn reason concept!!
	
	private long timeAsVampire = 0; // The total amount of milliseconds this player has been vampire.
	private long truceBreakTicksLeft = 0; // How many milliseconds more will the monsters be hostile?
	private transient double foodAccumulator = 0;
	private transient double healthAccumulator = 0;
	//public transient long regenDelayLeftMilliseconds = 0;
	
	private transient PermissionAttachment permA;
	
	// GSON need this noarg constructor.
	public VPlayer()
	{
		
	}
	
	public Player getPlayer()
	{
		return Bukkit.getPlayer(this.getId());
	}
	
	// -------------------------------------------- //
	// Online / Offline State Checking
	// -------------------------------------------- //
	
	public boolean isOnline()
	{
		return this.getPlayer() != null;
	}
	
	public boolean isOffline()
	{
		return ! isOnline();
	}
	
	// -------------------------------------------- //
	// The Each Second Tick
	// -------------------------------------------- //
	public void advanceTime(long ticks)
	{
		if (this.isVampire())
		{
			this.timeAsVampire += ticks;
			this.combustAdvanceTime(ticks);
			this.truceBreakAdvanceTime(ticks);
		}
		else if (this.isInfected())
		{
			this.infectionAdvanceTime(ticks);
		}
	}
	
	// -------------------------------------------- //
	// Vampire
	// -------------------------------------------- //
	public void turn()
	{
		if (this.isVampire()) return;
		SmokeUtil.smokeifyPlayer(this.getPlayer(), 20*30);
		this.setInfection(0);
		this.setIsVampire(true);
		this.msg(Lang.youWasTurned);
	}
	
	public void cureVampirism()
	{
		this.setInfection(0);
		this.setIsVampire(false);
		this.msg(Lang.youWasCured);
	}
	
	// -------------------------------------------- //
	// Offer and Accept infection
	// -------------------------------------------- //
	public void acceptInfection()
	{
		VPlayer vyou = this.infectionOfferedFrom;
		if (vyou == null || System.currentTimeMillis() - this.infectionOfferedAtTicks > Conf.cmdInfectMillisRecentTolerance)
		{
			this.msg(Lang.infectNoRecentOffer);
			return;
		}
		
		Player me = this.getPlayer();
		Player you = vyou.getPlayer();
		
		// Check the player-distance
		Location l1 = me.getLocation();
		Location l2 = you.getLocation();
		
		if ( ! l1.getWorld().equals(l2.getWorld()) || l1.distance(l2) > Conf.cmdInfectMaxDistance)
		{
			me.sendMessage(p.txt.parse(Lang.infectYouMustStandCloseToY, you.getDisplayName()));
			return;
		}
		
		this.msg(Lang.infectYouDrinkSomeOfXBlood, you.getDisplayName());
		vyou.msg(Lang.infectXDrinkSomeOfYourBlood, me.getDisplayName());
		
		if (this.isVampire()) return;
		
		this.alterInfection(5.0);
		you.damage(2);
	}
	
	public void offerInfectionTo(VPlayer vyou)
	{
		Player me = this.getPlayer();
		Player you = vyou.getPlayer();
		
		// Check the player-distance
		Location l1 = me.getLocation();
		Location l2 = you.getLocation();
		
		if ( ! l1.getWorld().equals(l2.getWorld()) || l1.distance(l2) > Conf.cmdInfectMaxDistance)
		{
			this.msg(Lang.infectYouMustStandCloseToY, you.getDisplayName());
			return;
		}
		
		vyou.infectionOfferedFrom = this;
		vyou.infectionOfferedAtTicks = System.currentTimeMillis();
		vyou.msg(Lang.infectXOffersToInfectYou, me.getDisplayName());
		
		// TODO: WHAT's THIS FOR!?!?!?!?!?!?!
		List<MCommand> cmdc = new ArrayList<MCommand>();
		cmdc.add(p.cmdBase);
		vyou.msg(Lang.infectTypeXToAccept, p.cmdBase.cmdAccept.getUseageTemplate(cmdc, false)); //TODO: Link to the accept command!
		this.msg(Lang.infectYouOfferToInfectX, you.getDisplayName());
	}
	
	// -------------------------------------------- //
	// Food. The food is handled as a double from 0 to 20
	// This system uses an accumulator to wrap the int in a double
	// -------------------------------------------- //
	
	public void foodAdd(double food)
	{
		this.foodAccumulator += food;
		this.foodApplyAccumulator();
	}
	
	public void foodApplyAccumulator()
	{
		int deltaFood = (int)Math.floor(this.foodAccumulator);
		this.foodAccumulator = this.foodAccumulator - deltaFood;
		
		Player player = this.getPlayer();
		
		int targetFood = limitNumber(player.getFoodLevel() + deltaFood, 0, 20);
		player.setFoodLevel(targetFood);
	}
	
	public void healthAdd(double health)
	{
		this.healthAccumulator += health;
		this.healthApplyAccumulator();
	}
	
	public void healthApplyAccumulator()
	{
		int deltaHealth = (int)Math.floor(this.healthAccumulator);
		this.healthAccumulator = this.healthAccumulator - deltaHealth;
		
		Player player = this.getPlayer();
		
		int targetHealth = limitNumber(player.getHealth() + deltaHealth, 0, 20);
		player.setHealth(targetHealth);
	}
	
	// -------------------------------------------- //
	// Monster Truce Feature (Passive)
	// -------------------------------------------- //
	public boolean truceIsBroken()
	{
		return this.truceBreakTicksLeft != 0;
	}
	
	public void truceBreak()
	{
		if ( ! this.truceIsBroken())
		{
			this.msg(Lang.messageTruceBroken);
		}
		this.truceBreakTimeLeftSet(Conf.truceBreakTicks);
	}
	
	public void truceRestore()
	{
		this.msg(Lang.messageTruceRestored);
		this.truceBreakTimeLeftSet(0);
		
		Player me = this.getPlayer();
		
		// Untarget the player.
		for (LivingEntity entity : this.getPlayer().getWorld().getLivingEntities())
		{
			if ( ! (entity instanceof Creature))
			{
				continue;
			}
			
			if ( ! Conf.creatureTypeTruceMonsters.contains(EntityUtil.creatureTypeFromEntity(entity)))
			{
				continue;
			}
			
			Creature creature = (Creature)entity;
			LivingEntity target = creature.getTarget();
			if ( ! me.equals(target))
			{
				continue;
			}
			
			creature.setTarget(null);
		}
	}
	
	public void truceBreakAdvanceTime(long ticks)
	{
		if ( ! this.truceIsBroken()) return;
		
		this.truceBreakTimeLeftAlter(-ticks);
		
		if ( ! this.truceIsBroken())
		{
			this.truceRestore();
		}
	}
	
	public long truceBreakTimeLeftGet()
	{
		return this.truceBreakTicksLeft;
	}
	
	private void truceBreakTimeLeftSet(long milliseconds)
	{
		if (milliseconds < 0)
		{
			this.truceBreakTicksLeft = 0;
		}
		else
		{
			this.truceBreakTicksLeft = milliseconds;
		}
	}
	
	private void truceBreakTimeLeftAlter(long delta)
	{
		this.truceBreakTimeLeftSet(this.truceBreakTimeLeftGet() + delta);
	}
	
	// -------------------------------------------- //
	// Jump ability
	// -------------------------------------------- //
	public void jump(double deltaSpeed, boolean upOnly)
	{
		Player player = this.getPlayer();
		
		int targetFood = player.getFoodLevel() - Conf.jumpFoodCost;
		
		if (targetFood < 0) return;
		
		player.setFoodLevel(targetFood);
		
		Vector vjadd;
		if (upOnly)
		{
			vjadd = new Vector(0, 1, 0);
		}
		else
		{
			vjadd = player.getLocation().getDirection();
			vjadd.normalize();
		}
		vjadd.multiply(deltaSpeed);
		vjadd.setY(vjadd.getY() / 2.5D); // Compensates for the "in air friction" that not applies to y-axis.
		
		player.setVelocity(player.getVelocity().add(vjadd));
	}
	
	// -------------------------------------------- //
	// Combustion
	// -------------------------------------------- //
	public boolean combustAdvanceTime(long ticks)
	{
		if ( ! this.standsInSunlight()) return false;
		
		Player player = this.getPlayer();
		if (player.getFireTicks() <= 0)
		{
			this.msg(Lang.combustMessage);
		}
		
		player.setFireTicks((int) (ticks + Conf.combustFireExtinguishTicks));
		
		return true;
	}
	
	public boolean standsInSunlight()
	{
		Player player = this.getPlayer();
		
		// No need to set on fire if the water will put the fire out at once.
		Material material = player.getLocation().getBlock().getType();
		World playerWorld = player.getWorld();
		
		if (player.getWorld().getEnvironment() == Environment.NETHER) return false;
		if (this.worldTimeIsNight()) return false;
		if (material == Material.STATIONARY_WATER) return false;
		if (material == Material.WATER) return false;
		if (playerWorld.hasStorm()) return false;
		if (playerWorld.isThundering()) return false;
		if (this.isUnderRoof()) return false;
		
		return true;
	}
	
	public boolean isUnderRoof()
	{
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
				if (opacity == null)
				{
					retVal = true; // Blocks not in that map have opacity 1;
					break;
				}
			
				opacityAccumulator += opacity;
				if (opacityAccumulator >= 1.0D)
				{
					retVal = true;
					break;
				}
			}
		}
		return retVal;
	}
	
	public boolean worldTimeIsNight()
	{
		long time = this.getPlayer().getWorld().getTime() % 24000;
		
		if (time < Conf.combustFromTime || time > Conf.combustToTime) return true;
		
		return false; 
	}
	
	// -------------------------------------------- //
	// Infection 
	// -------------------------------------------- //
	
	
	
	public void infectionHeal(double amount)
	{
		if (this.isVampire())
		{
			return;
		}
		
		double current = this.getInfection();
		
		if (current == 0D )
		{
			// The player is already completely healthy
			return;
		}
		
		current -= amount; 
		
		if (current <= 0D)
		{
			this.setInfection(0D);
			this.msg(Lang.infectionMessageCured);
			return;
		}
		
		this.setInfection(current);
		this.msg(Lang.infectionMessageHeal);
	}
	
	// -------------------------------------------- //
	// Infection Natural Advancement
	// -------------------------------------------- //
	
	public void infectionAdvanceTime(long ticks)
	{
		this.infectionAdvance(ticks * Conf.infectionProgressPerTick);
	}
	
	public void infectionAdvance(double amount)
	{
		if (this.isVampire()) return; 
		
		int oldMessageIndex = this.infectionGetMessageIndex();
		this.alterInfection(amount);
		int newMessageIndex = this.infectionGetMessageIndex();
		
		if (this.getInfection() >= 100)
		{
			this.turn();
			return;
		}
		
		if (oldMessageIndex != newMessageIndex)
		{
			//P.p.log("WOOOP");
			this.getPlayer().damage(1);
			this.msg(Lang.infectionMessagesProgress.get(newMessageIndex));
			this.msg(Lang.infectionBreadHintMessages.get(MCore.random.nextInt(Lang.infectionBreadHintMessages.size())));
		}
	}
	
	public int infectionGetMessageIndex()
	{
		return (int)((Lang.infectionMessagesProgress.size()+1) * this.getInfection() / 100D) - 1;
	}
	
	// -------------------------------------------- //
	// Close Combat
	// -------------------------------------------- //
	
	public double getDamageDealtFactor()
	{
		if (this.intendingToInfect)
		{
			return Conf.damageDealtFactorWithIntent;
		}
		return Conf.damageDealtFactorWithoutIntent;
	}
	
	public double getDamageReceivedFactor()
	{
		if (this.intendingToInfect)
		{
			return Conf.damageReceivedFactorWithIntent;
		}
		return Conf.damageReceivedFactorWithoutIntent;
	}
	
	public double infectionGetRiskToInfectOther()
	{
		if (this.intendingToInfect)
		{
			return Conf.infectionRiskAtCloseCombatWithIntent;
		}
		return Conf.infectionRiskAtCloseCombatWithoutIntent;
	}
	
	public void infectionContract(VPlayer fromvplayer)
	{
		if (this.isVampire()) return;
		
		if (fromvplayer != null && this.makerId == null)
		{
			this.makerId = fromvplayer.getId();
		}
		
		p.log(this.getId() + " contracted vampirism infection."); // TODO: Better messages and
		
		this.infectionAdvance(1);
	}
	
	public void infectionContractRisk(VPlayer fromvplayer)
	{
		if (MCore.random.nextDouble() > fromvplayer.infectionGetRiskToInfectOther()) return;
		this.infectionContract(fromvplayer);
	}

	// -------------------------------------------- //
	// Assigned Permission Update
	// -------------------------------------------- //
	public void updateVampPermission()
	{
		if (this.permA != null)
		{
			this.permA.remove();
		}
		
		this.permA = this.getPlayer().addAttachment(P.p);
		
		if (this.isVampire())
		{
			for (Entry<String, Boolean> entry : Conf.giveThesePermissionsToVampires.entrySet())
			{
				this.permA.setPermission(entry.getKey(), entry.getValue());
			}
		}
		else
		{
			for (Entry<String, Boolean> entry : Conf.giveThesePermissionsToNonVampires.entrySet())
			{
				this.permA.setPermission(entry.getKey(), entry.getValue());
			}
		}
		
		// Debug
		//p.log(this.getId() + " had vamp permission updated to " + this.getPlayer().hasPermission(Permission.IS.node));
	}
	
	// -------------------------------------------- //
	// Commonly used limiter of double
	// -------------------------------------------- //
	public static <T extends Number> T limitNumber(T d, T min, T max)
	{
		if (d.doubleValue() < min.doubleValue())
		{
			return min;
		}
		
		if (d.doubleValue() > max.doubleValue())
		{
			return max;
		}
		
		return d;
	}
	
	public void msg(String str, Object... args)
	{
		this.sendMessage(P.p.txt.parse(str, args));
	}
}
