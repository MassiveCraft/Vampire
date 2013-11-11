package com.massivecraft.vampire;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.integration.protocollib.MCoreEntityPotionColorEvent;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.PlayerUtil;
import com.massivecraft.vampire.entity.MLang;
import com.massivecraft.vampire.entity.UConf;
import com.massivecraft.vampire.entity.UPlayer;
import com.massivecraft.vampire.util.FxUtil;

public class ListenerMain implements Listener
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ListenerMain i = new ListenerMain();
	public static ListenerMain get() { return i; }
	public ListenerMain() {}
	
	// -------------------------------------------- //
	// ACTIVATE & DEACTIVATE
	// -------------------------------------------- //
	
	public void activate()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, Vampire.get());
	}
	
	public void deactivate()
	{
		HandlerList.unregisterAll(this);
	}
	
	// -------------------------------------------- //
	// ENTITY POTION COLOR
	// -------------------------------------------- //
	
	public void entityPotionColor(MCoreEntityPotionColorEvent event, EventPriority eventPriority)
	{
		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) return;
		
		Player player = (Player)entity;
		UPlayer uplayer = UPlayer.get(player);
		UConf uconf = UConf.get(player);
		
		if (uplayer.isHuman())
		{
			uconf.effectConfHuman.applyFor(event, eventPriority);
		}
		
		if (uplayer.isInfected())
		{
			uconf.effectConfInfected.applyFor(event, eventPriority);
		}
		
		if (uplayer.isVampire())
		{
			uconf.effectConfVampire.applyFor(event, eventPriority);
		}
		
		if (uplayer.isVampire() && uconf.nightvisionCanBeUsed && uplayer.isUsingNightVision())
		{
			uconf.effectConfNightvision.applyFor(event, eventPriority);
		}
		
		if (uplayer.isVampire() && uplayer.isBloodlusting())
		{
			uconf.effectConfBloodlust.applyFor(event, eventPriority);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void entityPotionColorLowest(MCoreEntityPotionColorEvent event)
	{
		this.entityPotionColor(event, EventPriority.LOWEST);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void entityPotionColorLow(MCoreEntityPotionColorEvent event)
	{
		this.entityPotionColor(event, EventPriority.LOW);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void entityPotionColorNormal(MCoreEntityPotionColorEvent event)
	{
		this.entityPotionColor(event, EventPriority.NORMAL);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void entityPotionColorHigh(MCoreEntityPotionColorEvent event)
	{
		this.entityPotionColor(event, EventPriority.HIGH);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void entityPotionColorHighest(MCoreEntityPotionColorEvent event)
	{
		this.entityPotionColor(event, EventPriority.HIGHEST);
	}
	
	// -------------------------------------------- //
	// FX
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void fxOnDeath(EntityDeathEvent event)
	{
		// If a vampire dies ...
		UPlayer uplayer = UPlayer.get(event.getEntity());
		if (uplayer == null) return;
		if (uplayer.isVampire() == false) return;
		
		// ... burns up with a violent scream ;,,;
		uplayer.runFxShriek();
		uplayer.runFxFlameBurst();
		uplayer.runFxSmokeBurst();
	}
	
	// -------------------------------------------- //
	// MISC
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void blockEvents(EntityDamageEvent event)
	{
		Entity entity = event.getEntity();
		UConf uconf = UConf.get(entity);
		
		if ( ! (entity instanceof Player)) return;
		if ( ! uconf.blockDamageFrom.contains(event.getCause())) return;
		
		Player player = (Player)entity;
		UPlayer uplayer = UPlayer.get(player);
		
		if (uplayer.isVampire()) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockEvents(EntityRegainHealthEvent event)
	{
		Entity entity = event.getEntity();
		UConf uconf = UConf.get(entity);
		
		if ( ! (entity instanceof Player)) return;
		if ( ! uconf.blockHealthFrom.contains(event.getRegainReason())) return;
		
		Player player = (Player) entity;		
		UPlayer uplayer = UPlayer.get(player);
		
		if (uplayer.isVampire()) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockEvents(FoodLevelChangeEvent event)
	{
		Entity entity = event.getEntity();
		if ( ! (entity instanceof Player)) return;
		
		Player player = (Player) entity;		
		UPlayer uplayer = UPlayer.get(player);
		
		if (uplayer.isVampire())
		{
			event.setCancelled(true);
			PlayerUtil.sendHealthFoodUpdatePacket(player);
		}
	}
	
	// -------------------------------------------- //
	// UPDATE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void updateOnJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		final UPlayer uplayer = UPlayer.get(player);
		uplayer.update();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void updateOnTeleport(PlayerTeleportEvent event)
	{
		final Player player = event.getPlayer();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Vampire.get(), new Runnable()
		{
			@Override
			public void run()
			{
				UPlayer uplayer = UPlayer.get(player);
				uplayer.update();
			}
		});
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void updateOnDeath(EntityDeathEvent event)
	{
		// If a vampire dies ...
		UPlayer uplayer = UPlayer.get(event.getEntity());
		if (uplayer == null) return;
		if (uplayer.isVampire() == false) return;
		
		// Close down bloodlust.
		uplayer.setRad(0);
		uplayer.setTemp(0);
		uplayer.setBloodlusting(false);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void updateOnRespawn(PlayerRespawnEvent event)
	{
		// If the player is a vampire ...
		final UPlayer uplayer = UPlayer.get(event.getPlayer());
		if (uplayer == null) return;
		if ( ! uplayer.isVampire()) return;
		
		// ... modify food and health levels and force another speed-update.
		Bukkit.getScheduler().scheduleSyncDelayedTask(Vampire.get(), new Runnable()
		{
			@Override
			public void run()
			{
				Player player = uplayer.getPlayer();
				if (player == null) return;
				UConf uconf = UConf.get(player);
				player.setFoodLevel(uconf.updateRespawnFood);
				player.setHealth(uconf.updateRespawnHealth);
				PlayerUtil.sendHealthFoodUpdatePacket(player);
				uplayer.update();
			}
		});
	}
	
	public void updateNameColor(Player player)
	{
		UConf uconf = UConf.get(player); 
		if (uconf.updateNameColor == false) return;
		UPlayer uplayer = UPlayer.get(player);
		if ( ! uplayer.isVampire()) return;
		player.setDisplayName(uconf.updateNameColorTo.toString()+ChatColor.stripColor(player.getDisplayName()));
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void updateNameColor(AsyncPlayerChatEvent event)
	{
		updateNameColor(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void updateNameColor(PlayerJoinEvent event)
	{
		updateNameColor(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void updateNameColor(PlayerTeleportEvent event)
	{
		updateNameColor(event.getPlayer());
	}
	
	
	// -------------------------------------------- //
	// DROP SELF
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void dropSelf(BlockBreakEvent event)
	{
		// If a non-creative player ...
		Player player = event.getPlayer();
		UConf uconf = UConf.get(player);
		if (player != null && player.getGameMode() == GameMode.CREATIVE) return;
		
		// ... broke a self-dropping block ...  
		Material material = event.getBlock().getType();
		if ( ! uconf.dropSelfMaterials.contains(material)) return;
		
		// ... then we make it drop itself.	
		event.setCancelled(true);
		event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(material, 1));
		event.getBlock().setType(Material.AIR);
	}
	
	// -------------------------------------------- //
	// BLOODLUST
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void bloodlustSmokeTrail(PlayerMoveEvent event)
	{
		// If a noncreative player ...
		Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.CREATIVE) return;
		
		// ... moved between two blocks ...
		Block from = event.getFrom().getBlock();
		Block to = event.getTo().getBlock();
		if (from.equals(to)) return;
		
		// ... and that player is a vampire ...
		UPlayer uplayer = UPlayer.get(player);
		if (uplayer.isHuman()) return;
		
		// ... that has bloodlust on ...
		if ( ! uplayer.isBloodlusting()) return;
		
		// ... then spawn smoke trail.
		UConf uconf = UConf.get(player);
		Location one = event.getFrom().clone();
		Location two = one.clone().add(0, 1, 0);
		long count1 = MUtil.probabilityRound(uconf.bloodlustSmokes);
		long count2 = MUtil.probabilityRound(uconf.bloodlustSmokes);
		for (long i = count1; i > 0; i--) FxUtil.smoke(one);
		for (long i = count2; i > 0; i--) FxUtil.smoke(two);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void bloodlustGameModeToggle(PlayerGameModeChangeEvent event)
	{
		// If a player enters creative-mode ...
		if (event.getNewGameMode() != GameMode.CREATIVE) return;
		
		// ... turn of bloodlust ...
		Player player = event.getPlayer();
		UPlayer uplayer = UPlayer.get(player);
		uplayer.setBloodlusting(false);
	}
	
	// -------------------------------------------- //
	// TRUCE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void truceTarget(EntityTargetEvent event)
	{
		// If a player is targeted...
		if ( ! (event.getTarget() instanceof Player)) return;
		
		Player player = (Player)event.getTarget();
		UConf uconf = UConf.get(player);
		
		// ... by creature that cares about the truce with vampires ...
		if ( ! (uconf.truceEntityTypes.contains(event.getEntityType()))) return;
		
		UPlayer uplayer = UPlayer.get(player);
		
		// ... and that player is a vampire ...
		if ( ! uplayer.isVampire()) return;
		
		// ... that has not recently done something to break the truce...
		if (uplayer.truceIsBroken()) return;
		
		// ... then if the player is a ghast target nothing ...
		if (event.getEntityType() == EntityType.GHAST)
		{
			event.setTarget(null);
			return;
		}
		
		// ... otherwise cancel the event.
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void truceDamage(EntityDamageEvent event)
	{
		// If this is a combat event ...
		if ( ! MUtil.isCombatEvent(event)) return;		
		
		// ... to a creature that cares about the truce with vampires...
		Entity entity = event.getEntity();
		UConf uconf = UConf.get(entity);
		if ( ! (uconf.truceEntityTypes.contains(entity.getType()))) return;
		
		// ... and the liable damager is a vampire ...
		UPlayer vpdamager = UPlayer.get(MUtil.getLiableDamager(event));
		if (vpdamager == null) return;
		if ( ! vpdamager.isVampire()) return;
		
		// Then that vampire broke the truce.
		vpdamager.truceBreak();
	}
	
	// -------------------------------------------- //
	// REGEN
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void regen(EntityDamageEvent event)
	{
		// If the damagee is a vampire ...
		UPlayer vampire = UPlayer.get(event.getEntity());
		if (vampire == null) return;
		if ( ! vampire.isVampire()) return;
		
		// ... mark now as lastDamageMillis
		vampire.setLastDamageMillis(System.currentTimeMillis());
	}
	
	// -------------------------------------------- //
	// COMBAT
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void combatVulnerability(EntityDamageEvent event)
	{
		// If this is a close combat event ...
		if ( ! MUtil.isCloseCombatEvent(event)) return;
		
		// ... where the liable damager is a human entity ...
		Entity damagerEntity = MUtil.getLiableDamager(event);
		if ( ! (damagerEntity instanceof HumanEntity)) return;
		HumanEntity damager = (HumanEntity) damagerEntity;
		UConf uconf = UConf.get(damager);
		
		// ... and the damagee is a vampire ...
		UPlayer vampire = UPlayer.get(event.getEntity());
		if (vampire == null) return;
		if ( ! vampire.isVampire()) return;
		
		// ... and a wooden item was used ...
		Material itemMaterial = damager.getItemInHand().getType();
		if ( ! uconf.combatWoodMaterials.contains(itemMaterial)) return;
		
		// ... Then modify damage!
		event.setDamage(uconf.combatWoodDamage);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void combatStrength(EntityDamageEvent event)
	{
		// If this is a close combat event ...
		if ( ! MUtil.isCloseCombatEvent(event)) return;
		
		// ... and the liable damager is a vampire ...
		UPlayer vampire = UPlayer.get(MUtil.getLiableDamager(event));
		if (vampire == null) return;
		if ( ! vampire.isVampire()) return;
		
		// ... Then modify damage!
		double damage = event.getDamage();
		damage *= vampire.combatDamageFactor();
		event.setDamage((int) MUtil.probabilityRound(damage));
	}
	
	// -------------------------------------------- //
	// INFECTION
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void infection(EntityDamageEvent event)
	{
		// If this is a close combat event ...
		if ( ! MUtil.isCloseCombatEvent(event)) return;
		
		// ... where there is one vampire and one non-vampire ...
		UPlayer vpdamagee = UPlayer.get(event.getEntity());
		if (vpdamagee == null) return;
		UPlayer vpdamager = UPlayer.get(MUtil.getLiableDamager(event));
		if (vpdamager == null) return;
		
		UPlayer vampire = null;
		UPlayer human = null;
		
		if (vpdamagee.isVampire())
		{
			vampire = vpdamagee;
		}
		else
		{
			human = vpdamagee;
		}
		
		if (vpdamager.isVampire())
		{
			vampire = vpdamager;
		}
		else
		{
			human = vpdamager;
		}
		
		if ( vampire == null || human == null) return;
		
		// ... and the vampire is allowed to infect through combat ...
		if ( ! Perm.COMBAT_INFECT.has(vampire.getPlayer())) return;
		
		// ... and the human is allowed to contract through combat ...
		if ( ! Perm.COMBAT_CONTRACT.has(human.getPlayer())) return;
		
		// ... Then there is a risk for infection ...
		if (MCore.random.nextDouble() > vampire.combatInfectRisk()) return;
		
		InfectionReason reason = vampire.isIntending() ? InfectionReason.COMBAT_INTENDED : InfectionReason.COMBAT_MISTAKE;
		human.addInfection(0.01D, reason, vampire);
	}
	
	// -------------------------------------------- //
	// FOOD
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void foodCake(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		UConf uconf = UConf.get(player);
		
		// If cake eating is not allowed for vampires ...
		if (uconf.foodCakeAllowed) return;
		
		// .. and the player right-clicks a cake block ...
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (event.getClickedBlock().getType() != Material.CAKE_BLOCK) return;
		
		// ... and the player is a vampire ...
		UPlayer uplayer = UPlayer.get(player);
		if ( ! uplayer.isVampire()) return;
		
		// ... we deny!
		event.setCancelled(true);
		uplayer.msg(MLang.get().foodCantEat, "cake");
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void foodBlood(EntityDamageEvent event)
	{
		// If this is a close combat event ...
		if ( ! MUtil.isCloseCombatEvent(event)) return;
		
		// ... to a living entity ...
		if ( ! (event.getEntity() instanceof LivingEntity)) return;
		LivingEntity damagee = (LivingEntity)event.getEntity();
		UConf uconf = UConf.get(damagee);
		
		// ... of a tasty type ...
		Double fullFoodQuotient = uconf.entityTypeFullFoodQuotient.get(damagee.getType());
		if (fullFoodQuotient == null || fullFoodQuotient == 0) return;
		
		// ... that has blood left ...
		if (damagee.getHealth() < 0) return;
		if (damagee.isDead()) return;
		
		// ... and the liable damager is a vampire ...
		UPlayer vampire = UPlayer.get(MUtil.getLiableDamager(event));
		if (vampire == null) return;
		if ( ! vampire.isVampire()) return;
		
		// ... and the player is still retrievable ...
		Player player = vampire.getPlayer();
		if (player == null) return;
		
		// ... drink blood! ;,,;
		double damage = event.getDamage();
		if (damagee.getHealth() < damage) damage = damagee.getHealth();
		double food = damage / damagee.getMaxHealth() * fullFoodQuotient * player.getMaxHealth();
		
		vampire.getFood().add(food);
	}
	
	// -------------------------------------------- //
	// HOLY WATER
	// -------------------------------------------- //
	
	/**
	 * You may wonder why the PotionSplashEvent was not used.
	 * That event is not triggered. This potion has no vanilla effects.
	 * Thus only this projectile hit event is triggered.
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void holyWater(ProjectileHitEvent event)
	{
		// If this projectile is a thrown potion ...
		Projectile projectile = event.getEntity();
		UConf uconf = UConf.get(projectile);
		if ( ! (projectile instanceof ThrownPotion)) return;
		ThrownPotion thrownPotion = (ThrownPotion)projectile;
		
		// ... and the potion type is holy water ...
		if ( ! HolyWaterUtil.isHolyWater(thrownPotion)) return;
		
		// ... who is the thrower and where did it splash? ...
		Location splashLocation = thrownPotion.getLocation();
		Player shooter = (Player)projectile.getShooter();
		
		// ... then to all nearby players ...
		for (Player player : splashLocation.getWorld().getPlayers())
		{
			if (player.getLocation().distance(splashLocation) > uconf.holyWaterSplashRadius) continue;
			UPlayer uplayer = UPlayer.get(player);
			uplayer.msg(MLang.get().holyWaterCommon, shooter.getDisplayName());
			uplayer.runFxEnderBurst();
			
			// Trigger a damage event so other plugins can cancel this.
			EntityDamageByEntityEvent triggeredEvent = new EntityDamageByEntityEvent(projectile.getShooter(), player, DamageCause.CUSTOM, 1D);
			Bukkit.getPluginManager().callEvent(triggeredEvent);
			if (triggeredEvent.isCancelled()) continue;
			
			if (uplayer.isHealthy())
			{
				uplayer.msg(MLang.get().holyWaterHealthy);
			}
			else if (uplayer.isInfected())
			{
				uplayer.msg(MLang.get().holyWaterInfected);
				uplayer.setInfection(0);
				uplayer.runFxEnder();
			}
			else if (uplayer.isVampire())
			{
				uplayer.msg(MLang.get().holyWaterVampire);
				uplayer.addTemp(uconf.holyWaterTemp);
				uplayer.runFxFlameBurst();
			}
		}
	}	
	
	// -------------------------------------------- //
	// ALTARS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void altars(PlayerInteractEvent event)
	{
		// If the player right-clicked a block ...
		Action action = event.getAction();
		if ( action != Action.RIGHT_CLICK_BLOCK) return;
		
		// ... run altar logic.
		Player player = event.getPlayer();
		UConf uconf = UConf.get(player);
		
		uconf.altarDark.evalBlockUse(event.getClickedBlock(), player);
		uconf.altarLight.evalBlockUse(event.getClickedBlock(), player);
	}
	
}
