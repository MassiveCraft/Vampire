package com.massivecraft.vampire;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TheTask implements Runnable
{
	@Override
	public void run()
	{
		// Tick each online player
		for (Player player: Bukkit.getOnlinePlayers())
		{
			VPlayer vplayer = VPlayerColls.i.get2(player);
			vplayer.tick(Conf.taskInterval);
		}
	}
}
