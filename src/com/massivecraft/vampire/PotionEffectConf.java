package com.massivecraft.vampire;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@SuppressWarnings("deprecation")
public class PotionEffectConf
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public EventPriority priority;
	
	public boolean colorSet;
	public int colorValue;
	
	public Map<PotionEffectType, Integer> effectToStrength;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PotionEffectConf(EventPriority priority, boolean colorSet, int colorValue, Map<PotionEffectType, Integer> effectToStrength)
	{
		this.priority = priority;
		
		this.colorSet = colorSet;
		this.colorValue = colorValue;
		
		this.effectToStrength = effectToStrength;
	}
	
	public PotionEffectConf()
	{
		this(EventPriority.NORMAL, false, 0, new HashMap<PotionEffectType, Integer>());
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public void addPotionEffects(LivingEntity entity, int targetDuration, int okDuration)
	{
		Map<PotionEffectType, Integer> effectToStrength = new HashMap<>(this.effectToStrength);
		
		for (PotionEffect pe : entity.getActivePotionEffects())
		{
			// We may update if the duration is below the ok duration
			if (pe.getDuration() < okDuration) continue;
			
			// Is there even an entry to remove?
			PotionEffectType effect = pe.getType();
			Integer peStrength = effectToStrength.get(effect);
			if (peStrength == null) continue;
			if (peStrength < 1) continue;
			
			// We may update if the strength is different
			int apeStrength = pe.getAmplifier();
			if (peStrength != apeStrength) continue;
			
			// Otherwise we may not
			effectToStrength.remove(effect);
		}
		
		for (Entry<PotionEffectType, Integer> entry : effectToStrength.entrySet())
		{
			entity.addPotionEffect(new PotionEffect(entry.getKey(), targetDuration, entry.getValue(), true), true);
		}
	}
	
	public void removePotionEffects(LivingEntity entity)
	{
		// The ids to deactivate
		Set<PotionEffectType> effects = new HashSet<>(this.effectToStrength.keySet());
	
		// The currently active ids 
		Set<PotionEffectType> activeEffects = new HashSet<>();
		for (PotionEffect pe : entity.getActivePotionEffects())
		{
			activeEffects.add(pe.getType());
		}
		
		// It only makes sense to deactivate active ids.
		Iterator<PotionEffectType> iter = effects.iterator();
		while(iter.hasNext())
		{
			PotionEffectType effect = iter.next();
			if (activeEffects.contains(effect)) continue;
			iter.remove();
		}
		
		// Perform the remove
		for (PotionEffectType effect : effects)
		{
			entity.removePotionEffect(effect);
		}
	}
	
}
