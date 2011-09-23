package com.massivecraft.vampire;

import com.massivecraft.vampire.config.GeneralConf;

public class VampireTask implements Runnable
{
	
	@Override
	public void run()
	{
		// Tick each online player
		for (VPlayer vplayer : VPlayers.i.getOnline())
		{
			vplayer.advanceTime(GeneralConf.taskInterval);
		}
	}
}
