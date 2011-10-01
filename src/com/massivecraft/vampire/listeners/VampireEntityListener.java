package com.massivecraft.vampire.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;

import com.massivecraft.vampire.P;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.VPlayers;
import com.massivecraft.vampire.config.Conf;
import com.massivecraft.vampire.config.Lang;
import com.massivecraft.vampire.util.EntityUtil;
import com.massivecraft.vampire.zcore.util.TextUtil;

//import com.bukkit.mcteam.vampire.Vampire;

public class VampireEntityListener extends EntityListener
{
	public P p;
	
	public VampireEntityListener(P p)
	{
		this.p = p;
	}
	
	
	/**
	 * In this entity-damage-listener we will cancel fall damage
	 * and suffocation damage for vampires. We will also modify the
	 * damage dealt.
	 */
	@Override
	public void onEntityDamage(EntityDamageEvent event)
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
			
			// Vampires can not drown or take fall damage.
			if (vpDamagee.isVampire() && (event.getCause() == DamageCause.DROWNING || event.getCause() == DamageCause.FALL))
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
					vpDamagee.msg(p.txt.parse(Lang.messageWoodCombatWarning, TextUtil.getMaterialName(itemMaterial)));
				}
				else
				{
					damage *= vpDamagee.getDamageReceivedFactor();
				}
			}
		}
		
		event.setDamage(Math.round(damage));
	}
	
	@Override
	public void onEntityTarget(EntityTargetEvent event) {
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

}














