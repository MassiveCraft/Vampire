package com.massivecraft.vampire;

import com.massivecraft.mcore4.store.Coll;
import com.massivecraft.mcore4.store.MStore;

public class ConfColl extends Coll<Conf, String>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ConfColl(String name)
	{
		super(MStore.getDb(ConfServer.dburi), P.p, "ai", name, Conf.class, String.class, false);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		// Ensure the instance exist
		this.get(Conf.INSTANCE, true);
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
}
