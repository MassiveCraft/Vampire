package com.massivecraft.vampire;

import java.util.Collection;

import com.massivecraft.mcore4.Predictate;
import com.massivecraft.mcore4.store.MStore;
import com.massivecraft.mcore4.store.PlayerColl;

public class VPlayers extends PlayerColl<VPlayer>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	public static VPlayers i = new VPlayers();
	
	private VPlayers()
	{
		super(MStore.getDb(Conf.dburi), P.p, "vampire_player", VPlayer.class);
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	@Override
	public boolean isDefault(VPlayer entity)
	{
		if (entity.vampire()) return false;
		if (entity.infected()) return false;
		return true;
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
	
	
	
	/*
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
	}*/
}
