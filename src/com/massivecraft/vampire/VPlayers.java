package com.massivecraft.vampire;

import java.io.File;
import java.util.Collection;

import com.massivecraft.mcore4.MCore;
import com.massivecraft.mcore4.Predictate;
import com.massivecraft.mcore4.store.DbGson;
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
	
	@Override
	public void init()
	{
		this.migrateFromOldFormat();
		super.init();
	}
	
	protected void migrateFromOldFormat()
	{
		File oldPlayerCollDir = new File(P.p.getDataFolder(), "player");
		if (!oldPlayerCollDir.isDirectory()) return;
		
		if (MCore.getDb().driver().name() != "gson") return;
		DbGson dbGson = (DbGson) MCore.getDb();
		File newPlayerCollDir = new File(dbGson.dir, this.name());
		if (newPlayerCollDir.isDirectory()) return;
		
		dbGson.dir.mkdirs();
		oldPlayerCollDir.renameTo(newPlayerCollDir);
	}
}
