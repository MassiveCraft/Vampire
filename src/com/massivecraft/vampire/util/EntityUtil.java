package com.massivecraft.vampire.util;

import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
// TODO investigate if this is still needed...
public class EntityUtil
{
	public static CreatureType creatureTypeFromEntity(Entity entity)
	{
		if ( ! (entity instanceof Creature))
		{
			return null;
		}
		
		String name = entity.getClass().getSimpleName();
		name = name.substring(5); // Remove "Craft"
		
		return CreatureType.fromName(name);
	}
}
