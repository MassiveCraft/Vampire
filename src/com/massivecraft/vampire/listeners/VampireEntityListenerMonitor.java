package com.massivecraft.vampire.listeners;

import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;

import com.massivecraft.vampire.P;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.VPlayers;
import com.massivecraft.vampire.config.*;
import com.massivecraft.vampire.util.EntityUtil;


public class VampireEntityListenerMonitor extends EntityListener
{
	public P p;
	
	public VampireEntityListenerMonitor(P p)
	{
		this.p = p;
	}
	
	/**
	 * In this entity-damage-listener we will obtain blood,
	 * risk infections and break truce. Those things does
	 * never cancel or alter an event so they belong at monitor level.
	 */
	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.isCancelled()) return;
		
		// For further interest this must be a close combat attack by another entity
		if (event.getCause() != DamageCause.ENTITY_ATTACK) return;
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
}














