package com.massivecraft.vampire.zcore.persist;

import com.massivecraft.vampire.zcore.MPlugin;

public class SaveTask implements Runnable
{
	MPlugin p;
	public SaveTask(MPlugin p)
	{
		this.p = p;
	}
	
	public void run()
	{
		if ( ! p.getAutoSave()) return;
		p.preAutoSave();
		EM.saveAllToDisc();
		p.postAutoSave();
	}
}
