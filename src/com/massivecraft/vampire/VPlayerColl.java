package com.massivecraft.vampire;

import java.io.File;
import java.util.Collection;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.Predictate;
import com.massivecraft.mcore.store.DbGson;
import com.massivecraft.mcore.store.MStore;
import com.massivecraft.mcore.store.SenderColl;

public class VPlayerColl extends SenderColl<VPlayer>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public VPlayerColl(String name)
	{
		super(name, VPlayer.class, MStore.getDb(ConfServer.dburi), P.p);
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	@Override
	public boolean isDefault(VPlayer entity)
	{
		if (entity.isVampire()) return false;
		if (entity.isInfected()) return false;
		return true;
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
	public void init()
	{
		this.migrateFromOldFormat();
		super.init();
	}
	
	protected void migrateFromOldFormat()
	{
		File oldPlayerCollDir = new File(P.p.getDataFolder(), "player");
		if (!oldPlayerCollDir.isDirectory()) return;
		
		if (MCore.getDb().getDriver().getName() != "gson") return;
		DbGson dbGson = (DbGson) MCore.getDb();
		File newPlayerCollDir = new File(dbGson.dir, this.getName());
		if (newPlayerCollDir.isDirectory()) return;
		
		dbGson.dir.mkdirs();
		oldPlayerCollDir.renameTo(newPlayerCollDir);
	}
}
