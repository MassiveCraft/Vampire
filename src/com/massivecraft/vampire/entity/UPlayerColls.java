package com.massivecraft.vampire.entity;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.store.Colls;
import com.massivecraft.vampire.Const;
import com.massivecraft.vampire.Vampire;

public class UPlayerColls extends Colls<UPlayerColl, UPlayer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static UPlayerColls i = new UPlayerColls();
	public static UPlayerColls get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: COLLS
	// -------------------------------------------- //
	
	@Override
	public UPlayerColl createColl(String collName)
	{
		return new UPlayerColl(collName);
	}

	@Override
	public Aspect getAspect()
	{
		return Vampire.get().playerAspect;
	}

	@Override
	public String getBasename()
	{
		return Const.COLLECTION_UPLAYER;
	}
	
}
