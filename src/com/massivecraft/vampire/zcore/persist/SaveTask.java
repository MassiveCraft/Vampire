package com.massivecraft.vampire.zcore.persist;

public class SaveTask implements Runnable
{
	public void run()
	{
		EM.saveAllToDisc();
	}
}
