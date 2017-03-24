package com.massivecraft.vampire.entity;

import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.store.SenderColl;

import java.util.Collection;

public class UPlayerColl extends SenderColl<UPlayer>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public UPlayerColl(String id)
	{
		super(id);
	}

	// -------------------------------------------- //
	// STACK TRACEABILITY
	// -------------------------------------------- //
	
	@Override
	public void onTick()
	{
		super.onTick();
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
		return this.getAll(new Predicate<UPlayer>()
		{
			public boolean apply(UPlayer entity)
			{
				return entity.isOnline() && entity.isInfected();
			}
		});
	}
	
	public Collection<UPlayer> getAllOnlineVampires()
	{
		return this.getAll(new Predicate<UPlayer>()
		{
			public boolean apply(UPlayer entity)
			{
				return entity.isOnline() && entity.isVampire();
			}
		});
	}
	
}
