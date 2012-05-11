package com.massivecraft.vampire;


public class TheTask implements Runnable
{
	@Override
	public void run()
	{
		// Tick each online player
		for (VPlayer vplayer : VPlayers.i.getAllOnline())
		{
			vplayer.tick(Conf.taskInterval);
		}
	}
}
