package com.massivecraft.vampire.entity;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;
import com.massivecraft.vampire.Vampire;

public class UConfColl extends Coll<UConf>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public UConfColl(String name)
	{
		super(name, UConf.class, MStore.getDb(), Vampire.get(), true, false);
	}

	// -------------------------------------------- //
	// STACK TRACEABILITY
	// -------------------------------------------- //
	
	@Override
	public void onTick()
	{
		super.onTick();
	}
	
}
