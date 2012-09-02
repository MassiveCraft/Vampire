package com.massivecraft.vampire;

import com.massivecraft.mcore4.store.Colls;

public class ConfColls extends Colls<ConfColl, Conf, String>
{
	public static ConfColls i = new ConfColls();
	
	@Override
	public ConfColl createColl(String collName)
	{
		ConfColl ret = new ConfColl(collName);
		ret.init();
		return ret;
	}

	@Override
	public String getContext()
	{
		return ConfServer.configContext;
	}
	
	@Override
	public Conf get2(Object worldNameExtractable)
	{
		ConfColl coll = this.get(worldNameExtractable);
		if (coll == null) return null;
		return coll.get(Conf.INSTANCE);
	}
}

