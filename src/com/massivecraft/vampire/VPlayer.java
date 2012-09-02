package com.massivecraft.vampire;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffectType;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.massivecraft.mcore4.MCore;
import com.massivecraft.mcore4.cmd.MCommand;
import com.massivecraft.mcore4.store.PlayerEntity;
import com.massivecraft.mcore4.util.MUtil;
import com.massivecraft.mcore4.util.Txt;
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
	@Override protected VPlayer getThis() { return this; }
	
	private final static transient VPlayer defaultInstance = new VPlayer();
	@Override public VPlayer getDefaultInstance() { return defaultInstance; }
	@Override protected Class<VPlayer> getClazz() { return VPlayer.class; }
	
	public static VPlayer get(Object worldNameExtractable)
	{
		return VPlayerColls.i.get2(worldNameExtractable);
	}
	
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
		this.changed();
		if (this.vampire)
		{
			this.msg(Lang.vampireTrue);
			this.fxShriekRun();
			this.fxSmokeBurstRun();
			this.fxSmokeRun();
		}
		else
		{
			this.msg(Lang.vampireFalse);
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
				this.msg(Lang.infectionCured);
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
	public VPlayer maker() { return this.getColl().get(this.makerId); }
	public void maker(VPlayer val) { this.makerId(val == null ? null : val.getId()); }
	
	// FIELD: intending - Vampires may choose their combat style. Do they intend to infect others in combat or do they not?
	protected boolean intend = false;
	public boolean intend() { return intend; }
	public void intend(boolean val)
	{
		this.intend = val;
		this.msg(this.intendMsg());
	}
	public String intendMsg() { return Lang.boolIsY("Infect intent", this.intend())+ " " + Lang.quotaIsPercent("Combat infect risk", this.combatInfectRisk()); }
	
	// FIELD: bloodlust - Is bloodlust activated?
	protected boolean bloodlust = false;
	public boolean bloodlust() { return bloodlust; }
	public void bloodlust(boolean val)
	{
		if (this.bloodlust == val)
		{
			// No real change - just view the info.
			this.msg(Lang.boolIsY("Bloodlust", val));
			return;
		}
		
		Player player = this.getPlayer();
		if (val && player != null)
		{
			// There are a few rules to when you can turn it on:
			if ( ! this.vampire())
			{
				msg(Lang.onlyVampsCanX, "use bloodlust");
				return;
			}
			
			if (this.food().get() < Conf.get(player).bloodlustMinFood)
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
		this.msg(this.bloodlustMsg());
		this.updateSpoutMovement();
	}
	public String bloodlustMsg() { return Lang.boolIsY("Bloodlust", this.bloodlust()) + " " + Lang.quotaIsPercent("combat damage", this.combatDamageFactor()); }
	
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
	
	// FIELD: lastShriekMillis - for the shriek ability
	protected transient long lastShriekMillis = 0;
	public long lastShriekMillis() { return this.lastShriekMillis; }
	public void lastShriekMillis(long val) { this.lastShriekMillis = val; }
	
	// FIELD: lastShriekMessageMillis - Anti pwnage
	protected transient long lastShriekWaitMessageMillis = 0;
	public long lastShriekMessageMillis() { return this.lastShriekWaitMessageMillis; }
	public void lastShriekMessageMillis(long val) { this.lastShriekWaitMessageMillis = val; }
	
	// FIELD: truceBreakTicksLeft - How many milliseconds more will the monsters be hostile?
	protected transient long truceBreakTicksLeft = 0;
	
	// FIELD: infectionOffered data - for the offer and accept commands.
	protected transient VPlayer tradeOfferedFrom;
	protected transient double tradeOfferedAmount;
	protected transient long tradeOfferedAtMillis;
	
	// FIELD: permA - permission assignments
	protected transient PermissionAttachment permA;
	
	// -------------------------------------------- //
	// FX
	// -------------------------------------------- //
	
	// FX: Smoke
	protected transient int fxSmokeTicks = 0;
	public int fxSmokeTicks() { return this.fxSmokeTicks; }
	public void fxSmokeTicks(int val) { this.fxSmokeTicks = val; }
	public void fxSmokeRun() { this.fxSmokeTicks = 20 * 20; }
	
	// FX: Ender
	protected transient int fxEnderTicks = 0;
	public int fxEnderTicks() { return this.fxEnderTicks; }
	public void fxEnderTicks(int val) { this.fxEnderTicks = val; }
	public void fxEnderRun() { this.fxEnderTicks = 10 * 20; }
	
	// FX: Shriek
	public void fxShriekRun()
	{
		Player player = this.getPlayer();
		if (player == null) return;
		Location location = player.getLocation();
		World world = location.getWorld();
		world.playEffect(location, Effect.GHAST_SHRIEK, 0);
	}
	
	// FX: SmokeBurst
	public void fxSmokeBurstRun()
	{
		Player player = this.getPlayer();
		if (player == null) return;
		double dcount = Conf.get(player).fxSmokeBurstCount;
		long lcount = MUtil.probabilityRound(dcount);
		for (long i = lcount; i > 0; i--) FxUtil.smoke(player);
	}
	
	// FX: EnderBurst
	public void fxEnderBurstRun()
	{
		Player player = this.getPlayer();
		if (player == null) return;
		double dcount = Conf.get(player).fxEnderBurstCount;
		long lcount = MUtil.probabilityRound(dcount);
		for (long i = lcount; i > 0; i--) FxUtil.ender(player, 0);
	}
	
	// FX: FlameBurst
	public void fxFlameBurstRun()
	{
		Player player = this.getPlayer();
		if (player == null) return;
		double dcount = Conf.get(player).fxFlameBurstCount;
		long lcount = MUtil.probabilityRound(dcount);
		for (long i = lcount; i > 0; i--) FxUtil.flame(player);
	}
	
	// -------------------------------------------- //
	// SHRIEK
	// -------------------------------------------- //
	
	public void shriek()
	{
		// You must be online to shriek
		Player player = this.getPlayer();
		Conf conf = Conf.get(player);
		if (player == null) return;
		
		// You must be a vampire to shriek
		if ( ! this.vampire())
		{
			msg(Lang.onlyVampsCanX, "shriek");
			return;
		}
		
		long now = System.currentTimeMillis();
		
		long millisSinceLastShriekWaitMessage = now - this.lastShriekWaitMessageMillis;
		if (millisSinceLastShriekWaitMessage < conf.shriekWaitMessageCooldownMillis)
		{
			return;
		}
		
		long millisSinceLastShriek = now - this.lastShriekMillis;
		long millisToWait = conf.shriekCooldownMillis - millisSinceLastShriek;
		
		if (millisToWait > 0)
		{
			long secondsToWait = (long) Math.ceil(millisToWait / 1000D);
			this.msg(Lang.shriekWait, secondsToWait);
			this.lastShriekWaitMessageMillis = now;
			return;
		}
		
		this.fxShriekRun();
		this.fxSmokeBurstRun();
		this.lastShriekMillis = now;
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
		
		Player player = this.getPlayer();
		if (player == null) return;
		Conf conf = Conf.get(player);
		
		this.permA = player.addAttachment(P.p);
		
		if (this.vampire())
		{
			for (Entry<String, Boolean> entry : conf.updatePermsVampire.entrySet())
			{
				this.permA.setPermission(entry.getKey(), entry.getValue());
			}
		}
		else
		{
			for (Entry<String, Boolean> entry : conf.updatePermsHuman.entrySet())
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
		Conf conf = Conf.get(player);
		if (player == null) return;
		SpoutPlayer splayer = SpoutManager.getPlayer(player);
		boolean spoutuser = splayer.isSpoutCraftEnabled();
		
		if (spoutuser && this.vampire())
		{
			P.p.noCheatExemptedPlayerNames.add(player.getName());
		}
		else
		{
			P.p.noCheatExemptedPlayerNames.remove(player.getName());
		}
		
		Double multGravity = null;
		Double multSwimming = null;
		Double multWalking = null;
		Double multJumping = null;
		Double multAirSpeed = null;
		String noSpoutWarn = null;		
		
		if (this.vampire() && this.bloodlust())
		{
			multGravity = conf.multGravityBloodlust;
			multSwimming = conf.multSwimmingBloodlust;
			multWalking = conf.multWalkingBloodlust;
			multJumping = conf.multJumpingBloodlust;
			multAirSpeed = conf.multAirSpeedBloodlust;
			noSpoutWarn = Lang.noSpoutWarnBloodlust;
		}
		else if (this.vampire())
		{
			multGravity = conf.multGravityVamp;
			multSwimming = conf.multSwimmingVamp;
			multWalking = conf.multWalkingVamp;
			multJumping = conf.multJumpingVamp;
			multAirSpeed = conf.multAirSpeedVamp;
			noSpoutWarn = Lang.noSpoutWarnVamp;
		}
		else
		{
			multGravity = conf.multGravityHuman;
			multSwimming = conf.multSwimmingHuman;
			multWalking = conf.multWalkingHuman;
			multJumping = conf.multJumpingHuman;
			multAirSpeed = conf.multAirSpeedHuman;
			noSpoutWarn = Lang.noSpoutWarnHuman;
		}

		if ( ! spoutuser && noSpoutWarn != null) this.msg(Txt.wrap(noSpoutWarn));
		
		if (multGravity != null) splayer.setGravityMultiplier(multGravity);
		if (multSwimming != null) splayer.setSwimmingMultiplier(multSwimming);
		if (multWalking != null) splayer.setWalkingMultiplier(multWalking);
		if (multJumping != null) splayer.setJumpingMultiplier(multJumping);
		if (multAirSpeed != null) splayer.setAirSpeedMultiplier(multAirSpeed);
	}
	
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
		Conf conf = Conf.get(player);
		
		boolean survival = player.getGameMode() == GameMode.SURVIVAL;
		if (survival && this.vampire() && ! player.isDead())
		{
			this.rad = conf.baseRad + SunUtil.calcPlayerIrradiation(player);
			Double tempDelta = conf.tempPerRadAndTick * this.rad * ticks;
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
		
		Player player = this.getPlayer();
		Conf conf = Conf.get(player);
		
		boolean survival = player.getGameMode() == GameMode.SURVIVAL;
		if ( ! survival) return;
		
		int indexOld = this.infectionGetMessageIndex();
		this.infectionAdd(ticks * conf.infectionPerTick);
		int indexNew = this.infectionGetMessageIndex();
		
		if (this.vampire()) return;
		if (indexOld == indexNew) return;
		
		if (conf.infectionProgressDamage != 0) player.damage(conf.infectionProgressDamage);
		if (conf.infectionProgressNauseaTicks > 0) FxUtil.ensure(PotionEffectType.CONFUSION, player, conf.infectionProgressNauseaTicks);
		
		this.msg(Lang.infectionFeeling.get(indexNew));
		this.msg(Lang.infectionHint.get(MCore.random.nextInt(Lang.infectionHint.size())));
		this.changed();
	}
	public int infectionGetMessageIndex()
	{
		return (int)((Lang.infectionFeeling.size()+1) * this.infection() / 1D) - 1;
	}
	
	public void tickRegen(long ticks)
	{
		if ( ! this.vampire()) return;
		Player player = this.getPlayer();
		Conf conf = Conf.get(player);
		boolean survival = player.getGameMode() == GameMode.SURVIVAL;
		if ( ! survival) return;
		if (player.isDead()) return;
		if (player.getHealth() >= 20) return;
		if (this.food().get() < conf.regenMinFood) return;
		
		long millisSinceLastDamage = System.currentTimeMillis() - this.lastDamageMillis;
		if (millisSinceLastDamage < conf.regenDelayMillis) return;
		
		double foodDiff = this.food().add(-conf.regenFoodPerTick * ticks);
		this.health().add(-foodDiff * conf.regenHealthPerFood);
	}
	
	public void tickBloodlust(long ticks)
	{
		if ( ! this.vampire()) return;
		if ( ! this.bloodlust()) return;
		Player player = this.getPlayer();
		Conf conf = Conf.get(player);
		boolean survival = player.getGameMode() == GameMode.SURVIVAL;
		if ( ! survival) return;
		if (player.isDead()) return;
		
		this.food().add(ticks * conf.bloodlustFoodPerTick);
		if (this.food().get() < conf.bloodlustMinFood) this.bloodlust(false);
	}
	
	public void tickEffects(long ticks)
	{
		Player player = this.getPlayer();
		Conf conf = Conf.get(player);
		if ( ! player.isOnline()) return;
		if (player.isDead()) return;
		
		boolean survival = player.getGameMode() == GameMode.SURVIVAL;
		if ( ! survival) return;

		// FX: Smoke
		if (this.fxSmokeTicks > 0)
		{
			this.fxSmokeTicks -= ticks;
			double dcount = conf.fxSmokePerTick * ticks;
			long lcount = MUtil.probabilityRound(dcount);
			for (long i = lcount; i > 0; i--) FxUtil.smoke(player);
		}
		
		// FX: Ender
		if (this.fxEnderTicks > 0)
		{
			this.fxEnderTicks -= ticks;
			double dcount = conf.fxEnderPerTick * ticks;
			long lcount = MUtil.probabilityRound(dcount);
			for (long i = lcount; i > 0; i--) FxUtil.ender(player, conf.fxEnderRandomMaxLen);
		}
		
		// Vampire sun reactions
		if (survival && this.vampire())
		{
			// Buffs
			if (this.temp() > conf.sunNauseaTemp)    FxUtil.ensure(PotionEffectType.CONFUSION, player, conf.sunNauseaTicks);
			if (this.temp() > conf.sunWeaknessTemp)  FxUtil.ensure(PotionEffectType.WEAKNESS, player, conf.sunWeaknessTicks);
			if (this.temp() > conf.sunSlowTemp)      FxUtil.ensure(PotionEffectType.SLOW, player, conf.sunSlowTicks);
			if (this.temp() > conf.sunBlindnessTemp) FxUtil.ensure(PotionEffectType.BLINDNESS, player, conf.sunBlindnessTicks);
			if (this.temp() > conf.sunBurnTemp)      FxUtil.ensureBurn(player, conf.sunBurnTicks);
			
			// Fx
			double dsmokes = conf.sunSmokesPerTempAndTick * this.temp * ticks;
			long lsmokes = MUtil.probabilityRound(dsmokes);
			for (long i = lsmokes; i > 0; i--) FxUtil.smoke(player);
			
			double dflames = conf.sunFlamesPerTempAndTick * this.temp * ticks;
			long lflames = MUtil.probabilityRound(dflames);
			for (long i = lflames; i > 0; i--) FxUtil.flame(player);
		}
	}
	
	// -------------------------------------------- //
	// TRADE
	// -------------------------------------------- //
	public void tradeAccept()
	{
		Player me = this.getPlayer();
		if (me == null) return;
		Conf conf = Conf.get(me);
		
		VPlayer vyou = this.tradeOfferedFrom;
		
		// Any offer available?
		if (vyou == null || System.currentTimeMillis() - this.tradeOfferedAtMillis > conf.tradeOfferToleranceMillis)
		{
			this.msg(Lang.tradeAcceptNone);
			return;
		}

		// Standing close enough?
		if ( ! this.withinDistanceOf(vyou, conf.tradeOfferMaxDistance))
		{
			this.msg(Lang.tradeNotClose, vyou.getId());
			return;
		}
		
		Player you = vyou.getPlayer();
		double amount = this.tradeOfferedAmount;
		
		// Enough blood?
		if (this.tradeOfferedAmount > vyou.food().get())
		{
			vyou.msg(Lang.tradeLackingOut);
			this.msg(Lang.tradeLackingIn, you.getDisplayName());
			return;
		}
		
		// Transfer food level
		vyou.food().add(-amount);
		this.food().add(amount);
		
		// Risk infection
		if (MCore.random.nextDouble()*20 < amount)
		{
			this.infectionAdd(0.05D, InfectionReason.TRADE, vyou);
		}
		
		// Trader Messages
		vyou.msg(Lang.tradeTransferOut, me.getDisplayName(), amount);
		this.msg(Lang.tradeTransferIn, amount, you.getDisplayName());
		
		// Who noticed?
		Location tradeLocation = me.getLocation();
		World tradeWorld = tradeLocation.getWorld();
		Location l1 = me.getEyeLocation();
		Location l2 = you.getEyeLocation();
		for (Player player : tradeWorld.getPlayers())
		{
			if (player.getLocation().distance(tradeLocation) > conf.tradeVisualDistance) continue;
			player.playEffect(l1, Effect.POTION_BREAK, 5);
			player.playEffect(l2, Effect.POTION_BREAK, 5);
			if (player.equals(me)) continue;
			if (player.equals(you)) continue;
			player.sendMessage(Txt.parse(Lang.tradeSeen, me.getDisplayName(), you.getDisplayName()));
		}
		
		// Reset trade memory
		this.tradeOfferedFrom = null;
		this.tradeOfferedAtMillis = 0;
		this.tradeOfferedAmount = 0;
	}
	
	public void tradeOffer(VPlayer vyou, double amount)
	{
		Player you = vyou.getPlayer();
		Player me = this.getPlayer();
		if (me == null) return;
		Conf conf = Conf.get(me);
		
		if ( ! this.withinDistanceOf(vyou, conf.tradeOfferMaxDistance))
		{
			this.msg(Lang.tradeNotClose, vyou.getId());
			return;
		}
		
		if (this == vyou)
		{
			this.msg(Lang.tradeSelf);
			FxUtil.ensure(PotionEffectType.CONFUSION, me, 12*20);
			return;
		}
		
		vyou.tradeOfferedFrom = this;
		vyou.tradeOfferedAtMillis = System.currentTimeMillis();
		vyou.tradeOfferedAmount = amount;
		
		this.msg(Lang.tradeOfferOut, amount, you.getDisplayName());
		vyou.msg(Lang.tradeOfferIn, me.getDisplayName(), amount);
		List<MCommand> cmdc = new ArrayList<MCommand>();
		cmdc.add(P.p.cmdBase);
		vyou.msg(Lang.tradeAcceptHelp, P.p.cmdBase.cmdAccept.getUseageTemplate(cmdc, false));
	}
	
	public boolean withinDistanceOf(VPlayer vyou, double maxDistance)
	{
		Player me = this.getPlayer();
		Player you = vyou.getPlayer();
		if (you == null) return false;
		if (me == null) return false;
		if (me.isDead()) return false;
		if (you.isDead()) return false;
		Location l1 = me.getLocation();
		Location l2 = you.getLocation();
		if ( ! l1.getWorld().equals(l2.getWorld())) return false;
		if (l1.distance(l2) > maxDistance) return false;
		return true;
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
		Player player = this.getPlayer();
		if (player == null) return;
		Conf conf = Conf.get(player);
		
		if ( ! this.truceIsBroken())
		{
			this.msg(Lang.truceBroken);
		}
		this.truceBreakTicksLeftSet(conf.truceBreakTicks);
	}
	
	public void truceRestore()
	{
		this.msg(Lang.truceRestored);
		this.truceBreakTicksLeftSet(0);
		
		Player me = this.getPlayer();
		Conf conf = Conf.get(me);
		
		// Untarget the player.
		for (LivingEntity entity : this.getPlayer().getWorld().getLivingEntities())
		{
			if ( ! (entity instanceof Creature))
			{
				continue;
			}
			
			if ( ! conf.truceEntityTypes.contains(entity.getType()))
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
	// COMBAT
	// -------------------------------------------- //
	
	public double combatDamageFactor()
	{
		Player player = this.getPlayer();
		Conf conf = Conf.get(player);
		
		if (this.bloodlust()) return conf.combatDamageFactorWithBloodlust;
		return conf.combatDamageFactorWithoutBloodlust;
	}
	
	public double combatInfectRisk()
	{
		Player player = this.getPlayer();
		Conf conf = Conf.get(player);
		
		if (this.human()) return 0D;
		if (this.intend()) return conf.infectionRiskAtCloseCombatWithIntent;
		return conf.infectionRiskAtCloseCombatWithoutIntent;
	}
}
