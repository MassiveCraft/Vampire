package com.massivecraft.vampire;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import com.massivecraft.mcore.EngineAbstract;
import com.massivecraft.mcore.event.MCoreUuidUpdateEvent;
import com.massivecraft.mcore.util.IdUpdateUtil;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.vampire.entity.UPlayer;
import com.massivecraft.vampire.entity.UPlayerColl;
import com.massivecraft.vampire.entity.UPlayerColls;

public class EngineIdUpdate extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineIdUpdate i = new EngineIdUpdate();
	public static EngineIdUpdate get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return Vampire.get();
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR)
	public void updateUplayerMakerId(MCoreUuidUpdateEvent event)
	{
		for (UPlayerColl coll : UPlayerColls.get().getColls())
		{
			IdUpdateUtil.update(coll);
			
			for (UPlayer entity : coll.getAll())
			{
				updateUplayerMakerId(entity);
			}
		}
	}
	
	public static void updateUplayerMakerId(UPlayer entity)
	{
		// Before and After
		String before = entity.getMakerId();
		if (before == null) return;
		String after = IdUpdateUtil.update(before, false);
		if (after == null) return;
		
		// NoChange
		if (MUtil.equals(before, after)) return;
		
		// Apply
		entity.setMakerId(after);
		entity.changed();
		entity.sync();
	}

}
