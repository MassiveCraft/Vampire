package com.massivecraft.vampire.entity;

import com.massivecraft.mcore.Aspect;
import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.Colls;
import com.massivecraft.vampire.Const;
import com.massivecraft.vampire.Vampire;

public class UConfColls extends Colls<UConfColl, UConf>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static UConfColls i = new UConfColls();
	public static UConfColls get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: COLLS
	// -------------------------------------------- //
	
	@Override
	public UConfColl createColl(String collName)
	{
		return new UConfColl(collName);
	}

	@Override
	public Aspect getAspect()
	{
		return Vampire.get().configAspect;
	}
	
	@Override
	public String getBasename()
	{
		return Const.COLLECTION_UCONF;
	}
	
	@Override
	public UConf get2(Object worldNameExtractable)
	{
		UConfColl coll = this.get(worldNameExtractable);
		if (coll == null) return null;
		return coll.get(MCore.INSTANCE);
	}
	
}
