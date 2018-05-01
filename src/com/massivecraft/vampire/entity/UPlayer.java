package com.massivecraft.vampire.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.PermissionUtil;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.vampire.InfectionReason;
import com.massivecraft.vampire.Vampire;
import com.massivecraft.vampire.accumulator.UPlayerFoodAccumulator;
import com.massivecraft.vampire.cmd.CmdVampire;
import com.massivecraft.vampire.event.EventVampirePlayerInfectionChange;
import com.massivecraft.vampire.event.EventVampirePlayerVampireChange;
import com.massivecraft.vampire.util.FxUtil;
import com.massivecraft.vampire.util.SunUtil;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class UPlayer extends SenderEntity<UPlayer>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static UPlayer get(Object oid)
	{
		return UPlayerColls.get().get2(oid);
	}
	
	// -------------------------------------------- //
	// LOAD
	// -------------------------------------------- //
	
	@Override
	public UPlayer load(UPlayer that)
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
		
		EventVampirePlayerVampireChange event = new EventVampirePlayerVampireChange(val, this);
		event.run();
		if (event.isCancelled()) return;
		
		this.changed(this.vampire, val);
		this.vampire = val;
		if (this.vampire)
		{
			this.msg(MLang.get().vampireTrue);
			this.runFxShriek();
			this.runFxSmokeBurst();
			this.runFxSmoke();
			
			Player player = this.getPlayer();
			if (player != null)
			{
				UConf uconf = UConf.get(player);
				uconf.effectConfHuman.removePotionEffects(player);
			}
		}
		else
		{
			this.msg(MLang.get().vampireFalse);
			this.runFxEnder();
			this.setMaker(null);
			this.setReason(null);
			this.setBloodlusting(false);
			this.setIntending(false);
			this.setUsingNightVision(false);
			
			Player player = this.getPlayer();
			if (player != null)
			{
				UConf uconf = UConf.get(player);
				uconf.effectConfVampire.removePotionEffects(player);
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
		EventVampirePlayerInfectionChange event = new EventVampirePlayerInfectionChange(val, this);
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
				this.msg(MLang.get().infectionCured);
			}
			this.changed(this.infection, 0D);
			this.infection = 0D;
			
			Player player = this.getPlayer();
			if (player != null)
			{
				UConf uconf = UConf.get(player);
				uconf.effectConfInfected.removePotionEffects(player);
			}
		}
		else
		{
			this.changed(this.infection, val);
			this.infection = val;
		}
		this.updatePotionEffects();
	}
	public void addInfection(double val)
	{
		this.setInfection(this.getInfection()+val);
	}
	public void addInfection(double val, InfectionReason reason, UPlayer maker)
	{
		if (vampire) return;
		this.setReason(reason);
		this.setMakerId(maker == null ? null : maker.getId());
		Vampire.get().log(Txt.parse(this.getReasonDesc(false)));
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
	public UPlayer getMaker() { return this.getColl().get(this.makerId); }
	public void setMaker(UPlayer val) { this.setMakerId(val == null ? null : val.getId()); }
	
	// FIELD: intending - Vampires may choose their combat style. Do they intend to infect others in combat or do they not?
	protected boolean intending = false;
	public boolean isIntending() { return this.intending; }
	public void setIntending(boolean val)
	{
		this.intending = val;
		this.msg(this.intendMsg());
	}
	public String intendMsg() { return MLang.get().boolIsY("Infect intent", this.isIntending())+ " " + MLang.get().quotaIsPercent("Combat infect risk", this.combatInfectRisk()); }
	
	// FIELD: bloodlust - Is bloodlust activated?
	protected boolean bloodlusting = false;
	public boolean isBloodlusting() { return this.bloodlusting; }
	public void setBloodlusting(boolean val)
	{
		if (this.bloodlusting == val)
		{
			// No real change - just view the info.
			this.msg(MLang.get().boolIsY("Bloodlust", val));
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
					msg(MLang.get().onlyVampsCanX, "use bloodlust");
					return;
				}
				
				if (this.getFood().get() < UConf.get(me).bloodlustMinFood)
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
				UConf uconf = UConf.get(me);
				uconf.effectConfBloodlust.removePotionEffects(me);
			}
		}
		
		this.changed(this.bloodlusting, val);
		this.bloodlusting = val;
		this.msg(this.bloodlustMsg());
		this.update();
	}
	public String bloodlustMsg() { return MLang.get().boolIsY("Bloodlust", this.isBloodlusting()) + " " + MLang.get().quotaIsPercent("combat damage", this.combatDamageFactor()); }
	
	// FIELD: usingNightVision - Vampires can use nightvision anytime they want.
	protected boolean usingNightVision = false;
	public boolean isUsingNightVision() { return this.usingNightVision; }
	public void setUsingNightVision(boolean val)
	{
		// If an actual change is being made ...
		if (this.usingNightVision == val) return;
		
		// ... mark as changed ...
		this.changed(this.usingNightVision, val);
		
		// ... do change stuff ...
		this.usingNightVision = val;
		
		// ... remove the nightvision potion effects ...
		Player me = this.getPlayer();
		if (me != null)
		{
			UConf uconf = UConf.get(me);
			uconf.effectConfNightvision.removePotionEffects(me);
		}
		
		// ... trigger a potion effect update ...
		this.updatePotionEffects();
		
		this.msg(this.usingNightVisionMsg());
	}
	public String usingNightVisionMsg() { return MLang.get().boolIsY("Nightvision", this.isUsingNightVision()); }
	
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
	protected transient UPlayerFoodAccumulator food = new UPlayerFoodAccumulator(this);
	public UPlayerFoodAccumulator getFood() { return this.food; }
	
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
	
	// FIELD: truceBreakMillisLeft - How many milliseconds more will the monsters be hostile?
	protected transient long truceBreakMillisLeft = 0;
	
	// FIELD: infectionOffered data - for the offer and accept commands.
	protected transient UPlayer tradeOfferedFrom;
	protected transient double tradeOfferedAmount;
	protected transient long tradeOfferedAtMillis;
	
	// FIELD: permA - permission assignments
	protected transient PermissionAttachment permA;
	
	// -------------------------------------------- //
	// FX
	// -------------------------------------------- //
	
	// FX: Smoke
	protected transient long fxSmokeMillis = 0;
	public long getFxSmokeMillis() { return this.fxSmokeMillis; }
	public void setFxSmokeMillis(long fxSmokeMillis) { this.fxSmokeMillis = fxSmokeMillis; }
	public void runFxSmoke() { this.fxSmokeMillis = 20L * 1000L; }
	
	// FX: Ender
	protected transient long fxEnderMillis = 0;
	public long getFxEnderMillis() { return this.fxEnderMillis; }
	public void getFxEnderMillis(long fxEnderMillis) { this.fxEnderMillis = fxEnderMillis; }
	public void runFxEnder() { this.fxEnderMillis = 10L * 1000L; }
	
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
		double dcount = UConf.get(me).fxSmokeBurstCount;
		long lcount = MUtil.probabilityRound(dcount);
		for (long i = lcount; i > 0; i--) FxUtil.smoke(me);
	}
	
	// FX: EnderBurst
	public void runFxEnderBurst()
	{
		Player me = this.getPlayer();
		if (me == null) return;
		double dcount = UConf.get(me).fxEnderBurstCount;
		long lcount = MUtil.probabilityRound(dcount);
		for (long i = lcount; i > 0; i--) FxUtil.ender(me, 0);
	}
	
	// FX: FlameBurst
	public void runFxFlameBurst()
	{
		Player me = this.getPlayer();
		if (me == null) return;
		double dcount = UConf.get(me).fxFlameBurstCount;
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
		UConf uconf = UConf.get(me);
		
		// You must be a vampire to shriek
		if ( ! this.isVampire())
		{
			msg(MLang.get().onlyVampsCanX, "shriek");
			return;
		}
		
		long now = System.currentTimeMillis();
		
		long millisSinceLastShriekWaitMessage = now - this.lastShriekWaitMessageMillis;
		if (millisSinceLastShriekWaitMessage < uconf.shriekWaitMessageCooldownMillis)
		{
			return;
		}
		
		long millisSinceLastShriek = now - this.lastShriekMillis;
		long millisToWait = uconf.shriekCooldownMillis - millisSinceLastShriek;
		
		if (millisToWait > 0)
		{
			long secondsToWait = (long) Math.ceil(millisToWait / 1000D);
			this.msg(MLang.get().shriekWait, secondsToWait);
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
	
	// -------------------------------------------- //
	// UPDATE > PERMISSONS
	// -------------------------------------------- //
	
	public void updatePermissions()
	{
		// Player
		Player player = this.getPlayer();
		if (player == null) return;
		
		// Attachment
		PermissionAttachment attachment = PermissionUtil.getPermissibleAttachment(player, Vampire.get(), true);
		
		// Permissions
		UConf uconf = UConf.get(player);
		Map<String, Boolean> permissions = (this.isVampire() ? uconf.updatePermsVampire : uconf.updatePermsHuman);
		
		// Update
		PermissionUtil.setAttachmentPermissions(attachment, permissions);
	}
	
	// -------------------------------------------- //
	// UPDATE > POTION EFFECTS
	// -------------------------------------------- //
	
	public void updatePotionEffects()
	{
		final int okDuration = 300;
		
		// TODO: I made this dirty fix for lower tps.
		// TODO: The real solution is to tick based on millis and not ticks.
		//final int targetDuration = okDuration*2; 
		final int targetDuration = okDuration*4;
		
		// Find the player and their conf
		Player player = this.getPlayer();
		if (player == null) return;
		if (player.isDead()) return;
		UConf uconf = UConf.get(player);
		
		// Add effects based their		
		if (this.isHuman())
		{
			uconf.effectConfHuman.addPotionEffects(player, targetDuration, okDuration);
		}
		
		if (this.isInfected())
		{
			uconf.effectConfInfected.addPotionEffects(player, targetDuration, okDuration);
		}
		
		if (this.isVampire())
		{
			uconf.effectConfVampire.addPotionEffects(player, targetDuration, okDuration);
		}
		
		if (this.isVampire() && uconf.nightvisionCanBeUsed && this.isUsingNightVision())
		{
			uconf.effectConfNightvision.addPotionEffects(player, targetDuration, okDuration);
		}
		
		if (this.isVampire() && this.isBloodlusting())
		{
			uconf.effectConfBloodlust.addPotionEffects(player, targetDuration, okDuration);
		}
	}
	
	// -------------------------------------------- //
	// TICK
	// -------------------------------------------- //
	
	public void tick(long millis)
	{
		this.tickRadTemp(millis);
		this.tickInfection(millis);
		this.tickRegen(millis);
		this.tickBloodlust(millis);
		this.tickPotionEffects(millis);
		this.tickEffects(millis);
		this.tickTruce(millis);
	}
	
	public void tickRadTemp(long millis)
	{
		// Update rad and temp
		Player me = this.getPlayer();
		if (me == null) return;
		UConf uconf = UConf.get(me);
		
		if (me.getGameMode() != GameMode.CREATIVE && this.isVampire() && ! me.isDead())
		{
			this.rad = uconf.baseRad + SunUtil.calcPlayerIrradiation(me);
			double tempDelta = uconf.tempPerRadAndMilli * this.rad * millis;
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
	
	public void tickInfection(long millis)
	{
		if ( ! this.isInfected()) return;
		
		Player me = this.getPlayer();
		if (me == null) return;
		UConf uconf = UConf.get(me);
		
		if (me.getGameMode() == GameMode.CREATIVE) return;
		
		int indexOld = this.infectionGetMessageIndex();
		this.addInfection(millis * uconf.infectionPerMilli);
		int indexNew = this.infectionGetMessageIndex();
		
		if (this.isVampire()) return;
		if (indexOld == indexNew) return;
		
		if (uconf.infectionProgressDamage != 0) me.damage(uconf.infectionProgressDamage);
		if (uconf.infectionProgressNauseaTicks > 0) FxUtil.ensure(PotionEffectType.CONFUSION, me, uconf.infectionProgressNauseaTicks);
		
		this.msg(MLang.get().infectionFeeling.get(indexNew));
		this.msg(MLang.get().infectionHint.get(MassiveCore.random.nextInt(MLang.get().infectionHint.size())));
	}
	public int infectionGetMessageIndex()
	{
		return (int)((MLang.get().infectionFeeling.size()+1) * this.getInfection() / 1D) - 1;
	}
	
	public void tickRegen(long millis)
	{
		if ( ! this.isVampire()) return;
		Player me = this.getPlayer();
		if (me == null) return;
		UConf uconf = UConf.get(me);
		if (me.getGameMode() == GameMode.CREATIVE) return;
		if (me.isDead()) return;
		if (me.getHealth() >= me.getMaxHealth()) return;
		if (this.getFood().get() < uconf.regenMinFood) return;
		
		long millisSinceLastDamage = System.currentTimeMillis() - this.lastDamageMillis;
		if (millisSinceLastDamage < uconf.regenDelayMillis) return;
		
		double foodDiff = this.getFood().add(-uconf.regenFoodPerMilli * millis);
		
		double healthTarget = me.getHealth() - foodDiff * uconf.regenHealthPerFood;
		healthTarget = Math.min(healthTarget, me.getMaxHealth());
		healthTarget = Math.max(healthTarget, 0D);
		
		me.setHealth(healthTarget);
	}
	
	public void tickBloodlust(long millis)
	{
		if ( ! this.isVampire()) return;
		if ( ! this.isBloodlusting()) return;
		Player me = this.getPlayer();
		if (me == null) return;
		UConf uconf = UConf.get(me);
		if (me.getGameMode() == GameMode.CREATIVE) return;
		if (me.isDead()) return;
		this.getFood().add(millis * uconf.bloodlustFoodPerMilli);
		if (this.getFood().get() < uconf.bloodlustMinFood) this.setBloodlusting(false);
	}
	
	public void tickPotionEffects(long millis)
	{
		// TODO: Will update to often!?
		this.updatePotionEffects();
	}
	
	public void tickEffects(long millis)
	{
		Player me = this.getPlayer();
		if (me == null) return;
		if (me.isDead()) return;
		UConf uconf = UConf.get(me);
		
		if (me.getGameMode() == GameMode.CREATIVE) return;

		// FX: Smoke
		if (this.fxSmokeMillis > 0)
		{
			this.fxSmokeMillis -= millis;
			double dcount = uconf.fxSmokePerMilli * millis;
			long lcount = MUtil.probabilityRound(dcount);
			for (long i = lcount; i > 0; i--) FxUtil.smoke(me);
		}
		
		// FX: Ender
		if (this.fxEnderMillis > 0)
		{
			this.fxEnderMillis -= millis;
			double dcount = uconf.fxEnderPerMilli * millis;
			long lcount = MUtil.probabilityRound(dcount);
			for (long i = lcount; i > 0; i--) FxUtil.ender(me, uconf.fxEnderRandomMaxLen);
		}
		
		// Vampire sun reactions
		if (this.isVampire())
		{
			// Buffs
			if (this.getTemp() > uconf.sunNauseaTemp) FxUtil.ensure(PotionEffectType.CONFUSION, me, uconf.sunNauseaTicks);
			if (this.getTemp() > uconf.sunWeaknessTemp)  FxUtil.ensure(PotionEffectType.WEAKNESS, me, uconf.sunWeaknessTicks);
			if (this.getTemp() > uconf.sunSlowTemp) FxUtil.ensure(PotionEffectType.SLOW, me, uconf.sunSlowTicks);
			if (this.getTemp() > uconf.sunBlindnessTemp) FxUtil.ensure(PotionEffectType.BLINDNESS, me, uconf.sunBlindnessTicks);
			if (this.getTemp() > uconf.sunBurnTemp) FxUtil.ensureBurn(me, uconf.sunBurnTicks);
			
			// Fx
			double dsmokes = uconf.sunSmokesPerTempAndMilli * this.temp * millis;
			long lsmokes = MUtil.probabilityRound(dsmokes);
			for (long i = lsmokes; i > 0; i--) FxUtil.smoke(me);
			
			double dflames = uconf.sunFlamesPerTempAndMilli * this.temp * millis;
			long lflames = MUtil.probabilityRound(dflames);
			for (long i = lflames; i > 0; i--) FxUtil.flame(me);
		}
	}
	
	// -------------------------------------------- //
	// TRADE
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	public void tradeAccept()
	{
		Player me = this.getPlayer();
		if (me == null) return;
		UConf uconf = UConf.get(me);
		
		UPlayer vyou = this.tradeOfferedFrom;
		
		// Any offer available?
		if (vyou == null || System.currentTimeMillis() - this.tradeOfferedAtMillis > uconf.tradeOfferToleranceMillis)
		{
			this.msg(MLang.get().tradeAcceptNone);
			return;
		}

		// Standing close enough?
		if ( ! this.withinDistanceOf(vyou, uconf.tradeOfferMaxDistance))
		{
			this.msg(MLang.get().tradeNotClose, vyou.getDisplayName(vyou));
			return;
		}
		
		Player you = vyou.getPlayer();
		double amount = this.tradeOfferedAmount;
		
		// Enough blood?
		double enough = 0;
		if(vyou.isVampire())
		{
			// blood is only food for vampires
			enough = vyou.getFood().get();
		}
		else
		{
			// but blood is health for humans
			enough = vyou.getPlayer().getHealth();
		}
		if (this.tradeOfferedAmount > enough)
		{
			vyou.msg(MLang.get().tradeLackingOut);
			this.msg(MLang.get().tradeLackingIn, you.getDisplayName());
			return;
		}
		
		// Transfer blood (food for vampires, life for humans)
		if(vyou.isVampire())
		{
			vyou.getFood().add(-amount);
		}
		else
		{
			vyou.getPlayer().damage(amount);
		}
		this.getFood().add(amount * uconf.tradePercentage);
		
		// Risk infection/boost infection
		if(!this.isVampire()){
			if(this.isInfected()){
					this.addInfection(0.01D);
			}else if (MassiveCore.random.nextDouble()*20 < amount)
			{
					this.addInfection(0.05D, InfectionReason.TRADE, vyou);
			}
		}
		// Trader Messages
		vyou.msg(MLang.get().tradeTransferOut, me.getDisplayName(), amount);
		this.msg(MLang.get().tradeTransferIn, amount, you.getDisplayName());
		
		// Who noticed?
		Location tradeLocation = me.getLocation();
		World tradeWorld = tradeLocation.getWorld();
		Location l1 = me.getEyeLocation();
		Location l2 = you.getEyeLocation();
		for (Player player : tradeWorld.getPlayers())
		{
			if (player.getLocation().distance(tradeLocation) > uconf.tradeVisualDistance) continue;
			player.playEffect(l1, Effect.POTION_BREAK, 5);
			player.playEffect(l2, Effect.POTION_BREAK, 5);
			if (player.equals(me)) continue;
			if (player.equals(you)) continue;
			String message = Txt.parse(MLang.get().tradeSeen, me.getDisplayName(), you.getDisplayName());
			MixinMessage.get().messageOne(player, message);
		}
		
		// Reset trade memory
		this.tradeOfferedFrom = null;
		this.tradeOfferedAtMillis = 0;
		this.tradeOfferedAmount = 0;
	}
	
	public void tradeOffer(UPlayer vyou, double amount)
	{
		Player you = vyou.getPlayer();
		if (you == null) return;
		Player me = this.getPlayer();
		if (me == null) return;
		UConf uconf = UConf.get(me);
		
		if ( ! this.withinDistanceOf(vyou, uconf.tradeOfferMaxDistance))
		{
			this.msg(MLang.get().tradeNotClose, vyou.getDisplayName(this.getId()));
			return;
		}
		
		if (this == vyou)
		{
			this.msg(MLang.get().tradeSelf);
			FxUtil.ensure(PotionEffectType.CONFUSION, me, 12*20);
			return;
		}
		
		vyou.tradeOfferedFrom = this;
		vyou.tradeOfferedAtMillis = System.currentTimeMillis();
		vyou.tradeOfferedAmount = amount;
		
		this.msg(MLang.get().tradeOfferOut, amount, you.getDisplayName());
		vyou.msg(MLang.get().tradeOfferIn, me.getDisplayName(), amount);
		vyou.msg(MLang.get().tradeAcceptHelp, CmdVampire.get().cmdVampireAccept.getTemplate(false).toPlain(true));
	}
	
	public boolean withinDistanceOf(UPlayer vyou, double maxDistance)
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
	
	public void tickTruce(long millis)
	{
		if ( ! this.isVampire()) return;
		if ( ! this.truceIsBroken()) return;
		
		this.truceBreakMillisLeftAlter(-millis);
		
		if ( ! this.truceIsBroken())
		{
			this.truceRestore();
		}
	}
	
	public boolean truceIsBroken()
	{
		return this.truceBreakMillisLeft != 0;
	}
	
	public void truceBreak()
	{
		Player player = this.getPlayer();
		if (player == null) return;
		UConf uconf = UConf.get(player);
		
		if ( ! this.truceIsBroken())
		{
			this.msg(MLang.get().truceBroken);
		}
		this.truceBreakMillisLeftSet(uconf.truceBreakMillis);
	}
	
	public void truceRestore()
	{
		this.msg(MLang.get().truceRestored);
		this.truceBreakMillisLeftSet(0);
		
		Player me = this.getPlayer();
		if (me == null) return;
		UConf uconf = UConf.get(me);
		
		// Untarget the player.
		for (LivingEntity entity : me.getWorld().getLivingEntities())
		{
			if ( ! uconf.truceEntityTypes.contains(entity.getType())) continue;
			
			if ( ! (entity instanceof Creature)) continue;
			Creature creature = (Creature)entity;
			
			Entity target = creature.getTarget();
			if ( ! me.equals(target)) continue;
			
			creature.setTarget(null);
		}
	}
	
	public long truceBreakMillisLeftGet()
	{
		return this.truceBreakMillisLeft;
	}
	
	private void truceBreakMillisLeftSet(long ticks)
	{
		if (ticks < 0)
		{
			this.truceBreakMillisLeft = 0;
		}
		else
		{
			this.truceBreakMillisLeft = ticks;
		}
	}
	
	private void truceBreakMillisLeftAlter(long delta)
	{
		this.truceBreakMillisLeftSet(this.truceBreakMillisLeftGet() + delta);
	}
	
	// -------------------------------------------- //
	// COMBAT
	// -------------------------------------------- //
	
	public double combatDamageFactor()
	{
		Player me = this.getPlayer();
		if (me == null) return 0D;
		UConf uconf = UConf.get(me);
		
		if (this.isBloodlusting()) return uconf.combatDamageFactorWithBloodlust;
		return uconf.combatDamageFactorWithoutBloodlust;
	}
	
	public double combatInfectRisk()
	{
		Player me = this.getPlayer();
		if (me == null) return 0D;
		UConf uconf = UConf.get(me);
		
		if (this.isHuman()) return 0D;
		if (this.isIntending()) return uconf.infectionRiskAtCloseCombatWithIntent;
		return uconf.infectionRiskAtCloseCombatWithoutIntent;
	}
}
