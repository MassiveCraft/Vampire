package com.massivecraft.vampire;

import java.io.File;
import java.util.Collection;

import com.massivecraft.mcore1.Predictate;
import com.massivecraft.mcore1.persist.gson.GsonPlayerEntityManager;

public class VPlayers extends GsonPlayerEntityManager<VPlayer>
{
	public static VPlayers i;
	
	public VPlayers()
	{
		super(P.p.gson, new File(P.p.getDataFolder(), "player"), true, true);
		i = this;
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
	
	/*
	 * 
	Use for the converter later
	@Override
	public Type getMapType()
	{
		return new TypeToken<Map<String, VPlayer>>(){}.getType();
	}*/
}
