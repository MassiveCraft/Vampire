package com.massivecraft.vampire.entity;

import java.util.Collection;

import com.massivecraft.massivecore.Predictate;
import com.massivecraft.massivecore.store.MStore;
import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.vampire.Vampire;

public class UPlayerColl extends SenderColl<UPlayer>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public UPlayerColl(String name)
	{
		super(name, UPlayer.class, MStore.getDb(), Vampire.get());
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //

	@Override
	public boolean isDefault(UPlayer entity)
	{
		if (entity.isVampire()) return false;
		if (entity.isInfected()) return false;
		return true;
	}
	
	
	public Collection<UPlayer> getAllOnlineInfected()
	{
		return this.getAll(new Predictate<UPlayer>()
		{
			public boolean apply(UPlayer entity)
			{
				return entity.isOnline() && entity.isInfected();
			}
		});
	}
	
	public Collection<UPlayer> getAllOnlineVampires()
	{
		return this.getAll(new Predictate<UPlayer>()
		{
			public boolean apply(UPlayer entity)
			{
				return entity.isOnline() && entity.isVampire();
			}
		});
	}
	
}
