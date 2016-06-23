package com.massivecraft.vampire.entity;

import com.massivecraft.massivecore.store.Coll;

public class UConfColl extends Coll<UConf>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public UConfColl(String id)
	{
		super(id);
		this.setCreative(true);
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
