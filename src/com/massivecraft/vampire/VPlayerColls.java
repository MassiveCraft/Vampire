package com.massivecraft.vampire;

import com.massivecraft.mcore4.store.Colls;
import com.massivecraft.mcore4.store.Db;
import com.massivecraft.mcore4.store.MStore;

public class VPlayerColls extends Colls<VPlayerColl, VPlayer, String>
{
	public static VPlayerColls i = new VPlayerColls();
	
	@Override
	public Db<?> getDb()
	{
		return MStore.getDb(ConfServer.dburi);
	}
	
	@Override
	public VPlayerColl createColl(String collName)
	{
		VPlayerColl ret = new VPlayerColl(collName);
		ret.init();
		return ret;
	}

	@Override
	public String getContext()
	{
		return ConfServer.playerContext;
	}
}
