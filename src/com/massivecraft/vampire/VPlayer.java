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
	
	private boolean isVampire = false;
	private boolean isTrueBlood = false;
	//private double blood = 100;
	private double infection = 0; // 0 means no infection. If infection reaches 100 the player will turn to vampire.
	private long timeAsVampire = 0; // The total amount of milliseconds this player has been vampire.
	private long truceBreakTimeLeft = 0; // How many milliseconds more will the monsters be hostile?
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
	// Get Conf
	// -------------------------------------------- //
	public VampireTypeConf getConf()
	{
		if (this.isTrueBlood)
		{
			return p.confVampTypeOriginal;
		}
		return p.confVampTypeCommon;
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
		long milliseconds = ticks * 1000 / 20;
		
		if (this.isVampire())
		{
			this.timeAsVampire += milliseconds;
			this.combustAdvanceTime(milliseconds);
			this.truceBreakAdvanceTime(milliseconds);
		}
		else if (this.isInfected())
		{
			this.infectionAdvanceTime(milliseconds);
		}
	}
	
	// -------------------------------------------- //
	// Vampire
	// -------------------------------------------- //
	
	public boolean isVampire()
	{
		return this.isVampire;
	}	
	
	public boolean isTrueBlood()
	{
		return this.isTrueBlood;
	}
	
	public void setIsTrueBlood(boolean val)
	{
		this.isTrueBlood = val;
	}
	
	public void turn()
	{
		this.isVampire = true;
		this.infectionSet(0);
		
		if(this.isTrueBlood)
		{
			this.msg(Lang.turnTrueBloodMessages);
			p.log(this.getId() + " turned into a TrueBlood vampire.");
		}
		else
		{
			this.msg(Lang.turnMessages);
			p.log(this.getId() + " turned into a common vampire.");
		}
	}
	
	public void cure()
	{
		this.isVampire = false;
		this.isTrueBlood = false;
		this.infectionSet(0);
		this.msg(Lang.cureMessages);
		//Vampire.log(this.playername + " was cured and is no longer a vampire.");
	}
	
	public boolean isExvampire()
	{
		return ( ! this.isVampire() && this.timeAsVampire > 0);
	}
	
	// -------------------------------------------- //
	// Food. The food is handled as a double from 0 to 20
	// This system uses an accumulator to wrap the int in a double
	// -------------------------------------------- //
	
	public void foodSet(double food)
	{
		food = this.limitDouble(food, 0, 20);
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
		return this.truceBreakTimeLeft != 0;
	}
	
	public void truceBreak()
	{
		if ( ! this.truceIsBroken())
		{
			this.msg(p.txt.parse(Lang.messageTruceBroken));
		}
		this.truceBreakTimeLeftSet(GeneralConf.truceBreakTime);
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
			
			if ( ! GeneralConf.creatureTypeTruceMonsters.contains(EntityUtil.creatureTypeFromEntity(entity)))
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
	
	public void truceBreakAdvanceTime(long milliseconds)
	{
		if ( ! this.truceIsBroken())
		{
			return;
		}
		
		this.truceBreakTimeLeftAlter(-milliseconds);
		
		if ( ! this.truceIsBroken())
		{
			this.truceRestore();
		}
	}
	
	public long truceBreakTimeLeftGet()
	{
		return this.truceBreakTimeLeft;
	}
	
	private void truceBreakTimeLeftSet(long milliseconds)
	{
		if (milliseconds < 0)
		{
			this.truceBreakTimeLeft = 0;
		}
		else
		{
			this.truceBreakTimeLeft = milliseconds;
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
		
		int targetFood = player.getFoodLevel() - this.getConf().jumpFoodCost;
		
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
	public boolean combustAdvanceTime(long milliseconds)
	{
		if ( ! this.standsInSunlight()) return false;
		if ( ! this.getConf().burnInSunlight) return false;
		
		
		// We assume the next tick will be in milliseconds milliseconds.
		
		int ticksTillNext = (int) (milliseconds / 1000D * 20D); // 20 minecraft ticks is a second.
		ticksTillNext += 5; // just to be on the safe side.
		
		Player player = this.getPlayer();
		if (player.getFireTicks() <= 0)
		{
			this.msg(p.txt.parse(Lang.combustMessage));
		}
		
		player.setFireTicks(ticksTillNext + GeneralConf.combustFireExtinguishTicks);
		
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
			
				opacity = GeneralConf.materialOpacity.get(blockCurrent.getType());
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
	
	public boolean worldTimeIsNight()
	{
		long time = this.getPlayer().getWorld().getTime() % 24000;
		
		if (time < GeneralConf.combustFromTime || time > GeneralConf.combustToTime) return true;
		
		return false; 
	}
	
	// -------------------------------------------- //
	// Infection 
	// -------------------------------------------- //
	public boolean isInfected()
	{
		return this.infection > 0D && this.isVampire == false;
	}
	
	public double infectionGet()
	{
		return this.infection;
	}
	
	public void infectionSet(double infection)
	{
		this.infection = this.limitDouble(infection, 0D, 100D);
	}
	
	public void infectionAlter(double infection)
	{
		this.infectionSet(this.infectionGet() + infection);
	}
	
	public void infectionHeal(double amount)
	{
		if (this.isVampire())
		{
			return;
		}
		
		double current = this.infectionGet();
		
		if (current == 0D )
		{
			// The player is already completely healthy
			return;
		}
		
		current -= amount; 
		
		if (current <= 0D)
		{
			this.infectionSet(0D);
			this.msg(p.txt.parse(Lang.infectionMessageCured));
			return;
		}
		
		this.infectionSet(current);
		this.msg(p.txt.parse(Lang.infectionMessageHeal));
	}
	
	public void infectionAdvanceTime(long milliseconds)
	{
		this.infectionAdvance(GeneralConf.infectionProgressPerSecond * milliseconds / 1000D );
	}
	
	public void infectionAdvance(double amount)
	{
		if (this.isVampire())
		{
			return;
		}
		
		int oldMessageIndex = this.infectionGetMessageIndex();
		this.infectionAlter(amount);
		int newMessageIndex = this.infectionGetMessageIndex();
		
		if (this.infectionGet() == 100)
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
		return (int)((Lang.infectionMessagesProgress.size()+1) * this.infectionGet() / 100D) - 1;
	}
	
	public void infectionRisk()
	{
		//Vampire.log(this.playername + " risked infection.");
		if (P.random.nextDouble() <= GeneralConf.infectionCloseCombatRisk)
		{
			p.log(this.getId() + " contracted vampirism infection.");
			this.infectionAdvance(GeneralConf.infectionCloseCombatAmount);
		}
	}
	
	// -------------------------------------------- //
	// Altar Usage
	// -------------------------------------------- //
	
	public void useAltarInfect(Block centerBlock)
	{
		// The altar must be big enough
		int count = GeometryUtil.countNearby(centerBlock, GeneralConf.altarInfectMaterialSurround, GeneralConf.altarInfectMaterialSurroundRadious);
		if (count == 0)
		{
			return;
		}
		
		this.msg(" ");
		
		if (count < GeneralConf.altarInfectMaterialSurroundCount)
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
		if (GeneralConf.altarInfectRecipe.playerHasEnough(this.getPlayer()))
		{
			this.msg(Lang.altarUseIngredientsSuccess);
			this.msg(GeneralConf.altarInfectRecipe.getRecipeLine());
			this.msg(Lang.altarInfectUse);
			p.log(this.getId() + " was infected by an evil altar.");
			GeneralConf.altarInfectRecipe.removeFromPlayer(this.getPlayer());
			this.infectionAlter(3D);
			this.isTrueBlood = true;
		}
		else
		{
			this.msg(Lang.altarUseIngredientsFail);
			this.msg(GeneralConf.altarInfectRecipe.getRecipeLine());
		}
	}
	
	public void useAltarCure(Block centerBlock)
	{
		// The altar must be big enough;
		int count = GeometryUtil.countNearby(centerBlock, this.getConf().altarCureMaterialSurround, this.getConf().altarCureMaterialSurroundRadious);
		if (count == 0)
		{
			return;
		}
		
		this.msg(" ");
		
		if (count < this.getConf().altarCureMaterialSurroundCount)
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
			this.infectionSet(0);
			this.msg(p.txt.parse(Lang.infectionMessageCured));
			return;
		}
		
		// Is vampire and thus can be cured...
		if(this.getConf().altarCureRecipe.playerHasEnough(this.getPlayer()))
		{
			this.msg(Lang.altarUseIngredientsSuccess);
			this.msg(this.getConf().altarCureRecipe.getRecipeLine());
			this.msg(Lang.altarCureUse);
			this.getConf().altarCureRecipe.removeFromPlayer(this.getPlayer());
			p.log(this.getId() + " was cured from being a vampire by a healing altar.");
			this.cure();
		} 
		else
		{
			this.msg(Lang.altarUseIngredientsFail);
			this.msg(this.getConf().altarCureRecipe.getRecipeLine());
		}
	}

	// -------------------------------------------- //
	// Commonly used limiter of double
	// -------------------------------------------- //
	public double limitDouble(double d, double min, double max)
	{
		if (d < min)
		{
			return min;
		}
		
		if (d > max)
		{
			return max;
		}
		
		return d;
	}
	
	// -------------------------------------------- //
	// Persistance
	// -------------------------------------------- //
	
	@Override
	public boolean shouldBeSaved()
	{
		return this.isVampire() || this.isInfected() || this.isExvampire();
	}
}
