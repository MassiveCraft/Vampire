package com.massivecraft.vampire;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.reflect.TypeToken;
import com.massivecraft.mcore2.Predictate;
import com.massivecraft.mcore2.persist.gson.GsonPlayerEntityManager;
import com.massivecraft.mcore2.util.DiscUtil;

public class VPlayers extends GsonPlayerEntityManager<VPlayer>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	public static VPlayers i = new VPlayers();
	
	private VPlayers()
	{
		super(P.p.gson, new File(P.p.getDataFolder(), "player"), true, false);
		P.p.persist.setManager(VPlayer.class, this);
		P.p.persist.setSaveInterval(VPlayer.class, 1000*60*30);
	}

	@Override
	public Class<VPlayer> getManagedClass() { return VPlayer.class; }
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	@Override
	public boolean shouldBeSaved(VPlayer entity)
	{
		return entity.vampire() || entity.infected();
	}
	
	public Collection<VPlayer> getAllOnlineInfected()
	{
		return this.getAll(new Predictate<VPlayer>()
		{
			public boolean apply(VPlayer entity)
			{
				return entity.isOnline() && entity.infected();
			}
		});
	}
	
	public Collection<VPlayer> getAllOnlineVampires()
	{
		return this.getAll(new Predictate<VPlayer>()
		{
			public boolean apply(VPlayer entity)
			{
				return entity.isOnline() && entity.vampire();
			}
		});
	}
	
	public void loadOldFormat()
	{
		File file = new File(P.p.getDataFolder(), "player.json");
		if ( ! file.exists()) return;
		
		P.p.log("Converting "+this.getManagedClass().getSimpleName()+" to new file format...");
		
		Type type = new TypeToken<Map<String, VPlayer>>(){}.getType();
		String json = DiscUtil.readCatch(file);
		Map<String, VPlayer> id2entity = P.p.gson.fromJson(json, type);
		
		for (Entry<String, VPlayer> entry : id2entity.entrySet())
		{
			String id = entry.getKey();
			VPlayer entity = entry.getValue();
			i.attach(entity, id);
		}
		
		i.saveAll();
		
		file.renameTo(new File(P.p.getDataFolder(), "player.json.old"));
		
		P.p.log("... done");
	}
}
