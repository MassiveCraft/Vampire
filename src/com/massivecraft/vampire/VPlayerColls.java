package com.massivecraft.vampire;

import com.massivecraft.mcore4.store.Colls;

public class VPlayerColls extends Colls<VPlayerColl, VPlayer, String>
{
	public static VPlayerColls i = new VPlayerColls();
	
	@Override
	public VPlayerColl createColl(String collName)
	{
		VPlayerColl ret = new VPlayerColl(collName);
		ret.init();
		return ret;
	}

	@Override
	public String name()
	{
		return Conf.dbprefix+"player";
	}
}
