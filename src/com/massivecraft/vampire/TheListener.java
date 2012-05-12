package com.massivecraft.vampire;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.massivecraft.mcore3.MCore;
import com.massivecraft.mcore3.util.MUtil;
import com.massivecraft.mcore3.util.PlayerUtil;
import com.massivecraft.mcore3.util.Txt;
import com.massivecraft.vampire.event.SpoutCraftAuthenticationEvent;
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
	// NAME COLORIZE
	// -------------------------------------------- //
	
	public void nameColorize(Player player)
	{
		if (Conf.nameColorize == false) return;
		VPlayer vplayer = VPlayers.i.get(player);
		if ( ! vplayer.vampire()) return;
		player.setDisplayName(Conf.nameColor.toString()+ChatColor.stripColor(player.getDisplayName()));
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void nameColorize(PlayerChatEvent event)
	{
		nameColorize(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void nameColorize(PlayerJoinEvent event)
	{
		nameColorize(event.getPlayer());
	}
	
	// -------------------------------------------- //
	// DROP SELF
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void dropSelf(BlockBreakEvent event)
	{
		// If a non-creative player ...
		Player player = event.getPlayer();
		if (player != null && player.getGameMode() == GameMode.CREATIVE) return;
		
		// ... broke a self-dropping block ...  
		Material material = event.getBlock().getType();
		if ( ! Conf.dropSelfMaterials.contains(material)) return;
		
		// ... then we make it drop itself.	
		event.setCancelled(true);
		event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(material, 1));
		event.getBlock().setType(Material.AIR);
	}
	
	// -------------------------------------------- //
	// UPDATE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void update(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		final SpoutPlayer splayer = SpoutManager.getPlayer(player);
		final VPlayer vplayer = VPlayers.i.get(player);
		
		vplayer.updateVampPermission();
		
		new SpoutCraftAuthenticationEvent(splayer);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void update(SpoutCraftAuthenticationEvent event)
	{
		VPlayer vplayer = VPlayers.i.get(event.splayer());
		vplayer.updateSpoutMovement();
	}
	
	// -------------------------------------------- //
	// FX
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void fxOnDeath(EntityDeathEvent event)
	{
		// If a vampire dies ...
		VPlayer vplayer = VPlayers.i.get(event.getEntity());
		if (vplayer == null) return;
		if (vplayer.vampire() == false) return;
		
		// ... burns up with a violent scream ;,,;
		vplayer.fxScreamRun();
		vplayer.fxFlameBurstRun();
		vplayer.fxSmokeBurstRun();
		
		vplayer.updateSpoutMovement();
	}
	
	
	
	// -------------------------------------------- //
	// INFECTION
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void infection(PlayerInteractEvent event)
	{
		// If the player is eating bread ...
		if (MUtil.getEatenMaterial(event) != Material.BREAD) return;
		
		// ... and the player is an infected non-vampire ...
		Player player = event.getPlayer();
		VPlayer vplayer = VPlayers.i.get(player);
		if ( ! vplayer.infected()) return;
		if (vplayer.vampire()) return;
		
		// ... then we heal infection.
		vplayer.msg(Lang.infectionMessageHeal);
		vplayer.infectionAdd(Conf.infectionPerBread);
		player.getInventory().removeItem(new ItemStack(Material.BREAD, 1));
		player.updateInventory();
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void infection(EntityDamageEvent event)
	{
		// If this is a close combat event ...
		if ( ! MUtil.isCloseCombatEvent(event)) return;
		
		// ... where there is one vampire and one non-vampire ...
		VPlayer vpdamagee = VPlayers.i.get(event.getEntity());
		if (vpdamagee == null) return;
		VPlayer vpdamager = VPlayers.i.get(MUtil.getLiableDamager(event));
		if (vpdamager == null) return;
		
		VPlayer vampire = null;
		VPlayer human = null;
		
		if (vpdamagee.vampire())
		{
			vampire = vpdamagee;
		}
		else
		{
			human = vpdamagee;
		}
		
		if (vpdamager.vampire())
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
		if (MCore.random.nextDouble() > vampire.infectionGetRiskToInfectOther()) return;
		
		InfectionReason reason = vampire.intend() ? InfectionReason.COMBAT_INTENDED : InfectionReason.COMBAT_MISTAKE;
		human.infectionAdd(0.01D, reason, vampire);
	}
	
	// -------------------------------------------- //
	// BLOCK EVENTS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void blockEvents(EntityDamageEvent event)
	{
		Entity entity = event.getEntity();
		if ( ! (entity instanceof Player)) return;
		if ( ! Conf.blockDamageFrom.contains(event.getCause())) return;
		
		Player player = (Player)entity;
		VPlayer vplayer = VPlayers.i.get(player);
		
		if (vplayer.vampire()) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockEvents(EntityRegainHealthEvent event)
	{
		Entity entity = event.getEntity();
		if ( ! (entity instanceof Player)) return;
		if ( ! Conf.blockHealthFrom.contains(event.getRegainReason())) return;
		
		Player player = (Player) entity;		
		VPlayer vplayer = VPlayers.i.get(player);
		
		if (vplayer.vampire()) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockEvents(FoodLevelChangeEvent event)
	{
		Entity entity = event.getEntity();
		if ( ! (entity instanceof Player)) return;
		
		Player player = (Player) entity;		
		VPlayer vplayer = VPlayers.i.get(player);
		
		if (vplayer.vampire())
		{
			event.setCancelled(true);
			PlayerUtil.sendHealthFoodUpdatePacket(player);
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
		Conf.altarEvil.evalBlockUse(event.getClickedBlock(), event.getPlayer());
		Conf.altarGood.evalBlockUse(event.getClickedBlock(), event.getPlayer());
	}
	
	// -------------------------------------------- //
	// RESPAWN
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void respawn(PlayerRespawnEvent event)
	{
		// If the player is a vampire ...
		Player player = event.getPlayer();
		VPlayer vampire = VPlayers.i.get(player);
		if (vampire == null) return;
		if ( ! vampire.vampire()) return;
		
		// ... we apply the respawn logic.
		vampire.bloodlust(false);
		vampire.rad(0);
		vampire.temp(0);
		player.setFoodLevel(Conf.respawnFood);
		player.setHealth(Conf.respawnHealth);
		PlayerUtil.sendHealthFoodUpdatePacket(player);
	}
	
	// -------------------------------------------- //
	// REGEN
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void regen(EntityDamageEvent event)
	{
		// If the damagee is a vampire ...
		VPlayer vampire = VPlayers.i.get(event.getEntity());
		if (vampire == null) return;
		if ( ! vampire.vampire()) return;
		
		// ... mark now as lastDamageMillis
		vampire.lastDamageMillis(System.currentTimeMillis());
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
		VPlayer vplayer = VPlayers.i.get(player);
		if (vplayer.human()) return;
		
		// ... that has bloodlust on ...
		if ( ! vplayer.bloodlust()) return;
		
		// ... then spawn smoke trail.
		Location one = event.getFrom().clone();
		Location two = one.clone().add(0, 1, 0);
		long count1 = MUtil.probabilityRound(Conf.bloodlustSmokes);
		long count2 = MUtil.probabilityRound(Conf.bloodlustSmokes);
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
		VPlayer vplayer = VPlayers.i.get(player);
		vplayer.bloodlust(false);
	}
	
	// -------------------------------------------- //
	// FOOD
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void cake(PlayerInteractEvent event)
	{
		// If cake eating is not allowed for vampires ...
		if (Conf.foodCakeAllowed) return;
		
		// .. and the player right-clicks a cake block ...
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (event.getClickedBlock().getType() != Material.CAKE_BLOCK) return;
		
		// ... and the player is a vampire ...
		VPlayer vplayer = VPlayers.i.get(event.getPlayer());
		if ( ! vplayer.vampire()) return;
		
		// ... we deny!
		event.setCancelled(true);
		vplayer.msg(Lang.vampiresCantEatThat, "cake");
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void blood(EntityDamageEvent event)
	{
		// If this is a close combat event ...
		if ( ! MUtil.isCloseCombatEvent(event)) return;
		
		// ... to a living entity ...
		if ( ! (event.getEntity() instanceof LivingEntity)) return;
		LivingEntity damagee = (LivingEntity)event.getEntity();
		
		// ... of a tasty type ...
		Double fullFoodQuotient = Conf.entityTypeFullFoodQuotient.get(damagee.getType());
		if (fullFoodQuotient == null || fullFoodQuotient == 0) return;
		
		// ... that has blood left ...
		if (damagee.getHealth() < 0) return;
		if (damagee.isDead()) return;
		
		// ... and the liable damager is a vampire ...
		VPlayer vampire = VPlayers.i.get(MUtil.getLiableDamager(event));
		if (vampire == null) return;
		if ( ! vampire.vampire()) return;
		
		// ... drink blood! ;,,;
		double damage = event.getDamage();
		if (damagee.getHealth() < damage) damage = damagee.getHealth();
		double food = damage / damagee.getMaxHealth() * fullFoodQuotient * vampire.getPlayer().getMaxHealth();
		
		vampire.food().add(food);
	}
	
	// -------------------------------------------- //
	// TRUCE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void truce(EntityTargetEvent event)
	{
		// If a player is targeted...
		if ( ! (event.getTarget() instanceof Player)) return;
		
		// ... by creature that cares about the truce with vampires ...
		if ( ! (Conf.truceEntityTypes.contains(event.getEntity().getType()))) return;
		
		VPlayer vplayer = VPlayers.i.get((Player)event.getTarget());
		
		// ... and that player is a vampire ...
		if ( ! vplayer.vampire()) return;
		
		// ... that has not recently done something to break the truce...
		if (vplayer.truceIsBroken()) return;
		
		// Then the creature will not attack.
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void truce(EntityDamageEvent event)
	{
		// If this is a combat event ...
		if ( ! MUtil.isCombatEvent(event)) return;		
		
		// ... to a creature that cares about the truce with vampires...
		if ( ! (Conf.truceEntityTypes.contains(event.getEntity().getType()))) return;
		
		// ... and the liable damager is a vampire ...
		VPlayer vpdamager = VPlayers.i.get(MUtil.getLiableDamager(event));
		if (vpdamager == null) return;
		if ( ! vpdamager.vampire()) return;
		
		// Then that vampire broke the truce.
		vpdamager.truceBreak();
	}
	
	// -------------------------------------------- //
	// COMBAT
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void damageDeal(EntityDamageEvent event)
	{
		// If this is a close combat event ...
		if ( ! MUtil.isCloseCombatEvent(event)) return;
		
		// ... and the liable damager is a vampire ...
		VPlayer vampire = VPlayers.i.get(MUtil.getLiableDamager(event));
		if (vampire == null) return;
		if ( ! vampire.vampire()) return;
		
		// ... Then modify damage!
		double damage = event.getDamage();
		damage *= vampire.getDamageDealtFactor();
		event.setDamage((int) MUtil.probabilityRound(damage));
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void damageReceive(EntityDamageEvent event)
	{
		// If this is a close combat event ...
		if ( ! MUtil.isCloseCombatEvent(event)) return;
		
		// ... where the liable damager is a human entity ...
		Entity damagerEntity = MUtil.getLiableDamager(event);
		if ( ! (damagerEntity instanceof HumanEntity)) return;
		HumanEntity damager = (HumanEntity) damagerEntity;
		
		// ... and the damagee is a vampire ...
		VPlayer vampire = VPlayers.i.get(event.getEntity());
		if (vampire == null) return;
		if ( ! vampire.vampire()) return;
		
		// ... Then modify damage!
		double damage = event.getDamage();
		Material itemMaterial = damager.getItemInHand().getType();
		if (Conf.woodMaterials.contains(itemMaterial))
		{
			damage = Conf.damageReceivedWood; // Just as much as a diamond sword.
			vampire.msg(Lang.messageWoodCombatWarning, Txt.getMaterialName(itemMaterial));
		}
		else
		{
			damage *= vampire.getDamageReceivedFactor();
		}
		
		event.setDamage((int) MUtil.probabilityRound(damage));
	}
}
