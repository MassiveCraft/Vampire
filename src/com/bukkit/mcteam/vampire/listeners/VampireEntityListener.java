package com.bukkit.mcteam.vampire.listeners;

import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;
import com.bukkit.mcteam.util.*;

import com.bukkit.mcteam.vampire.Conf;
import com.bukkit.mcteam.vampire.VPlayer;
//import com.bukkit.mcteam.vampire.Vampire;

public class VampireEntityListener extends EntityListener {
	
	//TODO make this look better. Possibly document the crap :P
	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		// Define local fields
		Entity damagee;
		Creature cDamagee;
		Player pDamagee;
		VPlayer vpDamagee;
		
		EntityDamageByEntityEvent edbeEvent;
		
		Entity damager;
		Player pDamager;
		VPlayer vpDamager;
		
		damagee = event.getEntity();
		
		// If the damagee is a player
		if (damagee instanceof Player) {
			//Vampire.log("damagee instanceof Player");
			pDamagee = (Player)damagee;
			vpDamagee = VPlayer.get(pDamagee);
			
			// Vampires can not drown or take fall damage.
			if (vpDamagee.isVampire() && (event.getCause() == DamageCause.DROWNING || event.getCause() == DamageCause.FALL)) {
				//Vampire.log("Vampires can not drown or take fall damage");
				event.setCancelled(true);
				return;
			}
			
			// Add delay to regeneration ability
			if (vpDamagee.isVampire()) {
				vpDamagee.regenDelayLeftMilliseconds = Conf.regenStartDelayMilliseconds;
			}
		}
	
		// For further interest there must be an attacking entity
		if (event.getCause() != DamageCause.ENTITY_ATTACK || ! (event instanceof EntityDamageByEntityEvent)) {
			return;
		}
		edbeEvent = (EntityDamageByEntityEvent)event;
		damager = edbeEvent.getDamager();
		
		// For further interest that attacker must be a player.
		if ( ! (damager instanceof Player)) {
			return;
		}
		pDamager = (Player)damager;
		vpDamager = VPlayer.get(pDamager);
		
		// The damage will be modified under certain circumstances.
		float damage = event.getDamage();
		
		// Modify damage if damager is a vampire
		if (vpDamager.isVampire()) {
			damage *= Conf.combatDamageDealtFactor;
		}
		
		// Modify damage if damagee is a vampire
		if (damagee instanceof Player) {
			pDamagee = (Player)damagee;
			vpDamagee = VPlayer.get(pDamagee);
			if (vpDamagee.isVampire()) {
				if (Conf.woodMaterials.contains(pDamager.getItemInHand().getType())) {
					damage *= Conf.combatDamageReceivedWoodFactor;
					vpDamagee.sendMessage(Conf.messageWoodCombatWarning);
				} else {
					damage *= Conf.combatDamageReceivedFactor;
				}
			}
		}
		
		event.setDamage(Math.round(damage));
		
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
	public void onEntityTarget(EntityTargetEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		// If a player is targeted...
		if ( ! (event.getTarget() instanceof Player)) {
			return;
		}
		
		// ... by creature that cares about the truce with vampires ...
		if ( ! (Conf.creatureTypeTruceMonsters.contains(EntityUtil.creatureTypeFromEntity(event.getEntity())))) {
			return;
		}
		
		VPlayer vplayer = VPlayer.get((Player)event.getTarget());
		
		// ... and that player is a vampire ...
		if ( ! vplayer.isVampire()) {
			return;
		}
		
		// ... that has not recently done something to break the truce...
		if (vplayer.truceIsBroken()) {
			return;
		}
		
		// Then the creature will not attack.
		event.setCancelled(true);
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














