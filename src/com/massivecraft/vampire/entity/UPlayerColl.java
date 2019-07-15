package com.massivecraft.vampire.entity;

import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.massivecore.store.cleanable.CleaningUtil;

import java.util.Collection;

public class UPlayerColl extends SenderColl<UPlayer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static UPlayerColl i = new UPlayerColl();
	public static UPlayerColl get() { return i; }

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
		return this.getAll(entity -> entity.isOnline() && entity.isInfected());
	}
	
	public Collection<UPlayer> getAllOnlineVampires()
	{
		return this.getAll(entity -> entity.isOnline() && entity.isVampire());
	}
	
	// -------------------------------------------- //
	// CLEAN
	// -------------------------------------------- //
	
	@Override
	public long getCleanInactivityToleranceMillis()
	{
		return CleaningUtil.CLEAN_INACTIVITY_TOLERANCE_MILLIS_STANDARD;
	}
	
}
