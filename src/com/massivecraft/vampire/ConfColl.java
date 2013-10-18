package com.massivecraft.vampire;

import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.MStore;

public class ConfColl extends Coll<Conf>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ConfColl(String name)
	{
		super(name, Conf.class, MStore.getDb(ConfServer.dburi), P.p, false, true, false);
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
}
