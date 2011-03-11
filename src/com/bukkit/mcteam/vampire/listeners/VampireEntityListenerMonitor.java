package com.bukkit.mcteam.vampire.listeners;

import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;
import com.bukkit.mcteam.util.EntityUtil;

import com.bukkit.mcteam.vampire.Conf;
import com.bukkit.mcteam.vampire.VPlayer;

public class VampireEntityListenerMonitor extends EntityListener {
	
	/**
	 * In this entity-damage-listener we will obtain blood,
	 * risk infections and break truce. Those things does
	 * never cancel or alter an event so they belong at monitor level.
	 */
	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		// For further interest this must be a close combat attack by another entity
		if (event.getCause() != DamageCause.ENTITY_ATTACK) {
			return;
		}
		if ( ! (event instanceof EntityDamageByEntityEvent)) {
			return;
		}
		if (event instanceof EntityDamageByProjectileEvent) {
			return;
		}
		
		// Define local fields
		Entity damagee = event.getEntity();
		Creature cDamagee;
		Player pDamagee;
		VPlayer vpDamagee;
		
		EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent)event;
		
		Entity damager = edbeEvent.getDamager();
		Player pDamager;
		VPlayer vpDamager;
		
		// For further interest that attacker must be a player.
		if ( ! (damager instanceof Player)) {
			return;
		}
		
		pDamager = (Player)damager;
		vpDamager = VPlayer.get(pDamager);
		
		if (vpDamager.isVampire()) {
			if (damagee instanceof Player) {
				// A vampire attacked a normal player. There is risk for infection.
				pDamagee = (Player)damagee;
				vpDamagee = VPlayer.get(pDamagee);
				vpDamagee.infectionRisk();
				
				// There will also be blood!
				if (pDamagee.getHealth() > 0) {
					int damageForBlood = event.getDamage();
					if (pDamagee.getHealth() < damageForBlood) {
						damageForBlood = pDamagee.getHealth();
					}
					vpDamager.bloodDrink(damageForBlood * Conf.playerBloodQuality, pDamagee.getDisplayName());
				}
			} else if (damagee instanceof Creature) {
				//A vampire attacked a creature
				cDamagee = (Creature)damagee;
				CreatureType creatureType = EntityUtil.creatureTypeFromEntity(damagee);
				
				// Obtain blood?
				if (Conf.creatureTypeBloodQuality.containsKey(creatureType) && cDamagee.getHealth() > 0) {
					int damageForBlood = event.getDamage();
					if (cDamagee.getHealth() < damageForBlood) {
						damageForBlood = cDamagee.getHealth();
					}
					vpDamager.bloodDrink(damageForBlood * Conf.creatureTypeBloodQuality.get(creatureType), "the "+creatureType.getName().toLowerCase());
				}
				
				// Break truce
				if (Conf.creatureTypeTruceMonsters.contains(creatureType)) {
					vpDamager.truceBreak();
				}
			}
		} else if (damagee instanceof Player) {
			pDamagee = (Player)damagee;
			vpDamagee = VPlayer.get(pDamagee);
			
			if (vpDamagee.isVampire()) {
				// A non vampire attacked a vampire. There is risk for infection.
				vpDamager.infectionRisk();
			}
		}
	}
	
	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		// Set the blood for vampires to 100
		Entity entity = event.getEntity();
		if ( ! (entity instanceof Player)) {
			return;
		}
		VPlayer vplayer = VPlayer.get((Player)entity);
		vplayer.bloodSet(100); // No fun to be reborn thirsty... so we reset it. 
	}
}














