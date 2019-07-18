package com.massivecraft.vampire;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.InventoryUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.PlayerUtil;
import com.massivecraft.vampire.entity.MConf;
import com.massivecraft.vampire.entity.MLang;
import com.massivecraft.vampire.entity.UPlayer;
import com.massivecraft.vampire.util.FxUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class ListenerMain extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ListenerMain i = new ListenerMain();
	public static ListenerMain get() { return i; }
	
	// -------------------------------------------- //
	// FX
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void fxOnDeath(EntityDeathEvent event)
	{
		// If a vampire dies ...
		Player player = IdUtil.getAsPlayer(event.getEntity());
		if (player == null || MUtil.isntPlayer(player)) return;
		
		UPlayer uplayer = UPlayer.get(player);
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
		MConf mconf = MConf.get();
		if ( ! mconf.getBlockDamageFrom().contains(event.getCause())) return;
		
		if (entity == null || MUtil.isntPlayer(entity)) return;
		Player player = (Player)entity;
		UPlayer uplayer = UPlayer.get(player);
		
		if (uplayer.isVampire()) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockEvents(EntityRegainHealthEvent event)
	{
		Entity entity = event.getEntity();
		MConf mconf = MConf.get();
		if ( ! mconf.getBlockHealthFrom().contains(event.getRegainReason())) return;
		
		if (entity == null || MUtil.isntPlayer(entity)) return;
		Player player = (Player) entity;		
		UPlayer uplayer = UPlayer.get(player);
		
		if (uplayer.isVampire()) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockEvents(FoodLevelChangeEvent event)
	{
		Entity entity = event.getEntity();
		if (entity == null || MUtil.isntPlayer(entity)) return;
		
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
		if (player == null || MUtil.isntPlayer(player)) return;
		
		UPlayer uplayer = UPlayer.get(player);
		uplayer.update();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void updateOnTeleport(PlayerTeleportEvent event)
	{
		final Player player = event.getPlayer();
		if (player == null || MUtil.isntPlayer(player)) return;
		
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
		LivingEntity entity = event.getEntity();
		if (entity == null || MUtil.isntPlayer(entity)) return;
		UPlayer uplayer = UPlayer.get(entity);
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
		final Player player = event.getPlayer();
		if (player == null || MUtil.isntPlayer(player)) return;
		
		final UPlayer uplayer = UPlayer.get(player);
		if (uplayer == null) return;
		if ( ! uplayer.isVampire()) return;
		
		// ... modify food and health levels and force another speed-update.
		Bukkit.getScheduler().scheduleSyncDelayedTask(Vampire.get(), new Runnable()
		{
			@Override
			public void run()
			{
				MConf mconf = MConf.get();
				player.setFoodLevel(mconf.getUpdateRespawnFood());
				player.setHealth((double)mconf.getUpdateRespawnHealth());
				PlayerUtil.sendHealthFoodUpdatePacket(player);
				uplayer.update();
			}
		});
	}
	
	public void updateNameColor(Player player)
	{
		if (player == null || MUtil.isntPlayer(player)) return;
		MConf mconf = MConf.get();
		if (mconf.isUpdateNameColor() == false) return;
		UPlayer uplayer = UPlayer.get(player);
		if ( ! uplayer.isVampire()) return;
		player.setDisplayName(mconf.getUpdateNameColorTo().toString()+ChatColor.stripColor(player.getDisplayName()));
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
		MConf mconf = MConf.get();
		if (player != null && player.getGameMode() == GameMode.CREATIVE) return;
		
		// ... broke a self-dropping block ...  
		Material material = event.getBlock().getType();
		if ( ! mconf.getDropSelfMaterials().contains(material)) return;
		
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
		if (player == null || MUtil.isntPlayer(player)) return;
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
		MConf mconf = MConf.get();
		Location one = event.getFrom().clone();
		Location two = one.clone().add(0, 1, 0);
		long count1 = MUtil.probabilityRound(mconf.getBloodlustSmokes());
		long count2 = MUtil.probabilityRound(mconf.getBloodlustSmokes());
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
		if (player == null || MUtil.isntPlayer(player)) return;
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
		if (event.getTarget() == null || MUtil.isntPlayer(event.getTarget())) return;
		Player player = (Player)event.getTarget();
		MConf mconf = MConf.get();
		
		// ... by creature that cares about the truce with vampires ...
		if ( ! (mconf.getTruceEntityTypes().contains(event.getEntityType()))) return;
		
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
		MConf mconf = MConf.get();
		if ( ! (mconf.getTruceEntityTypes().contains(entity.getType()))) return;
		
		// ... and the liable damager is a vampire ...
		Entity damager = MUtil.getLiableDamager(event);
		if (damager == null || MUtil.isntPlayer(damager)) return;
		UPlayer vpdamager = UPlayer.get(damager);
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
		Entity entity = event.getEntity();
		if (entity == null || MUtil.isntPlayer(entity)) return;
		UPlayer vampire = UPlayer.get(entity);
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
		MConf mconf = MConf.get();
		
		// ... and the damagee is a vampire ...
		Entity entity = event.getEntity();
		if (entity == null || MUtil.isntPlayer(entity)) return;
		UPlayer vampire = UPlayer.get(entity);
		if (vampire == null) return;
		if ( ! vampire.isVampire()) return;
		
		// ... and a wooden item was used ...
		ItemStack item = InventoryUtil.getWeapon(damager);
		if (item == null) return;
		Material itemMaterial = item.getType();
		if ( ! mconf.getCombatWoodMaterials().contains(itemMaterial)) return;
		
		// ... Then modify damage!
		MUtil.setDamage(event, mconf.getCombatWoodDamage());
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void combatStrength(EntityDamageEvent event)
	{
		// If this is a close combat event ...
		if ( ! MUtil.isCloseCombatEvent(event)) return;
		
		// ... and the liable damager is a vampire ...
		Entity damager = MUtil.getLiableDamager(event);
		if (damager == null || MUtil.isntPlayer(damager)) return;
		UPlayer vampire = UPlayer.get(damager);
		if (vampire == null) return;
		if ( ! vampire.isVampire()) return;
		
		// ... and this event isn't a forbidden mcmmo one ...
		if ( ! MConf.get().isCombatDamageFactorWithMcmmoAbilities() && event.getClass().getName().equals("com.gmail.nossr50.events.fake.FakeEntityDamageByEntityEvent")) return;
		
		// ... Then modify damage!
		MUtil.scaleDamage(event, vampire.combatDamageFactor());
	}
	
	// -------------------------------------------- //
	// INFECT PLAYERS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void infection(EntityDamageEvent event)
	{
		// If this is a close combat event ...
		if ( ! MUtil.isCloseCombatEvent(event)) return;
		
		// ... where there is one vampire and one non-vampire ...
		Entity damagee = event.getEntity();
		if (damagee == null || MUtil.isntPlayer(damagee)) return;
		UPlayer vpdamagee = UPlayer.get(damagee);
		if (vpdamagee == null) return;
		Entity damager = MUtil.getLiableDamager(event);
		if (damager == null || MUtil.isntPlayer(damager)) return;
		UPlayer vpdamager = UPlayer.get(damager);
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
		if (MassiveCore.random.nextDouble() > vampire.combatInfectRisk()) return;
		
		InfectionReason reason = vampire.isIntending() ? InfectionReason.COMBAT_INTENDED : InfectionReason.COMBAT_MISTAKE;
		human.addInfection(0.01D, reason, vampire);
	}

	// -------------------------------------------- //
	// INFECT HORSES
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void infectHorse(EntityDamageEvent event)
	{
		// If this is a close combat event ...
		if ( ! MUtil.isCloseCombatEvent(event)) return;
		
		// ... where there is one vampire ... 
		Entity damager = MUtil.getLiableDamager(event);
		if (damager == null || MUtil.isntPlayer(damager)) return;
		UPlayer vpdamager = UPlayer.get(damager);
		if (vpdamager == null) return;
		if (!vpdamager.isVampire()) return;
		UPlayer vampire = vpdamager;
		
		// ... and one is a living horse ...
		Entity damagee = event.getEntity();
		Horse horse = null;
		if (damagee instanceof Horse)
		{
			horse = (Horse)damagee;
			// only horses, no mules donkeys or undead ones
			if(horse.getVariant() != Variant.HORSE) return;
		}
		
		if ( vampire == null || horse == null) return;

		// ... and the vampire can infect horses
		MConf mconf = MConf.get();
		if(!mconf.isCanInfectHorses()) return;
		
		// ... and the vampire is allowed to infect through combat ...
		if ( ! Perm.COMBAT_INFECT.has(vampire.getPlayer())) return;
		
		// ... Then there is a risk for infection ...
		if (MassiveCore.random.nextDouble() > vampire.combatInfectRisk()) return;
		
		// if its wearing armor remove it (otherwise it turns invisible and can crash people)
		ItemStack horseArmor = horse.getInventory().getArmor();
		if(horseArmor != null)
		{
			horse.getWorld().dropItem(horse.getLocation(), horseArmor);
			horse.getInventory().setArmor(null);
		}
		horse.setVariant(MassiveCore.random.nextDouble() > 0.5 ? Variant.SKELETON_HORSE : Variant.UNDEAD_HORSE);
	}

	// -------------------------------------------- //
	// FOOD
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void foodCake(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		if (player == null || MUtil.isntPlayer(player)) return;
		MConf mconf = MConf.get();
		
		// If cake eating is not allowed for vampires ...
		if (mconf.isFoodCakeAllowed()) return;
		
		// .. and the player right-clicks a cake block ...
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (event.getClickedBlock().getType() != Material.CAKE) return;
		
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
		MConf mconf = MConf.get();
		
		// ... of a tasty type ...
		Double fullFoodQuotient = mconf.getEntityTypeFullFoodQuotient().get(damagee.getType());
		if (fullFoodQuotient == null || fullFoodQuotient == 0) return;
		
		// ... that has blood left ...
		if (damagee.getHealth() < 0) return;
		if (damagee.isDead()) return;
		if (damagee instanceof Horse)
		{
			Horse horse = (Horse) damagee;
			if(horse.getVariant() == Variant.SKELETON_HORSE || horse.getVariant() == Variant.UNDEAD_HORSE) return;
		}
		
		// ... and the liable damager is a vampire ...
		Entity damager = MUtil.getLiableDamager(event);
		if (damager == null || MUtil.isntPlayer(damager)) return;
		UPlayer vampire = UPlayer.get(damager);
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
	// BLOOD FLASK
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void bloodFlaskConsume(PlayerItemConsumeEvent event)
	{
		// If the item is a potion ...
		ItemStack item = event.getItem();
		if ( ! item.getType().equals(Material.POTION)) return;
		
		// ... and is a blood flask ...
		if ( ! BloodFlaskUtil.isBloodFlask(item)) return;
		
		// ... get the blood amount ...
		double amount = BloodFlaskUtil.getBloodFlaskAmount(item);
		
		// ... and is the blood vampiric?  ...
		boolean isVampiric = BloodFlaskUtil.isBloodFlaskVampiric(item);
		
		// ... get the player ...
		UPlayer uplayer = UPlayer.get(event.getPlayer());
		
		// ... if the player is a vampire, are they bloodlusting? ...
		if (uplayer.isBloodlusting())
		{
			uplayer.msg(MLang.get().flaskBloodlusting);
			event.setCancelled(true);
			return;
		}
		
		// ... calculate and add the blood amount to the player ...
		double lacking;
		if (uplayer.isVampire())
		{
			// Vampires drink blood to replenish food.
			lacking = (20 - uplayer.getFood().get());
			if (amount > lacking) amount = lacking;
			uplayer.getFood().add(amount);
		}
		
		// ... finally, if the player is human did they contract the dark disease from vampiric blood?
		if (uplayer.isVampire() && ! isVampiric) return;
		if (uplayer.isInfected())
		{
			uplayer.addInfection(0.01D);
		}
		else if (MassiveCore.random.nextDouble() * 20 < amount)
		{
			uplayer.addInfection(0.05D, InfectionReason.FLASK, uplayer);
		}
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
		MConf mconf = MConf.get();
		if ( ! (projectile instanceof ThrownPotion)) return;
		ThrownPotion thrownPotion = (ThrownPotion)projectile;
		
		// ... and the potion type is holy water ...
		if ( ! HolyWaterUtil.isHolyWater(thrownPotion)) return;
		
		// ... who is the thrower and where did it splash? ...
		Location splashLocation = thrownPotion.getLocation();
		
		ProjectileSource projectileShooter = projectile.getShooter();
		if (projectileShooter == null || MUtil.isntPlayer(projectileShooter)) return;
		Player shooter = (Player)projectileShooter;
		
		// ... then to all nearby players ...
		for (Player player : splashLocation.getWorld().getPlayers())
		{
			if (player == null || MUtil.isntPlayer(player)) continue;
			if (player.getLocation().distance(splashLocation) > mconf.getHolyWaterSplashRadius()) continue;
			UPlayer uplayer = UPlayer.get(player);
			uplayer.msg(MLang.get().holyWaterCommon, shooter.getDisplayName());
			uplayer.runFxEnderBurst();
			
			// Trigger a damage event so other plugins can cancel this.
			EntityDamageByEntityEvent triggeredEvent = new EntityDamageByEntityEvent(shooter, player, DamageCause.CUSTOM, 1D);
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
				uplayer.addTemp(mconf.getHolyWaterTemp());
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
		if (player == null || MUtil.isntPlayer(player)) return;
		MConf mconf = MConf.get();
		
		if(mconf.getAltarDark().evalBlockUse(event.getClickedBlock(), player) || mconf.getAltarLight().evalBlockUse(event.getClickedBlock(), player))
		{
			event.setCancelled(true);
		}
	}
	
}
