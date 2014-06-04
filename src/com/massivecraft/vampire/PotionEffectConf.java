package com.massivecraft.vampire;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.massivecore.integration.protocollib.EventMassiveCoreEntityPotionColor;

@SuppressWarnings("deprecation")
public class PotionEffectConf
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public EventPriority priority;
	
	public boolean colorSet;
	public int colorValue;
	
	public Map<Integer, Integer> effectIdToStrength;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PotionEffectConf(EventPriority priority, boolean colorSet, int colorValue, Map<Integer, Integer> effectToStrength)
	{
		this.priority = priority;
		
		this.colorSet = colorSet;
		this.colorValue = colorValue;
		
		this.effectIdToStrength = effectToStrength;
	}
	
	public PotionEffectConf()
	{
		this(EventPriority.NORMAL, false, 0, new HashMap<Integer, Integer>());
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public void applyFor(EventMassiveCoreEntityPotionColor event, EventPriority eventPriority)
	{
		if (this.colorSet == false) return;
		if (this.priority != eventPriority) return;
		event.setColor(this.colorValue);
	}
	
	public void addPotionEffects(LivingEntity entity, int targetDuration, int okDuration)
	{
		Map<Integer, Integer> effectIdToStrength = new HashMap<Integer, Integer>(this.effectIdToStrength);
		
		for (PotionEffect pe : entity.getActivePotionEffects())
		{
			// We may update if the duration is below the ok duration
			if (pe.getDuration() < okDuration) continue;
			
			// Is there even an entry to remove?
			int id = pe.getType().getId();
			Integer peStrength = effectIdToStrength.get(id);
			if (peStrength == null) continue;
			if (peStrength < 1) continue;
			
			// We may update if the strength is different
			int apeStrength = pe.getAmplifier();
			if (peStrength != apeStrength) continue;
			
			// Otherwise we may not
			effectIdToStrength.remove(id);
		}
		
		for (Entry<Integer, Integer> entry : effectIdToStrength.entrySet())
		{
			PotionEffectType pet = PotionEffectType.getById(entry.getKey());
			Integer strength = entry.getValue();
			
			entity.addPotionEffect(new PotionEffect(pet, targetDuration, strength), true);
		}
	}
	
	public void removePotionEffects(LivingEntity entity)
	{
		// The ids to deactivate
		Set<Integer> ids = new HashSet<Integer>(this.effectIdToStrength.keySet());
	
		// The currently active ids 
		Set<Integer> activeIds = new HashSet<Integer>();
		for (PotionEffect pe : entity.getActivePotionEffects())
		{
			activeIds.add(pe.getType().getId());
		}
		
		// It only makes sense to deactivate active ids.
		Iterator<Integer> iter = ids.iterator();
		while(iter.hasNext())
		{
			Integer id = iter.next();
			if (activeIds.contains(id)) continue;
			iter.remove();
		}
		
		// Perform the remove
		for (Integer id : ids)
		{
			entity.removePotionEffect(PotionEffectType.getById(id));
		}
	}
	
}
