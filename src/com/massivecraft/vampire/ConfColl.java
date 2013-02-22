package com.massivecraft.vampire;

import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.MStore;

public class ConfColl extends Coll<Conf, String>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ConfColl(String name)
	{
		super(MStore.getDb(ConfServer.dburi), P.p, "ai", name, Conf.class, String.class, true);
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
}
