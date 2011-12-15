package com.massivecraft.vampire;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import com.massivecraft.mcore1.Predictate;
import com.massivecraft.mcore1.lib.gson.reflect.TypeToken;
import com.massivecraft.mcore1.persist.gson.GsonPlayerEntityManager;
import com.massivecraft.mcore1.util.DiscUtil;

public class VPlayers extends GsonPlayerEntityManager<VPlayer>
{
	public static VPlayers i = new VPlayers();
	
	private VPlayers()
	{
		super(P.p.gson, new File(P.p.getDataFolder(), "player"), true, true);
		P.p.persist.setManager(VPlayer.class, this);
		P.p.persist.setSaveInterval(VPlayer.class, 1000*60*30);
	}

	@Override
	public Class<VPlayer> getManagedClass()
	{
		return VPlayer.class;
	}
	
	
	public Collection<VPlayer> getAllOnlineInfected()
	{
		return this.getAll(new Predictate<VPlayer>()
		{
			public boolean apply(VPlayer entity)
			{
				return entity.isOnline() && entity.isInfected();
			}
		});
	}
	
	public Collection<VPlayer> getAllOnlineVampires()
	{
		return this.getAll(new Predictate<VPlayer>()
		{
			public boolean apply(VPlayer entity)
			{
				return entity.isOnline() && entity.isVampire();
			}
		});
	}
	
	@Override
	public boolean shouldBeSaved(VPlayer entity)
	{
		return entity.isExvampire() || entity.isVampire() || entity.isInfected();
	}
	
	public void loadOldFormat()
	{
		File file = new File(P.p.getDataFolder(), "player.json");
		if ( ! file.exists()) return;
		
		P.p.log("Converting players to new file format...");
		
		Type type = new TypeToken<Map<String, VPlayer>>(){}.getType();
		String json = DiscUtil.readCatch(file);
		Map<String, VPlayer> id2vplayers = P.p.gson.fromJson(json, type);
		
		for (Entry<String, VPlayer> entry : id2vplayers.entrySet())
		{
			String id = entry.getKey();
			VPlayer vplayer = entry.getValue();
			VPlayers.i.attach(vplayer, id);
		}
		
		VPlayers.i.saveAll();
		
		file.renameTo(new File(P.p.getDataFolder(), "player.json.old"));
		
		P.p.log("... done");
	}
}
