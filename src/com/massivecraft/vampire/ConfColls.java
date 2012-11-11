package com.massivecraft.vampire;

import com.massivecraft.mcore5.MCore;
import com.massivecraft.mcore5.store.Colls;
import com.massivecraft.mcore5.usys.Aspect;

public class ConfColls extends Colls<ConfColl, Conf, String>
{
	public static ConfColls i = new ConfColls();
	
	@Override
	public ConfColl createColl(String collName)
	{
		return new ConfColl(collName);
	}

	@Override
	public Aspect getAspect()
	{
		return P.p.configAspect;
	}
	
	@Override
	public String getBasename()
	{
		return Const.configBasename;
	}
	
	@Override
	public Conf get2(Object worldNameExtractable)
	{
		ConfColl coll = this.get(worldNameExtractable);
		if (coll == null) return null;
		return coll.get(MCore.INSTANCE);
	}

	
}

