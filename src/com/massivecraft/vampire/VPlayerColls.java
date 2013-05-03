package com.massivecraft.vampire;

import com.massivecraft.mcore.Aspect;
import com.massivecraft.mcore.store.Colls;

public class VPlayerColls extends Colls<VPlayerColl, VPlayer>
{
	public static VPlayerColls i = new VPlayerColls();
	
	@Override
	public VPlayerColl createColl(String collName)
	{
		return new VPlayerColl(collName);
	}

	@Override
	public Aspect getAspect()
	{
		return P.p.playerAspect;
	}

	@Override
	public String getBasename()
	{
		return Const.playerBasename;
	}
}
