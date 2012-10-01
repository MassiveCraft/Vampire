package com.massivecraft.vampire;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftThrownPotion;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

import com.massivecraft.mcore4.MCore;
import com.massivecraft.mcore4.util.MUtil;
import com.massivecraft.mcore4.util.PlayerUtil;
import com.massivecraft.vampire.util.FxUtil;

public class TheListener implements Listener
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public P p;
	public TheListener(P p)
	{
		this.p = p;
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
	}
	
	// -------------------------------------------- //
	// FX
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void fxOnDeath(EntityDeathEvent event)
	{
		// If a vampire dies ...
		VPlayer vplayer = VPlayer.get(event.getEntity());
		if (vplayer == null) return;
		if (vplayer.isVampire() == false) return;
		
		// ... burns up with a violent scream ;,,;
		vplayer.runFxShriek();
		vplayer.runFxFlameBurst();
		vplayer.runFxSmokeBurst();
	}
	
	// -------------------------------------------- //
	// MISC
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void blockEvents(EntityDamageEvent event)
	{
		Entity entity = event.getEntity();
		Conf conf = Conf.get(entity);
		
		if ( ! (entity instanceof Player)) return;
		if ( ! conf.blockDamageFrom.contains(event.getCause())) return;
		
		Player player = (Player)entity;
		VPlayer vplayer = VPlayer.get(player);
		
		if (vplayer.isVampire()) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockEvents(EntityRegainHealthEvent event)
	{
		Entity entity = event.getEntity();
		Conf conf = Conf.get(entity);
		
		if ( ! (entity instanceof Player)) return;
		if ( ! conf.blockHealthFrom.contains(event.getRegainReason())) return;
		
		Player player = (Player) entity;		
		VPlayer vplayer = VPlayer.get(player);
		
		if (vplayer.isVampire()) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockEvents(FoodLevelChangeEvent event)
	{
		Entity entity = event.getEntity();
		if ( ! (entity instanceof Player)) return;
		
		Player player = (Player) entity;		
		VPlayer vplayer = VPlayer.get(player);
		
		if (vplayer.isVampire())
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
		final VPlayer vplayer = VPlayer.get(player);
		vplayer.update();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void updateOnTeleport(PlayerTeleportEvent event)
	{
		final Player player = event.getPlayer();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(p, new Runnable()
		{
			@Override
			public void run()
			{
				VPlayer vplayer = VPlayer.get(player);
				vplayer.update();
			}
		});
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void updateOnDeath(EntityDeathEvent event)
	{
		// If a vampire dies ...
		VPlayer vplayer = VPlayer.get(event.getEntity());
		if (vplayer == null) return;
		if (vplayer.isVampire() == false) return;
		
		// Close down bloodlust.
		vplayer.setRad(0);
		vplayer.setTemp(0);
		vplayer.setBloodlusting(false);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void updateOnRespawn(PlayerRespawnEvent event)
	{
		// If the player is a vampire ...
		final VPlayer vplayer = VPlayer.get(event.getPlayer());
		if (vplayer == null) return;
		if ( ! vplayer.isVampire()) return;
		
		// ... modify food and health levels and force another speed-update.
		Bukkit.getScheduler().scheduleSyncDelayedTask(p, new Runnable()
		{
			@Override
			public void run()
			{
				Player player = vplayer.getPlayer();
				Conf conf = Conf.get(player);
				player.setFoodLevel(conf.updateRespawnFood);
				player.setHealth(conf.updateRespawnHealth);
				PlayerUtil.sendHealthFoodUpdatePacket(player);
				vplayer.update();
			}
		});
	}
	
	public void updateNameColor(Player player)
	{
		Conf conf = Conf.get(player); 
		if (conf.updateNameColor == false) return;
		VPlayer vplayer = VPlayer.get(player);
		if ( ! vplayer.isVampire()) return;
		player.setDisplayName(conf.updateNameColorTo.toString()+ChatColor.stripColor(player.getDisplayName()));
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
		Conf conf = Conf.get(player);
		if (player != null && player.getGameMode() == GameMode.CREATIVE) return;
		
		// ... broke a self-dropping block ...  
		Material material = event.getBlock().getType();
		if ( ! conf.dropSelfMaterials.contains(material)) return;
		
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
		// If a survivalmode player ...
		Player player = event.getPlayer();
		if (player.getGameMode() != GameMode.SURVIVAL) return;
		
		// ... moved between two blocks ...
		Block from = event.getFrom().getBlock();
		Block to = event.getTo().getBlock();
		if (from.equals(to)) return;
		
		// ... and that player is a vampire ...
		VPlayer vplayer = VPlayer.get(player);
		if (vplayer.isHuman()) return;
		
		// ... that has bloodlust on ...
		if ( ! vplayer.isBloodlusting()) return;
		
		// ... then spawn smoke trail.
		Conf conf = Conf.get(player);
		Location one = event.getFrom().clone();
		Location two = one.clone().add(0, 1, 0);
		long count1 = MUtil.probabilityRound(conf.bloodlustSmokes);
		long count2 = MUtil.probabilityRound(conf.bloodlustSmokes);
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
		VPlayer vplayer = VPlayer.get(player);
		vplayer.setBloodlusting(false);
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
		Conf conf = Conf.get(player);
		
		// ... by creature that cares about the truce with vampires ...
		if ( ! (conf.truceEntityTypes.contains(event.getEntity().getType()))) return;
		
		VPlayer vplayer = VPlayer.get(player);
		
		// ... and that player is a vampire ...
		if ( ! vplayer.isVampire()) return;
		
		// ... that has not recently done something to break the truce...
		if (vplayer.truceIsBroken()) return;
		
		// Then the creature will not attack.
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void truceDamage(EntityDamageEvent event)
	{
		// If this is a combat event ...
		if ( ! MUtil.isCombatEvent(event)) return;		
		
		// ... to a creature that cares about the truce with vampires...
		Entity entity = event.getEntity();
		Conf conf = Conf.get(entity);
		if ( ! (conf.truceEntityTypes.contains(entity.getType()))) return;
		
		// ... and the liable damager is a vampire ...
		VPlayer vpdamager = VPlayer.get(MUtil.getLiableDamager(event));
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
		VPlayer vampire = VPlayer.get(event.getEntity());
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
		Conf conf = Conf.get(damager);
		
		// ... and the damagee is a vampire ...
		VPlayer vampire = VPlayer.get(event.getEntity());
		if (vampire == null) return;
		if ( ! vampire.isVampire()) return;
		
		// ... and a wooden item was used ...
		Material itemMaterial = damager.getItemInHand().getType();
		if ( ! conf.combatWoodMaterials.contains(itemMaterial)) return;
		
		// ... Then modify damage!
		event.setDamage(conf.combatWoodDamage);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void combatStrength(EntityDamageEvent event)
	{
		// If this is a close combat event ...
		if ( ! MUtil.isCloseCombatEvent(event)) return;
		
		// ... and the liable damager is a vampire ...
		VPlayer vampire = VPlayer.get(MUtil.getLiableDamager(event));
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
		VPlayer vpdamagee = VPlayer.get(event.getEntity());
		if (vpdamagee == null) return;
		VPlayer vpdamager = VPlayer.get(MUtil.getLiableDamager(event));
		if (vpdamager == null) return;
		
		VPlayer vampire = null;
		VPlayer human = null;
		
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
		if ( ! Permission.COMBAT_INFECT.has(vampire.getPlayer())) return;
		
		// ... and the human is allowed to contract through combat ...
		if ( ! Permission.COMBAT_CONTRACT.has(human.getPlayer())) return;
		
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
		Conf conf = Conf.get(player);
		
		// If cake eating is not allowed for vampires ...
		if (conf.foodCakeAllowed) return;
		
		// .. and the player right-clicks a cake block ...
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (event.getClickedBlock().getType() != Material.CAKE_BLOCK) return;
		
		// ... and the player is a vampire ...
		VPlayer vplayer = VPlayer.get(player);
		if ( ! vplayer.isVampire()) return;
		
		// ... we deny!
		event.setCancelled(true);
		vplayer.msg(Lang.foodCantEat, "cake");
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void foodBlood(EntityDamageEvent event)
	{
		// If this is a close combat event ...
		if ( ! MUtil.isCloseCombatEvent(event)) return;
		
		// ... to a living entity ...
		if ( ! (event.getEntity() instanceof LivingEntity)) return;
		LivingEntity damagee = (LivingEntity)event.getEntity();
		Conf conf = Conf.get(damagee);
		
		// ... of a tasty type ...
		Double fullFoodQuotient = conf.entityTypeFullFoodQuotient.get(damagee.getType());
		if (fullFoodQuotient == null || fullFoodQuotient == 0) return;
		
		// ... that has blood left ...
		if (damagee.getHealth() < 0) return;
		if (damagee.isDead()) return;
		
		// ... and the liable damager is a vampire ...
		VPlayer vampire = VPlayer.get(MUtil.getLiableDamager(event));
		if (vampire == null) return;
		if ( ! vampire.isVampire()) return;
		
		// ... drink blood! ;,,;
		double damage = event.getDamage();
		if (damagee.getHealth() < damage) damage = damagee.getHealth();
		double food = damage / damagee.getMaxHealth() * fullFoodQuotient * vampire.getPlayer().getMaxHealth();
		
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
		Conf conf = Conf.get(projectile);
		if ( ! (projectile instanceof ThrownPotion)) return;
		
		// ... and the potion type is holy water ...
		ThrownPotion potion = (ThrownPotion)projectile;
		CraftThrownPotion cpotion = (CraftThrownPotion)potion;
		int potionvalue = cpotion.getHandle().getPotionValue();
		if (potionvalue != conf.holyWaterPotionValue) return;
		
		// ... who is the thrower and where did it splash? ...
		Location splashLocation = potion.getLocation();
		Player shooter = (Player)projectile.getShooter();
		
		// ... then to all nearby players ...
		for (Player player : splashLocation.getWorld().getPlayers())
		{
			if (player.getLocation().distance(splashLocation) > conf.holyWaterSplashRadius) continue;
			VPlayer vplayer = VPlayer.get(player);
			vplayer.msg(Lang.holyWaterCommon, shooter.getDisplayName());
			vplayer.runFxEnderBurst();
			
			// Trigger a damage event so other plugins can cancel this.
			EntityDamageByEntityEvent triggeredEvent = new EntityDamageByEntityEvent(projectile.getShooter(), player, DamageCause.CUSTOM, 1);
			Bukkit.getPluginManager().callEvent(triggeredEvent);
			if (triggeredEvent.isCancelled()) continue;
			
			if (vplayer.isHealthy())
			{
				vplayer.msg(Lang.holyWaterHealthy);
			}
			else if (vplayer.isInfected())
			{
				vplayer.msg(Lang.holyWaterInfected);
				vplayer.setInfection(0);
				vplayer.runFxEnder();
			}
			else if (vplayer.isVampire())
			{
				vplayer.msg(Lang.holyWaterVampire);
				vplayer.addTemp(conf.holyWaterTemp);
				vplayer.runFxFlameBurst();
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
		Conf conf = Conf.get(player);
		
		conf.altarDark.evalBlockUse(event.getClickedBlock(), player);
		conf.altarLight.evalBlockUse(event.getClickedBlock(), player);
	}
	
}
