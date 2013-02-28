package com.massivecraft.vampire;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.store.SenderEntity;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.PermUtil;
import com.massivecraft.mcore.util.Txt;
import com.massivecraft.vampire.accumulator.VPlayerFoodAccumulator;
import com.massivecraft.vampire.accumulator.VPlayerHealthAccumulator;
import com.massivecraft.vampire.event.VampirePlayerInfectionChangeEvent;
import com.massivecraft.vampire.event.VampirePlayerVampireChangeEvent;
import com.massivecraft.vampire.util.FxUtil;
import com.massivecraft.vampire.util.SunUtil;

/**
 * The VPlayer is a "skin" for a normal player.
 * Through this skin we can reach the player plus extra plugin specific data and functionality.
 */
public class VPlayer extends SenderEntity<VPlayer>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static VPlayer get(Object worldNameExtractable)
	{
		return VPlayerColls.i.get2(worldNameExtractable);
	}
	
	// -------------------------------------------- //
	// LOAD
	// -------------------------------------------- //
	
	@Override
	public VPlayer load(VPlayer that)
	{
		this.vampire = that.vampire;
		this.infection = that.infection;
		this.reason = that.reason;
		this.makerId = that.makerId;
		this.intending = that.intending;
		this.bloodlusting = that.bloodlusting;
		this.usingNightVision = that.usingNightVision;
		return this;
	}
	
	// -------------------------------------------- //
	// PERSISTENT FIELDS
	// -------------------------------------------- //
	
	// Is the player a vampire?
	protected boolean vampire = false;
	public boolean isVampire() { return this.vampire; }
	public boolean isHuman() { return ! this.isVampire(); } // Shortcut
	public void setVampire(boolean val)
	{
		this.setInfection(0);
		
		if (this.vampire == val) return;
		
		VampirePlayerVampireChangeEvent event = new VampirePlayerVampireChangeEvent(val, this);
		event.run();
		if (event.isCancelled()) return;
		
		this.vampire = val;
		this.changed();
		if (this.vampire)
		{
			this.msg(Lang.vampireTrue);
			this.runFxShriek();
			this.runFxSmokeBurst();
			this.runFxSmoke();
			
			Player player = this.getPlayer();
			if (player != null)
			{
				Conf conf = Conf.get(player);
				conf.effectConfHuman.removePotionEffects(player);
			}
		}
		else
		{
			this.msg(Lang.vampireFalse);
			this.runFxEnder();
			this.setMaker(null);
			this.setReason(null);
			this.setBloodlusting(false);
			this.setIntending(false);
			this.setUsingNightVision(false);
			
			Player player = this.getPlayer();
			if (player != null)
			{
				Conf conf = Conf.get(player);
				conf.effectConfVampire.removePotionEffects(player);
			}
		}
		
		this.update();
	}
	
	// 0 means no infection. If infection reaches 1 the player will turn to vampire.
	protected double infection = 0;
	public double getInfection() { return this.infection; }
	public boolean isInfected() { return this.infection > 0D; }
	public void setInfection(double val)
	{
		if (this.infection == val) return;
		
		// Call event
		VampirePlayerInfectionChangeEvent event = new VampirePlayerInfectionChangeEvent(val, this);
		event.run();
		if (event.isCancelled()) return;
		val = event.getInfection();
		
		if (val >= 1D)
		{
			this.setVampire(true);
		}
		else if (val <= 0D)
		{
			if (this.infection > 0D && ! this.isVampire())
			{
				this.msg(Lang.infectionCured);
			}
			this.infection = 0D;
			
			Player player = this.getPlayer();
			if (player != null)
			{
				Conf conf = Conf.get(player);
				conf.effectConfInfected.removePotionEffects(player);
			}
		}
		else
		{
			this.infection = val;
		}
		
		this.updatePotionEffects();
	}
	public void addInfection(double val)
	{
		this.setInfection(this.getInfection()+val);
	}
	public void addInfection(double val, InfectionReason reason, VPlayer maker)
	{
		if (vampire) return;
		this.setReason(reason);
		this.setMakerId(maker == null ? null : maker.getId());
		P.p.log(Txt.parse(this.getReasonDesc(false)));
		if (reason.isNoticeable()) this.msg(this.getReasonDesc(true));
		this.addInfection(val);
	}
	
	// healthy and unhealthy - Fake field shortcuts
	public boolean isHealthy() { return ! this.isVampire() && ! this.isInfected(); }
	public boolean isUnhealthy() { return ! this.isHealthy(); }
	
	// How come this player is infected?
	protected InfectionReason reason;
	public InfectionReason getReason() { return reason == null ? InfectionReason.UNKNOWN : reason; }
	public void setReason(InfectionReason reason) {this.reason = reason; }
	public String getReasonDesc(boolean self) { return this.getReason().getDesc(this, self); }
	
	// Who made this vampire?
	protected String makerId;
	public String getMakerId() { return this.makerId; }
	public void setMakerId(String makerId) { this.makerId = makerId; }
	public VPlayer getMaker() { return this.getColl().get(this.makerId); }
	public void setMaker(VPlayer val) { this.setMakerId(val == null ? null : val.getId()); }
	
	// FIELD: intending - Vampires may choose their combat style. Do they intend to infect others in combat or do they not?
	protected boolean intending = false;
	public boolean isIntending() { return this.intending; }
	public void setIntending(boolean val)
	{
		this.intending = val;
		this.msg(this.intendMsg());
	}
	public String intendMsg() { return Lang.boolIsY("Infect intent", this.isIntending())+ " " + Lang.quotaIsPercent("Combat infect risk", this.combatInfectRisk()); }
	
	// FIELD: bloodlust - Is bloodlust activated?
	protected boolean bloodlusting = false;
	public boolean isBloodlusting() { return this.bloodlusting; }
	public void setBloodlusting(boolean val)
	{
		if (this.bloodlusting == val)
		{
			// No real change - just view the info.
			this.msg(Lang.boolIsY("Bloodlust", val));
			return;
		}
		
		Player me = this.getPlayer();
		if (me != null)
		{
			if (val)
			{
				// There are a few rules to when you can turn it on:
				if ( ! this.isVampire())
				{
					msg(Lang.onlyVampsCanX, "use bloodlust");
					return;
				}
				
				if (this.getFood().get() < Conf.get(me).bloodlustMinFood)
				{
					msg("<b>Your food is too low for bloodlust.");
					return;
				}
				
				if (this.isGameMode(GameMode.CREATIVE, true))
				{
					msg("<b>You can't use bloodlust while in Creative Mode."); // or offline :P but offline players wont see the message
					return;
				}
			}
			else
			{
				Conf conf = Conf.get(me);
				conf.effectConfBloodlust.removePotionEffects(me);
			}
		}
		
		this.bloodlusting = val;
		this.msg(this.bloodlustMsg());
		this.update();
	}
	public String bloodlustMsg() { return Lang.boolIsY("Bloodlust", this.isBloodlusting()) + " " + Lang.quotaIsPercent("combat damage", this.combatDamageFactor()); }
	
	// FIELD: usingNightVision - Vampires can use nightvision anytime they want.
	protected boolean usingNightVision = false;
	public boolean isUsingNightVision() { return this.usingNightVision; }
	public void setUsingNightVision(boolean val)
	{
		// If an actual change is being made ...
		if (this.usingNightVision == val) return;
		
		// ... do change stuff ...
		this.usingNightVision = val;
		
		// ... mstore mark (not required but is good practice) ...
		this.changed();
		
		// ... remove the nightvision potion effects ...
		Player me = this.getPlayer();
		if (me != null)
		{
			Conf conf = Conf.get(me);
			conf.effectConfNightvision.removePotionEffects(me);
		}
		
		// ... trigger a potion effect update ...
		this.updatePotionEffects();
		
		this.msg(this.usingNightVisionMsg());
	}
	public String usingNightVisionMsg() { return Lang.boolIsY("Nightvision", this.isUsingNightVision()); }
	
	// -------------------------------------------- //
	// TRANSIENT FIELDS
	// -------------------------------------------- //
	
	// FIELD: rad - The irradiation for the player.
	protected transient double rad = 0;
	public double getRad() { return this.rad; }
	public void setRad(double rad) { this.rad = rad; }
	
	// FIELD: temp - The temperature of the player. a double between 0 and 1.
	protected transient double temp = 0;
	public double getTemp() { return this.temp; }
	public void setTemp(double val) { this.temp = MUtil.limitNumber(val, 0D, 1D); }
	public void addTemp(double val) { this.setTemp(this.getTemp()+val); }
	
	// FIELD: food - the food accumulator
	protected transient VPlayerFoodAccumulator food = new VPlayerFoodAccumulator(this);
	public VPlayerFoodAccumulator getFood() { return this.food; }
	
	// FIELD: health - the health accumulator
	protected transient VPlayerHealthAccumulator health = new VPlayerHealthAccumulator(this);
	public VPlayerHealthAccumulator getHealth() { return this.health; }
	
	// FIELD: lastDamageMillis - for the regen
	protected transient long lastDamageMillis = 0;
	public long getLastDamageMillis() { return this.lastDamageMillis; }
	public void setLastDamageMillis(long lastDamageMillis) { this.lastDamageMillis = lastDamageMillis; }
	
	// FIELD: lastShriekMillis - for the shriek ability
	protected transient long lastShriekMillis = 0;
	public long getLastShriekMillis() { return this.lastShriekMillis; }
	public void setLastShriekMillis(long lastShriekMillis) { this.lastShriekMillis = lastShriekMillis; }
	
	// FIELD: lastShriekMessageMillis - Anti pwnage
	protected transient long lastShriekWaitMessageMillis = 0;
	public long getLastShriekWaitMessageMillis() { return this.lastShriekWaitMessageMillis; }
	public void setLastShriekWaitMessageMillis(long lastShriekWaitMessageMillis) { this.lastShriekWaitMessageMillis = lastShriekWaitMessageMillis; }
	
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
	public int getFxSmokeTicks() { return this.fxSmokeTicks; }
	public void setFxSmokeTicks(int fxSmokeTicks) { this.fxSmokeTicks = fxSmokeTicks; }
	public void runFxSmoke() { this.fxSmokeTicks = 20 * 20; }
	
	// FX: Ender
	protected transient int fxEnderTicks = 0;
	public int getFxEnderTicks() { return this.fxEnderTicks; }
	public void getFxEnderTicks(int fxEnderTicks) { this.fxEnderTicks = fxEnderTicks; }
	public void runFxEnder() { this.fxEnderTicks = 10 * 20; }
	
	// FX: Shriek
	public void runFxShriek()
	{
		Player me = this.getPlayer();
		if (me == null) return;
		Location location = me.getLocation();
		World world = location.getWorld();
		world.playEffect(location, Effect.GHAST_SHRIEK, 0);
	}
	
	// FX: SmokeBurst
	public void runFxSmokeBurst()
	{
		Player me = this.getPlayer();
		if (me == null) return;
		double dcount = Conf.get(me).fxSmokeBurstCount;
		long lcount = MUtil.probabilityRound(dcount);
		for (long i = lcount; i > 0; i--) FxUtil.smoke(me);
	}
	
	// FX: EnderBurst
	public void runFxEnderBurst()
	{
		Player me = this.getPlayer();
		if (me == null) return;
		double dcount = Conf.get(me).fxEnderBurstCount;
		long lcount = MUtil.probabilityRound(dcount);
		for (long i = lcount; i > 0; i--) FxUtil.ender(me, 0);
	}
	
	// FX: FlameBurst
	public void runFxFlameBurst()
	{
		Player me = this.getPlayer();
		if (me == null) return;
		double dcount = Conf.get(me).fxFlameBurstCount;
		long lcount = MUtil.probabilityRound(dcount);
		for (long i = lcount; i > 0; i--) FxUtil.flame(me);
	}
	
	// -------------------------------------------- //
	// SHRIEK
	// -------------------------------------------- //
	
	public void shriek()
	{
		// You must be online to shriek
		Player me = this.getPlayer();
		if (me == null) return;
		Conf conf = Conf.get(me);
		
		// You must be a vampire to shriek
		if ( ! this.isVampire())
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
		
		this.runFxShriek();
		this.runFxSmokeBurst();
		this.lastShriekMillis = now;
	}
	
	// -------------------------------------------- //
	// UPDATE 
	// -------------------------------------------- //
	
	public void update()
	{
		this.updatePermissions();
		this.updatePotionEffects();
	}
	
	public String getPermissionName()
	{
		return "vampire.player."+this.getId();
	}
	
	// This method will always return a permission.
	// It may however be an empty one (no children) if the player is offline.
	public Permission getPermission(boolean update)
	{
		Map<String, Boolean> targetChildren = null;
		
		if (update)
		{
			// This one will return null if the player is offline
			targetChildren = this.getPermissionTargetChildren();
		}
		
		if (targetChildren == null)
		{
			return PermUtil.get(true, update, this.getPermissionName(), PermissionDefault.FALSE);
		}
		else
		{
			return PermUtil.get(true, update, this.getPermissionName(), PermissionDefault.FALSE, targetChildren);
		}
	}
	
	public Map<String, Boolean> getPermissionTargetChildren()
	{
		Player player = this.getPlayer();
		if (player == null) return null;
		Conf conf = Conf.get(player);
		
		Map<String, Boolean> ret;
		if (this.isVampire())
		{
			ret = conf.updatePermsVampire;
		}
		else
		{
			ret = conf.updatePermsHuman;
		}
		
		return ret;
	}
	
	public void updatePermissions()
	{
		Permission permission = this.getPermission(true);
		
		Player player = this.getPlayer();
		if (player == null) return;
		
		PermUtil.ensureHas(player, permission);
	}
	
	public void updatePotionEffects()
	{
		final int okDuration = 300;
		final int targetDuration = okDuration*2;
		
		// Find the player and their conf
		Player player = this.getPlayer();
		if (player == null) return;
		if (player.isDead()) return;
		Conf conf = Conf.get(player);
		
		// Add effects based their		
		if (this.isHuman())
		{
			System.out.println("adding human to "+player.getName());
			conf.effectConfHuman.addPotionEffects(player, targetDuration, okDuration);
		}
		
		if (this.isInfected())
		{
			System.out.println("adding infected to "+player.getName());
			conf.effectConfInfected.addPotionEffects(player, targetDuration, okDuration);
		}
		
		if (this.isVampire())
		{
			System.out.println("adding vampire to "+player.getName());
			conf.effectConfVampire.addPotionEffects(player, targetDuration, okDuration);
		}
		
		if (this.isVampire() && conf.nightvisionCanBeUsed && this.isUsingNightVision())
		{
			System.out.println("adding nightvision to "+player.getName());
			conf.effectConfNightvision.addPotionEffects(player, targetDuration, okDuration);
		}
		
		if (this.isVampire() && this.isBloodlusting())
		{
			System.out.println("adding bloodlust to "+player.getName());
			conf.effectConfBloodlust.addPotionEffects(player, targetDuration, okDuration);
		}
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
		this.tickPotionEffects(ticks);
		this.tickEffects(ticks);
		this.tickTruce(ticks);
	}
	
	public void tickRadTemp(long ticks)
	{
		// Update rad and temp
		Player me = this.getPlayer();
		if (me == null) return;
		Conf conf = Conf.get(me);
		
		boolean survival = me.getGameMode() == GameMode.SURVIVAL;
		if (survival && this.isVampire() && ! me.isDead())
		{
			this.rad = conf.baseRad + SunUtil.calcPlayerIrradiation(me);
			Double tempDelta = conf.tempPerRadAndTick * this.rad * ticks;
			this.addTemp(tempDelta);
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
		if ( ! this.isInfected()) return;
		
		Player me = this.getPlayer();
		if (me == null) return;
		Conf conf = Conf.get(me);
		
		boolean survival = me.getGameMode() == GameMode.SURVIVAL;
		if ( ! survival) return;
		
		int indexOld = this.infectionGetMessageIndex();
		this.addInfection(ticks * conf.infectionPerTick);
		int indexNew = this.infectionGetMessageIndex();
		
		if (this.isVampire()) return;
		if (indexOld == indexNew) return;
		
		if (conf.infectionProgressDamage != 0) me.damage(conf.infectionProgressDamage);
		if (conf.infectionProgressNauseaTicks > 0) FxUtil.ensure(PotionEffectType.CONFUSION, me, conf.infectionProgressNauseaTicks);
		
		this.msg(Lang.infectionFeeling.get(indexNew));
		this.msg(Lang.infectionHint.get(MCore.random.nextInt(Lang.infectionHint.size())));
		this.changed();
	}
	public int infectionGetMessageIndex()
	{
		return (int)((Lang.infectionFeeling.size()+1) * this.getInfection() / 1D) - 1;
	}
	
	public void tickRegen(long ticks)
	{
		if ( ! this.isVampire()) return;
		Player me = this.getPlayer();
		if (me == null) return;
		Conf conf = Conf.get(me);
		boolean survival = me.getGameMode() == GameMode.SURVIVAL;
		if ( ! survival) return;
		if (me.isDead()) return;
		if (me.getHealth() >= 20) return;
		if (this.getFood().get() < conf.regenMinFood) return;
		
		long millisSinceLastDamage = System.currentTimeMillis() - this.lastDamageMillis;
		if (millisSinceLastDamage < conf.regenDelayMillis) return;
		
		double foodDiff = this.getFood().add(-conf.regenFoodPerTick * ticks);
		this.getHealth().add(-foodDiff * conf.regenHealthPerFood);
	}
	
	public void tickBloodlust(long ticks)
	{
		if ( ! this.isVampire()) return;
		if ( ! this.isBloodlusting()) return;
		Player me = this.getPlayer();
		if (me == null) return;
		Conf conf = Conf.get(me);
		boolean survival = me.getGameMode() == GameMode.SURVIVAL;
		if ( ! survival) return;
		if (me.isDead()) return;
		
		this.getFood().add(ticks * conf.bloodlustFoodPerTick);
		if (this.getFood().get() < conf.bloodlustMinFood) this.setBloodlusting(false);
	}
	
	public void tickPotionEffects(long ticks)
	{
		// TODO: Will update to often!?
		this.updatePotionEffects();
	}
	
	public void tickEffects(long ticks)
	{
		Player me = this.getPlayer();
		if (me == null) return;
		if (me.isDead()) return;
		Conf conf = Conf.get(me);
		
		boolean survival = me.getGameMode() == GameMode.SURVIVAL;
		if ( ! survival) return;

		// FX: Smoke
		if (this.fxSmokeTicks > 0)
		{
			this.fxSmokeTicks -= ticks;
			double dcount = conf.fxSmokePerTick * ticks;
			long lcount = MUtil.probabilityRound(dcount);
			for (long i = lcount; i > 0; i--) FxUtil.smoke(me);
		}
		
		// FX: Ender
		if (this.fxEnderTicks > 0)
		{
			this.fxEnderTicks -= ticks;
			double dcount = conf.fxEnderPerTick * ticks;
			long lcount = MUtil.probabilityRound(dcount);
			for (long i = lcount; i > 0; i--) FxUtil.ender(me, conf.fxEnderRandomMaxLen);
		}
		
		// Vampire sun reactions
		if (survival && this.isVampire())
		{
			// Buffs
			if (this.getTemp() > conf.sunNauseaTemp)    FxUtil.ensure(PotionEffectType.CONFUSION, me, conf.sunNauseaTicks);
			if (this.getTemp() > conf.sunWeaknessTemp)  FxUtil.ensure(PotionEffectType.WEAKNESS, me, conf.sunWeaknessTicks);
			if (this.getTemp() > conf.sunSlowTemp)      FxUtil.ensure(PotionEffectType.SLOW, me, conf.sunSlowTicks);
			if (this.getTemp() > conf.sunBlindnessTemp) FxUtil.ensure(PotionEffectType.BLINDNESS, me, conf.sunBlindnessTicks);
			if (this.getTemp() > conf.sunBurnTemp)      FxUtil.ensureBurn(me, conf.sunBurnTicks);
			
			// Fx
			double dsmokes = conf.sunSmokesPerTempAndTick * this.temp * ticks;
			long lsmokes = MUtil.probabilityRound(dsmokes);
			for (long i = lsmokes; i > 0; i--) FxUtil.smoke(me);
			
			double dflames = conf.sunFlamesPerTempAndTick * this.temp * ticks;
			long lflames = MUtil.probabilityRound(dflames);
			for (long i = lflames; i > 0; i--) FxUtil.flame(me);
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
			this.msg(Lang.tradeNotClose, vyou.getDisplayName());
			return;
		}
		
		Player you = vyou.getPlayer();
		double amount = this.tradeOfferedAmount;
		
		// Enough blood?
		if (this.tradeOfferedAmount > vyou.getFood().get())
		{
			vyou.msg(Lang.tradeLackingOut);
			this.msg(Lang.tradeLackingIn, you.getDisplayName());
			return;
		}
		
		// Transfer food level
		vyou.getFood().add(-amount);
		this.getFood().add(amount);
		
		// Risk infection/boost infection
		if(!this.isVampire()){
			if(this.isInfected()){
					this.addInfection(0.01D);
			}else if (MCore.random.nextDouble()*20 < amount)
			{
					this.addInfection(0.05D, InfectionReason.TRADE, vyou);
			}
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
		if (you == null) return;
		Player me = this.getPlayer();
		if (me == null) return;
		Conf conf = Conf.get(me);
		
		if ( ! this.withinDistanceOf(vyou, conf.tradeOfferMaxDistance))
		{
			this.msg(Lang.tradeNotClose, vyou.getDisplayName());
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
		if ( ! this.isVampire()) return;
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
		if (me == null) return;
		Conf conf = Conf.get(me);
		
		// Untarget the player.
		for (LivingEntity entity : me.getWorld().getLivingEntities())
		{
			if ( ! conf.truceEntityTypes.contains(entity.getType())) continue;
			
			if ( ! (entity instanceof Creature)) continue;
			Creature creature = (Creature)entity;
			
			Entity target = creature.getTarget();
			if ( ! me.equals(target)) continue;
			
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
		Player me = this.getPlayer();
		if (me == null) return 0D;
		Conf conf = Conf.get(me);
		
		if (this.isBloodlusting()) return conf.combatDamageFactorWithBloodlust;
		return conf.combatDamageFactorWithoutBloodlust;
	}
	
	public double combatInfectRisk()
	{
		Player me = this.getPlayer();
		if (me == null) return 0D;
		Conf conf = Conf.get(me);
		
		if (this.isHuman()) return 0D;
		if (this.isIntending()) return conf.infectionRiskAtCloseCombatWithIntent;
		return conf.infectionRiskAtCloseCombatWithoutIntent;
	}
}
