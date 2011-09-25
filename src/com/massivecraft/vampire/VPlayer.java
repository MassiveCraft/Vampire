package com.massivecraft.vampire;

import org.bukkit.Bukkit;
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
import com.massivecraft.vampire.util.EntityUtil;
import com.massivecraft.vampire.util.GeometryUtil;
import com.massivecraft.vampire.zcore.persist.PlayerEntity;

/**
 * The VPlayer is a "skin" for a normal player.
 * Through this skin we can reach the player plus extra plugin specific data and functionality.
 */
public class VPlayer extends PlayerEntity
{
	public static transient P p = P.p;
	
	// Is the player a vampire?
	private boolean isVampire = false;
	public boolean isVampire() { return this.isVampire; }
	public void setIsVampire(boolean isVampire) { this.isVampire = isVampire; }
	public boolean isExvampire() { return this.isVampire() == false && this.timeAsVampire > 0; }
		
	// 0 means no infection. If infection reaches 100 the player will turn to vampire.
	private double infection = 0; 
	public double getInfection() { return this.infection; }
	public void setInfection(double infection) { this.infection = limitNumber(infection, 0D, 100D); }	
	public void alterInfection(double infection) { this.setInfection(this.getInfection() + infection); }
	public boolean isInfected() { return this.infection > 0D; }
	
	// Vampires may choose their combat style. Do they intend to infect others in combat or do they not?
	private boolean intendingToInfect = false;
	public boolean isIntendingToInfect() { return intendingToInfect; }
	public void setIntendingToInfect(boolean intendingToInfect) { this.intendingToInfect = intendingToInfect; }

	private String makerId;
	// TODO: Extend the maker and turn reason concept!!
	
	private long timeAsVampire = 0; // The total amount of milliseconds this player has been vampire.
	private long truceBreakTicksLeft = 0; // How many milliseconds more will the monsters be hostile?
	private transient double foodAccumulator = 0;
	//public transient long regenDelayLeftMilliseconds = 0;
	
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
		
		this.setInfection(0);
		this.setIsVampire(true);
		this.msg(p.txt.parse(Lang.youWasTurned));
	}
	
	public void cureVampirism()
	{
		this.setInfection(0);
		this.setIsVampire(false);
		this.msg(p.txt.parse(Lang.youWasCured));
	}
	
	// -------------------------------------------- //
	// Food. The food is handled as a double from 0 to 20
	// This system uses an accumulator to wrap the int in a double
	// -------------------------------------------- //
	
	public void foodSet(double food)
	{
		food = limitNumber(food, 0d, 20d);
		int targetFood = (int)Math.floor(food);
		this.foodAccumulator = food - targetFood;
		this.getPlayer().setFoodLevel(targetFood);
	}
	
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
		
		int targetFood = player.getFoodLevel() + deltaFood;
		player.setFoodLevel(targetFood);
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
			this.msg(p.txt.parse(Lang.messageTruceBroken));
		}
		this.truceBreakTimeLeftSet(Conf.truceBreakTicks);
	}
	
	public void truceRestore()
	{
		this.msg(p.txt.parse(Lang.messageTruceRestored));
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
			this.msg(p.txt.parse(Lang.combustMessage));
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
		
		if
		(
			player.getWorld().getEnvironment() == Environment.NETHER
			||
			this.worldTimeIsNight()
			||
			this.isUnderRoof()
			||
			material == Material.STATIONARY_WATER
			||
			material == Material.WATER
			||
			playerWorld.hasStorm()
			||
			playerWorld.isThundering()
		)
		{
			return false;
		}
		
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
			this.msg(p.txt.parse(Lang.infectionMessageCured));
			return;
		}
		
		this.setInfection(current);
		this.msg(p.txt.parse(Lang.infectionMessageHeal));
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
			this.getPlayer().damage(1);
			this.msg(p.txt.parse(Lang.infectionMessagesProgress.get(newMessageIndex)));
			this.msg(p.txt.parse(Lang.infectionBreadHintMessages.get(P.random.nextInt(Lang.infectionBreadHintMessages.size()))));
		}
	}
	
	public int infectionGetMessageIndex()
	{
		return (int)((Lang.infectionMessagesProgress.size()+1) * this.getInfection() / 100D) - 1;
	}
	
	// -------------------------------------------- //
	// Infection Close Combat Contract Risk
	// -------------------------------------------- //
	
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
		if (P.random.nextDouble() > fromvplayer.infectionGetRiskToInfectOther()) return;
		this.infectionContract(fromvplayer);
	}
	
	// -------------------------------------------- //
	// Altar Usage TODO: Abstract into the Altar Class
	// -------------------------------------------- //
	
	public void useAltarInfect(Block centerBlock)
	{
		// The altar must be big enough
		int count = GeometryUtil.countNearby(centerBlock, Conf.altarInfect.materialSurround, Conf.altarInfect.surroundRadius);
		if (count == 0)
		{
			return;
		}
		
		this.msg(" ");
		
		if (count < Conf.altarInfect.surroundCount)
		{
			this.msg(Lang.altarInfectToSmall);
			return;
		}
		
		// Always examine first
		this.msg(Lang.altarInfectExamineMsg);
		
		// Is Vampire
		if (this.isVampire())
		{
			this.msg(Lang.altarInfectExamineMsgNoUse);
			return;
		}
		
		// Is Infected
		if (this.isInfected())
		{
			this.msg(Lang.altarInfectExamineWhileInfected);
			return;
		}
		
		// Is healthy and thus can be infected...
		if (Conf.altarInfect.recipe.playerHasEnough(this.getPlayer()))
		{
			this.msg(Lang.altarUseIngredientsSuccess);
			this.msg(Conf.altarInfect.recipe.getRecipeLine());
			this.msg(Lang.altarInfectUse);
			p.log(this.getId() + " was infected by an evil altar.");
			Conf.altarInfect.recipe.removeFromPlayer(this.getPlayer());
			this.alterInfection(3D);
		}
		else
		{
			this.msg(Lang.altarUseIngredientsFail);
			this.msg(Conf.altarInfect.recipe.getRecipeLine());
		}
	}
	
	public void useAltarCure(Block centerBlock)
	{
		// The altar must be big enough;
		int count = GeometryUtil.countNearby(centerBlock, Conf.altarCure.materialSurround, Conf.altarCure.surroundRadius);
		if (count == 0)
		{
			return;
		}
		
		this.msg(" ");
		
		if (count < Conf.altarCure.surroundCount)
		{
			this.msg(Lang.altarCureToSmall);
			return;
		}
		
		// Always examine first
		this.msg(Lang.altarCureExamineMsg);
		
		// If healthy
		if ( ! this.isInfected() && ! this.isVampire())
		{
			this.msg(Lang.altarCureExamineMsgNoUse);
			return;
		}
		
		// If Infected
		if (this.isInfected())
		{
			this.msg(Lang.altarCureExamineWhileInfected);
			this.setInfection(0);
			this.msg(p.txt.parse(Lang.infectionMessageCured));
			return;
		}
		
		// Is vampire and thus can be cured...
		if(Conf.altarCure.recipe.playerHasEnough(this.getPlayer()))
		{
			this.msg(Lang.altarUseIngredientsSuccess);
			this.msg(Conf.altarCure.recipe.getRecipeLine());
			this.msg(Lang.altarCureUse);
			Conf.altarCure.recipe.removeFromPlayer(this.getPlayer());
			p.log(this.getId() + " was cured from being a vampire by a healing altar.");
			this.cureVampirism();
		} 
		else
		{
			this.msg(Lang.altarUseIngredientsFail);
			this.msg(Conf.altarCure.recipe.getRecipeLine());
		}
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
	
}
