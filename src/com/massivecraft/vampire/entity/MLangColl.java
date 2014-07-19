package com.massivecraft.vampire.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;
import com.massivecraft.vampire.Const;
import com.massivecraft.vampire.Vampire;

public class MLangColl extends Coll<MLang>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MLangColl i = new MLangColl();
	public static MLangColl get() { return i; }
	private MLangColl()
	{
		super(Const.COLLECTION_MLANG, MLang.class, MStore.getDb(), Vampire.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void init()
	{
		super.init();
		MLang.i = this.get(MassiveCore.INSTANCE, true);
	}
	
}
