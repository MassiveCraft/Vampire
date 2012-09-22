package com.massivecraft.vampire;

import com.massivecraft.mcore4.store.Colls;
import com.massivecraft.mcore4.usys.Aspect;

public class VPlayerColls extends Colls<VPlayerColl, VPlayer, String>
{
	public static VPlayerColls i = new VPlayerColls();
	
	@Override
	public VPlayerColl createColl(String collName)
	{
		return new VPlayerColl(collName);
	}

	@Override
	public Aspect aspect()
	{
		return P.p.playerAspect;
	}

	@Override
	public String basename()
	{
		return ConfServer.playerBasename;
	}
	
	
}
