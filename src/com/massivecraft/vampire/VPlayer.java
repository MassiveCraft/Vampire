package com.massivecraft.vampire;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffectType;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.massivecraft.mcore3.MCore;
import com.massivecraft.mcore3.cmd.MCommand;
import com.massivecraft.mcore3.persist.IClassManager;
import com.massivecraft.mcore3.persist.PlayerEntity;
import com.massivecraft.mcore3.util.MUtil;
import com.massivecraft.mcore3.util.Txt;
import com.massivecraft.vampire.accumulator.VPlayerFoodAccumulator;
import com.massivecraft.vampire.accumulator.VPlayerHealthAccumulator;
import com.massivecraft.vampire.util.FxUtil;
import com.massivecraft.vampire.util.SunUtil;

/**
 * The VPlayer is a "skin" for a normal player.
 * Through this skin we can reach the player plus extra plugin specific data and functionality.
 */
public class VPlayer extends PlayerEntity<VPlayer>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	@Override public IClassManager<VPlayer> getManager() { return VPlayers.i; }
	@Override protected VPlayer getThis() { return this; }
	
	// -------------------------------------------- //
	// PERSISTENT FIELDS
	// -------------------------------------------- //
	
	// FIELD: vampire - Is the player a vampire?
	protected boolean vampire = false;
	public boolean vampire() { return this.vampire; }
	public boolean human() { return ! this.vampire(); } // Shortcut
	public void vampire(boolean val)
	{
		this.infection = 0;
		if (this.vampire == val) return;
		this.vampire = val;
		if (this.vampire)
		{
			this.msg(Lang.youWasTurned);
			this.fxSmokeRun();
		}
		else
		{
			this.msg(Lang.youWasCured);
			this.fxEnderRun();
			this.maker(null);
			this.reason(null);
			this.bloodlust(false);
			this.intend(false);
		}
		this.updateVampPermission();
		
		// We do not change spout-features if we are in creative-mode.
		if (this.isGameMode(GameMode.CREATIVE, true)) return;
		this.updateSpoutMovement();
	}
	
	// FIELD: infection - 0 means no infection. If infection reaches 1 the player will turn to vampire.
	protected double infection = 0; 
	public double infection() { return this.infection; }
	public boolean infected() { return this.infection > 0D; }
	public void infection(double val)
	{
		if (val >= 1D)
		{
			this.vampire(true);
		}
		else if (val <= 0D)
		{
			if (this.infection > 0D)
			{
				this.msg(Lang.infectionMessageCured);
			}
			this.infection = 0D;
		}
		else
		{
			this.infection = val;
		}
	}
	public void infectionAdd(double val)
	{
		this.infection(this.infection()+val);
	}
	public void infectionAdd(double val, InfectionReason reason, VPlayer maker)
	{
		if (vampire) return;
		this.reason(reason);
		this.makerId(maker == null ? null : maker.getId());
		P.p.log(Txt.parse(this.reasonDesc(false)));
		if (reason.notice()) this.msg(this.reasonDesc(true));
		this.infectionAdd(val);
	}
	
	// FIELD: healthy and unhealthy - Fake field shortcuts
	public boolean healthy() { return ! this.vampire() && ! this.infected(); }
	public boolean unhealthy() { return ! this.healthy(); }
	
	// FIELD: reason - How come this player is infected?
	protected InfectionReason reason;
	public InfectionReason reason() { return reason == null ? InfectionReason.UNKNOWN : reason; }
	public void reason(InfectionReason val) { this.reason = val; }
	public String reasonDesc(boolean self) { return this.reason().getDesc(this, self); }
	
	// FIELD: makerId - Who made this vampire?
	protected String makerId;
	public String makerId() { return this.makerId; }
	public void makerId(String val) { this.makerId = val; }
	public VPlayer maker() { return VPlayers.i.get(this.makerId); }
	public void maker(VPlayer val) { this.makerId(val == null ? null : val.getId()); }
	
	// FIELD: intending - Vampires may choose their combat style. Do they intend to infect others in combat or do they not?
	protected boolean intend = false;
	public boolean intend() { return intend; }
	public void intend(boolean val) { this.intend = val; this.msg(val ? Lang.xIsOn : Lang.xIsOff, "Infect intent"); }
	
	// FIELD: bloodlust - Is bloodlust activated?
	protected boolean bloodlust = false;
	public boolean bloodlust() { return bloodlust; }
	public void bloodlust(boolean val)
	{
		if (this.bloodlust == val)
		{
			// No real change - just view the info.
			this.msg(val ? Lang.xIsOn : Lang.xIsOff, "Bloodlust");
			return;
		}
		
		if (val)
		{
			// There is a few reasons to when you can turn it on.
			if (this.food().get() < Conf.bloodlustMinFood)
			{
				msg("<b>Your food is to low for bloodlust.");
				return;
			}
			
			if (this.isGameMode(GameMode.CREATIVE, true))
			{
				msg("<b>You cant use bloodlust while in creativemode."); // or offline :P but offline players wont see the message
				return;
			}
		}
		this.bloodlust = val;
		this.updateSpoutMovement();
	}
	
	// -------------------------------------------- //
	// TRANSIENT FIELDS
	// -------------------------------------------- //
	
	// FIELD: rad - The irradiation for the player.
	protected transient double rad = 0;
	public double rad() { return this.rad; }
	public void rad(double val) { this.rad = val; }
	
	// FIELD: temp - The temperature of the player. a double between 0 and 1.
	protected transient double temp = 0;
	public double temp() { return this.temp; }
	public void temp(double val) { this.temp = MUtil.limitNumber(val, 0D, 1D); }
	public void tempAdd(double val) { this.temp(this.temp()+val); }
	
	// FIELD: food - the food accumulator
	protected transient VPlayerFoodAccumulator food = new VPlayerFoodAccumulator(this);
	public VPlayerFoodAccumulator food() { return this.food; }
	
	// FIELD: health - the health accumulator
	protected transient VPlayerHealthAccumulator health = new VPlayerHealthAccumulator(this);
	public VPlayerHealthAccumulator health() { return this.health; }
	
	// FIELD: lastDamageMillis - for the regen
	protected transient long lastDamageMillis = 0;
	public long lastDamageMillis() { return this.lastDamageMillis; }
	public void lastDamageMillis(long val) { this.lastDamageMillis = val; }
	
	// FIELD: truceBreakTicksLeft - How many milliseconds more will the monsters be hostile?
	protected transient long truceBreakTicksLeft = 0;
	
	// FIELD: infectionOfferedFrom and infectionOfferedAtTicks - infect and accept commands.
	protected transient VPlayer infectionOfferedFrom;
	protected transient long infectionOfferedAtTicks;
	
	// FIELD: smokeTicksLeft - Container for the special effect
	protected transient int fxSmokeTicks = 0;
	public int fxSmokeTicks() { return this.fxSmokeTicks; }
	public void fxSmokeTicks(int val) { this.fxSmokeTicks = val; }
	public void fxSmokeRun() { this.fxSmokeTicks = 20 * 20; }
	
	// FIELD: enderTicksLeft - Container for the special effect
	protected transient int fxEnderTicks = 0;
	public int fxEnderTicks() { return this.fxEnderTicks; }
	public void fxEnderTicks(int val) { this.fxEnderTicks = val; }
	public void fxEnderRun() { this.fxEnderTicks = 10 * 20; }
	
	// FIELD: permA - permission assignments
	protected transient PermissionAttachment permA;
	
	// -------------------------------------------- //
	// TICK
	// -------------------------------------------- //
	public void tick(long ticks)
	{
		this.tickRadTemp(ticks);
		this.tickInfection(ticks);
		this.tickRegen(ticks);
		this.tickBloodlust(ticks);
		this.tickEffects(ticks);
		this.tickTruce(ticks);
	}
	
	public void tickRadTemp(long ticks)
	{
		// Update rad and temp
		Player player = this.getPlayer();
		boolean survival = player.getGameMode() == GameMode.SURVIVAL;
		if (survival && this.vampire() && ! player.isDead())
		{
			this.rad = Conf.baseRad + SunUtil.calcPlayerIrradiation(player);
			Double tempDelta = Conf.tempPerRadAndTick * this.rad * ticks;
			this.tempAdd(tempDelta);
		}
		else
		{
			this.rad = 0;
			this.temp = 0;
		}
		
		//P.p.log("this.rad ", this.rad);
		//P.p.log("this.temp ", this.temp);
	}
	
	public void tickInfection(long ticks)
	{
		if ( ! this.infected()) return;
		int indexOld = this.infectionGetMessageIndex();
		this.infectionAdd(ticks * Conf.infectionPerTick);
		int indexNew = this.infectionGetMessageIndex();
		
		if (this.vampire()) return;
		if (indexOld == indexNew) return;
		
		Player player = this.getPlayer();
		
		if (Conf.infectionProgressDamage != 0) player.damage(Conf.infectionProgressDamage);
		if (Conf.infectionProgressNauseaTicks > 0) FxUtil.ensure(PotionEffectType.CONFUSION, player, Conf.infectionProgressNauseaTicks);
		
		this.msg(Lang.infectionMessagesProgress.get(indexNew));
		this.msg(Lang.infectionBreadHintMessages.get(MCore.random.nextInt(Lang.infectionBreadHintMessages.size())));
	}
	public int infectionGetMessageIndex()
	{
		return (int)((Lang.infectionMessagesProgress.size()+1) * this.infection() / 100D) - 1;
	}
	
	public void tickRegen(long ticks)
	{
		if ( ! this.vampire()) return;
		Player player = this.getPlayer();
		if (player.isDead()) return;
		if (player.getHealth() >= 20) return;
		if (this.food().get() < Conf.regenMinFood) return;
		
		long millisSinceLastDamage = System.currentTimeMillis() - this.lastDamageMillis;
		if (millisSinceLastDamage < Conf.regenDelayMillis) return;
		
		double foodDiff = this.food().add(-Conf.regenFoodPerTick * ticks);
		this.health().add(-foodDiff * Conf.regenHealthPerFood);
	}
	
	public void tickBloodlust(long ticks)
	{
		if ( ! this.vampire()) return;
		if ( ! this.bloodlust()) return;
		Player player = this.getPlayer();
		if (player.isDead()) return;
		
		this.food().add(ticks * Conf.bloodlustFoodPerTick);
		if (this.food().get() < Conf.bloodlustMinFood) this.bloodlust(false);
	}
	
	public void tickEffects(long ticks)
	{
		Player player = this.getPlayer();
		if ( ! player.isOnline()) return;
		if (player.isDead()) return;
		
		boolean survival = player.getGameMode() == GameMode.SURVIVAL;
		
		// The generic smoke effect
		if (this.fxSmokeTicks > 0)
		{
			this.fxSmokeTicks -= ticks;
			double dcount = Conf.fxSmokePerTick * ticks;
			long lcount = MUtil.probabilityRound(dcount);
			for (long i = lcount; i > 0; i--) FxUtil.smoke(player);
		}
		
		// The ender effect
		if (this.fxEnderTicks > 0)
		{
			this.fxEnderTicks -= ticks;
			double dcount = Conf.fxEnderPerTick * ticks;
			long lcount = MUtil.probabilityRound(dcount);
			for (long i = lcount; i > 0; i--) FxUtil.ender(player, Conf.fxEnderRandomMaxLen);
		}
		
		// Vampire sun reactions
		if (survival && this.vampire())
		{
			// Buffs
			if (this.temp() > Conf.sunNauseaTemp)    FxUtil.ensure(PotionEffectType.CONFUSION, player, Conf.sunNauseaTicks);
			if (this.temp() > Conf.sunSlowTemp)      FxUtil.ensure(PotionEffectType.SLOW, player, Conf.sunSlowTicks);
			if (this.temp() > Conf.sunBlindnessTemp) FxUtil.ensure(PotionEffectType.BLINDNESS, player, Conf.sunBlindnessTicks);
			if (this.temp() > Conf.sunBurnTemp)      FxUtil.ensureBurn(player, Conf.sunBurnTicks);
			
			// Fx
			double dsmokes = Conf.sunSmokesPerTempAndTick * this.temp * ticks;
			long lsmokes = MUtil.probabilityRound(dsmokes);
			for (long i = lsmokes; i > 0; i--) FxUtil.smoke(player);
			
			double dflames = Conf.sunFlamesPerTempAndTick * this.temp * ticks;
			long lflames = MUtil.probabilityRound(dflames);
			for (long i = lflames; i > 0; i--) FxUtil.flame(player);
		}
	}
	
	// -------------------------------------------- //
	// OFFER AND ACCEPT INFECTION
	// -------------------------------------------- //
	public void infectionAccept()
	{
		VPlayer vyou = this.infectionOfferedFrom;
		Player me = this.getPlayer();
		if (vyou == null || vyou.isOffline() || me == null || System.currentTimeMillis() - this.infectionOfferedAtTicks > Conf.infectOfferToleranceTicks)
		{
			this.msg(Lang.infectNoRecentOffer);
			return;
		}
		Player you = vyou.getPlayer();
		
		// Check the player-distance
		Location l1 = me.getLocation();
		Location l2 = you.getLocation();
		
		if ( ! l1.getWorld().equals(l2.getWorld()) || l1.distance(l2) > Conf.infectOfferMaxDistance)
		{
			me.sendMessage(Txt.parse(Lang.infectYouMustStandCloseToY, you.getDisplayName()));
			return;
		}
		
		//this.msg(Lang.infectYouDrinkSomeOfXBlood, you.getDisplayName());
		vyou.msg(Lang.infectXDrinkSomeOfYourBlood, me.getDisplayName());
		you.damage(2);
		
		if (this.vampire()) return;
		this.infectionAdd(5.0D, InfectionReason.OFFER, vyou);
	}
	
	public void infectionOffer(VPlayer vyou)
	{
		Player me = this.getPlayer();
		Player you = vyou.getPlayer();
		
		// Check the player-distance
		Location l1 = me.getLocation();
		Location l2 = you.getLocation();
		
		if ( ! l1.getWorld().equals(l2.getWorld()) || l1.distance(l2) > Conf.infectOfferMaxDistance)
		{
			this.msg(Lang.infectYouMustStandCloseToY, you.getDisplayName());
			return;
		}
		
		vyou.infectionOfferedFrom = this;
		vyou.infectionOfferedAtTicks = System.currentTimeMillis();
		vyou.msg(Lang.infectXOffersToInfectYou, me.getDisplayName());
		
		List<MCommand> cmdc = new ArrayList<MCommand>();
		cmdc.add(P.p.cmdBase);
		vyou.msg(Lang.infectTypeXToAccept, P.p.cmdBase.cmdAccept.getUseageTemplate(cmdc, false));
		this.msg(Lang.infectYouOfferToInfectX, you.getDisplayName());
	}
	
	// -------------------------------------------- //
	// TRUCE
	// -------------------------------------------- //
	public void tickTruce(long ticks)
	{
		if ( ! this.vampire()) return;
		if ( ! this.truceIsBroken()) return;
		
		this.truceBreakTicksLeftAlter(-ticks);
		
		if ( ! this.truceIsBroken())
		{
			this.truceRestore();
		}
	}
	
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
		this.truceBreakTicksLeftSet(Conf.truceBreakTicks);
	}
	
	public void truceRestore()
	{
		this.msg(Lang.messageTruceRestored);
		this.truceBreakTicksLeftSet(0);
		
		Player me = this.getPlayer();
		
		// Untarget the player.
		for (LivingEntity entity : this.getPlayer().getWorld().getLivingEntities())
		{
			if ( ! (entity instanceof Creature))
			{
				continue;
			}
			
			if ( ! Conf.truceEntityTypes.contains(entity.getType()))
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
	
	public long truceBreakTicksLeftGet()
	{
		return this.truceBreakTicksLeft;
	}
	
	private void truceBreakTicksLeftSet(long ticks)
	{
		if (ticks < 0)
		{
			this.truceBreakTicksLeft = 0;
		}
		else
		{
			this.truceBreakTicksLeft = ticks;
		}
	}
	
	private void truceBreakTicksLeftAlter(long delta)
	{
		this.truceBreakTicksLeftSet(this.truceBreakTicksLeftGet() + delta);
	}
	
	// -------------------------------------------- //
	// Jump ability
	// -------------------------------------------- //
	/*public void jump(double deltaSpeed, boolean upOnly)
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
	}*/
	
	
	
	// -------------------------------------------- //
	// Close Combat
	// -------------------------------------------- //
	
	public double getDamageDealtFactor()
	{
		if (this.intend) return Conf.damageDealtFactorWithIntent;
		return Conf.damageDealtFactorWithoutIntent;
	}
	
	public double getDamageReceivedFactor()
	{
		if (this.intend) return Conf.damageReceivedFactorWithIntent;
		return Conf.damageReceivedFactorWithoutIntent;
	}
	
	public double infectionGetRiskToInfectOther()
	{
		if (this.intend) return Conf.infectionRiskAtCloseCombatWithIntent;
		return Conf.infectionRiskAtCloseCombatWithoutIntent;
	}

	// -------------------------------------------- //
	// UPDATE 
	// -------------------------------------------- //
	
	public void updateVampPermission()
	{
		if (this.permA != null)
		{
			this.permA.remove();
		}
		
		this.permA = this.getPlayer().addAttachment(P.p);
		
		if (this.vampire())
		{
			for (Entry<String, Boolean> entry : Conf.permissionsGrantVampire.entrySet())
			{
				this.permA.setPermission(entry.getKey(), entry.getValue());
			}
		}
		else
		{
			for (Entry<String, Boolean> entry : Conf.permissionsGrantHuman.entrySet())
			{
				this.permA.setPermission(entry.getKey(), entry.getValue());
			}
		}
		
		// Debug
		//p.log(this.getId() + " had vamp permission updated to " + this.getPlayer().hasPermission(Permission.IS.node));
	}
	
	public void updateSpoutMovement()
	{
		Player player = this.getPlayer();
		if (player == null) return;
		SpoutPlayer splayer = SpoutManager.getPlayer(player);
		
		Double multGravity = null;
		Double multSwimming = null;
		Double multWalking = null;
		Double multJumping = null;
		Double multAirSpeed = null;
		String noSpoutWarn = null;		
		
		if (this.vampire() && this.bloodlust())
		{
			multGravity = Conf.multGravityBloodlust;
			multSwimming = Conf.multSwimmingBloodlust;
			multWalking = Conf.multWalkingBloodlust;
			multJumping = Conf.multJumpingBloodlust;
			multAirSpeed = Conf.multAirSpeedBloodlust;
			noSpoutWarn = Lang.noSpoutWarnBloodlust;
		}
		else if (this.vampire())
		{
			multGravity = Conf.multGravityVamp;
			multSwimming = Conf.multSwimmingVamp;
			multWalking = Conf.multWalkingVamp;
			multJumping = Conf.multJumpingVamp;
			multAirSpeed = Conf.multAirSpeedVamp;
			noSpoutWarn = Lang.noSpoutWarnVamp;
		}
		else
		{
			multGravity = Conf.multGravityHuman;
			multSwimming = Conf.multSwimmingHuman;
			multWalking = Conf.multWalkingHuman;
			multJumping = Conf.multJumpingHuman;
			multAirSpeed = Conf.multAirSpeedHuman;
			noSpoutWarn = Lang.noSpoutWarnHuman;
		}

		if ( ! splayer.isSpoutCraftEnabled() && noSpoutWarn != null) this.msg(Txt.wrap(noSpoutWarn));
		
		if (multGravity != null) splayer.setGravityMultiplier(multGravity);
		if (multSwimming != null) splayer.setSwimmingMultiplier(multSwimming);
		if (multWalking != null) splayer.setWalkingMultiplier(multWalking);
		if (multJumping != null) splayer.setJumpingMultiplier(multJumping);
		if (multAirSpeed != null) splayer.setAirSpeedMultiplier(multAirSpeed);
	}
}
