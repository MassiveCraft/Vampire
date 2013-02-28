package com.massivecraft.vampire;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventPriority;

import com.massivecraft.mcore.integration.protocollib.MCoreEntityPotionColorEvent;

public class PotionEffectConf
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public EventPriority priority;
	
	public boolean colorSet;
	public int colorValue;
	
	public Map<Integer, Integer> effectToStrength;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PotionEffectConf(EventPriority priority, boolean colorSet, int colorValue, Map<Integer, Integer> effectToStrength)
	{
		this.priority = priority;
		
		this.colorSet = colorSet;
		this.colorValue = colorValue;
		
		this.effectToStrength = effectToStrength;
	}
	
	public PotionEffectConf()
	{
		this(EventPriority.NORMAL, false, 0, new HashMap<Integer, Integer>());
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public void applyFor(MCoreEntityPotionColorEvent event, EventPriority eventPriority)
	{
		if (this.colorSet == false) return;
		if (this.priority != eventPriority) return;
		event.setColor(this.colorValue);
	}
}
