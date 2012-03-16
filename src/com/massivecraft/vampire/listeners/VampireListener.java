package com.massivecraft.vampire.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore2.util.Txt;
import com.massivecraft.vampire.P;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.VPlayers;
import com.massivecraft.vampire.config.Conf;
import com.massivecraft.vampire.config.Lang;
import com.massivecraft.vampire.util.EntityUtil;

public class VampireListener implements Listener
{
	public P p;
	
	public VampireListener(P p)
	{
		this.p = p;
		Bukkit.getServer().getPluginManager().registerEvents(this, this.p);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		if (event.isCancelled()) return;
		Material material = event.getBlock().getType();
		if ( ! Conf.dropSelfOverrideMaterials.contains(material)) return;
		
		event.setCancelled(true);
		event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(material, 1));
		event.getBlock().setType(Material.AIR);
	}
	
	/**
	 * In this entity-damage-listener we will obtain blood,
	 * risk infections and break truce. Those things does
	 * never cancel or alter an event so they belong at monitor level.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamageMonitor(EntityDamageEvent event)
	{
		if (event.isCancelled()) return;
		
		// For further interest this must be a close combat attack by another entity
		if (event.getCause() != DamageCause.ENTITY_ATTACK && event.getCause() != DamageCause.PROJECTILE) return;
		if ( ! (event instanceof EntityDamageByEntityEvent)) return;
		
		// Define local fields
		Entity damagee = event.getEntity();
		Creature cDamagee;
		Player pDamagee;
		VPlayer vpDamagee;
		
		EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent)event;
		
		Entity damager = edbeEvent.getDamager();
		Player pDamager;
		VPlayer vpDamager;
		
		// Consider the damager to be the shooter if this is a projectile
		if (damager instanceof Projectile)
		{
			damager = ((Projectile)damager).getShooter();
		}
		
		// For further interest that attacker must be a player.
		if ( ! (damager instanceof Player)) return;
		
		pDamager = (Player)damager;
		vpDamager = VPlayers.i.get(pDamager);
		
		if (vpDamager.isVampire())
		{
			if (damagee instanceof Player)
			{
				// A True Blood vampire attacked a normal player. There is risk for infection.
				pDamagee = (Player)damagee;
				vpDamagee = VPlayers.i.get(pDamagee);
				
				vpDamagee.infectionContractRisk(vpDamager);
				
				// There will also be blood!
				if (pDamagee.getHealth() > 0)
				{
					int damage = event.getDamage();
					if (pDamagee.getHealth() < damage)
					{
						damage = pDamagee.getHealth();
					}
					vpDamager.foodAdd(damage * Conf.foodPerDamageFromPlayer);
					vpDamager.healthAdd(damage * Conf.healthPerDamageFromPlayer);
				}
			}
			else if (damagee instanceof Creature)
			{
				//A vampire attacked a creature
				cDamagee = (Creature)damagee;
				CreatureType creatureType = EntityUtil.creatureTypeFromEntity(damagee);
				
				// Obtain blood?
				if (Conf.foodPerDamageFromCreature.containsKey(creatureType) && cDamagee.getHealth() > 0)
				{
					int damage = event.getDamage();
					if (cDamagee.getHealth() < damage)
					{
						damage = cDamagee.getHealth();
					}
					vpDamager.foodAdd(damage * Conf.foodPerDamageFromCreature.get(creatureType));
					vpDamager.healthAdd(damage * Conf.healthPerDamageFromCreature.get(creatureType));
				}
				
				// Break truce
				if (Conf.creatureTypeTruceMonsters.contains(creatureType))
				{
					vpDamager.truceBreak();
				}
			}
		}
		else if (damagee instanceof Player)
		{
			pDamagee = (Player)damagee;
			vpDamagee = VPlayers.i.get(pDamagee);
			
			if (vpDamagee.isVampire())
			{
				vpDamager.infectionContractRisk(vpDamager);
			}
		}
	}
	
	/**
	 * In this entity-damage-listener we will cancel fall damage
	 * and suffocation damage for vampires. We will also modify the
	 * damage dealt.
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageHigh(EntityDamageEvent event)
	{
		if (event.isCancelled()) return;
		
		// Define local fields
		Entity damagee;
		Player pDamagee;
		VPlayer vpDamagee;
		
		EntityDamageByEntityEvent edbeEvent;
		
		Entity damager;
		Player pDamager;
		VPlayer vpDamager;
		
		damagee = event.getEntity();
		
		// If the damagee is a player
		if (damagee instanceof Player)
		{
			pDamagee = (Player)damagee;
			vpDamagee = VPlayers.i.get(pDamagee);
			
			// Vampires can not drown or take fall damage or starve
			if (vpDamagee.isVampire() && Conf.vampiresCantTakeDamageFrom.contains(event.getCause()))
			{
				event.setCancelled(true);
				return;
			}
		}
		
		// For further interest this must be a close combat attack by another entity
		if (event.getCause() != DamageCause.ENTITY_ATTACK) return;
		if ( ! (event instanceof EntityDamageByEntityEvent)) return;
		
		edbeEvent = (EntityDamageByEntityEvent)event;
		damager = edbeEvent.getDamager();
		
		// For further interest that attacker must be a player.
		if ( ! (damager instanceof Player)) return;
		pDamager = (Player)damager;
		vpDamager = VPlayers.i.get(pDamager);
		
		// The damage will be modified under certain circumstances.
		float damage = event.getDamage();
		
		// Modify damage if damager is a vampire
		if (vpDamager.isVampire())
		{
			damage *= vpDamager.getDamageDealtFactor();
		}
		
		// Modify damage if damagee is a vampire
		if (damagee instanceof Player)
		{
			pDamagee = (Player)damagee;
			vpDamagee = VPlayers.i.get(pDamagee);
			if (vpDamagee.isVampire())
			{
				Material itemMaterial = pDamager.getItemInHand().getType();
				if (Conf.woodMaterials.contains(itemMaterial))
				{
					damage = Conf.damageReceivedWood; // Just as much as a diamond sword.
					vpDamagee.msg(Lang.messageWoodCombatWarning, Txt.getMaterialName(itemMaterial));
				}
				else
				{
					damage *= vpDamagee.getDamageReceivedFactor();
				}
			}
		}
		
		event.setDamage(Math.round(damage));
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityRegainHealth(EntityRegainHealthEvent event)
	{
		if (event.isCancelled()) return;
		Entity entity = event.getEntity();
		if ( ! (entity instanceof Player)) return;
		if ( ! Conf.vampiresCantRegainHealthFrom.contains(event.getRegainReason())) return;
		Player player = (Player) entity;		
		VPlayer vplayer = VPlayers.i.get(player);
		if ( ! vplayer.isVampire()) return;
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onFoodLevelChange(FoodLevelChangeEvent event)
	{
		if (event.isCancelled()) return;
		if (Conf.vampiresLooseFoodNaturally) return;
		Entity entity = event.getEntity();
		if ( ! (entity instanceof Player)) return;
		Player player = (Player) entity;		
		VPlayer vplayer = VPlayers.i.get(player);
		if ( ! vplayer.isVampire()) return;
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityTarget(EntityTargetEvent event)
	{
		if (event.isCancelled()) return;
		
		// If a player is targeted...
		if ( ! (event.getTarget() instanceof Player)) return;
		
		// ... by creature that cares about the truce with vampires ...
		if ( ! (Conf.creatureTypeTruceMonsters.contains(EntityUtil.creatureTypeFromEntity(event.getEntity())))) return;
		
		VPlayer vplayer = VPlayers.i.get((Player)event.getTarget());
		
		// ... and that player is a vampire ...
		if ( ! vplayer.isVampire()) return;
		
		// ... that has not recently done something to break the truce...
		if (vplayer.truceIsBroken()) return;
		
		// Then the creature will not attack.
		event.setCancelled(true);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		if ( ! (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) )  return;
		
		Player player = event.getPlayer();
		VPlayer vplayer = VPlayers.i.get(player);
		Material itemMaterial = event.getMaterial();
		
		if(vplayer.isVampire())
		{
			
			if ( Conf.foodMaterials.contains(itemMaterial) && ! Conf.vampireCanEat(itemMaterial))
			{
				vplayer.msg(Lang.vampiresCantEatThat, Txt.getMaterialName(itemMaterial));
				event.setCancelled(true);
			}
			
			if (action == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CAKE_BLOCK && Conf.vampireCanEat(Material.CAKE_BLOCK))
			{
				vplayer.msg(Lang.vampiresCantEatThat, Txt.getMaterialName(Material.CAKE));
				event.setCancelled(true);
			}
				
			if (Conf.jumpMaterials.contains(event.getMaterial())) 
			{
				vplayer.jump(Conf.jumpDeltaSpeed, false);
			}
		}
		
		if (vplayer.isInfected() && itemMaterial == Material.BREAD)
		{
			vplayer.infectionHeal(Conf.infectionBreadHealAmount);
			player.getInventory().removeItem(new ItemStack(Material.BREAD, 1));
			player.updateInventory();
			event.setCancelled(true);
		}		
		
		if ( action != Action.RIGHT_CLICK_BLOCK) return;
		Conf.altarEvil.evalBlockUse(event.getClickedBlock(), player);
		Conf.altarGood.evalBlockUse(event.getClickedBlock(), player);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChat(PlayerChatEvent event)
	{		
		if (event.isCancelled()) return;
		
		Player me = event.getPlayer();
		VPlayer vme = VPlayers.i.get(me);
		
		if (Conf.nameColorize == false) return;
		if ( ! vme.isVampire()) return;
		
		me.setDisplayName(""+Conf.nameColor+ChatColor.stripColor(me.getDisplayName()));
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		VPlayer vplayer = VPlayers.i.get(player);
		vplayer.updateVampPermission();
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerAnimation(PlayerAnimationEvent event)
	{
		VPlayer vplayer = VPlayers.i.get(event.getPlayer());
		if ( ! vplayer.isVampire()) return;
			
		if (event.getAnimationType() == PlayerAnimationType.ARM_SWING && Conf.jumpMaterials.contains(event.getPlayer().getItemInHand().getType()))
		{
			vplayer.jump(Conf.jumpDeltaSpeed, true);
		}
	}
}
