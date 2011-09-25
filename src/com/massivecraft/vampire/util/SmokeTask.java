package com.massivecraft.vampire.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SmokeTask implements Runnable
{
	public int id;
	public long timesLeft;
	public Player player;
	
	public SmokeTask(Player player, long times)
	{
		this.player = player;
		this.timesLeft = times;
	}
	
	@Override
	public void run()
	{
		if (this.timesLeft == 0 || ! player.isOnline())
		{
			Bukkit.getScheduler().cancelTask(id);
			return;
		}
		SmokeUtil.spawnAtPlayer(player);
		this.timesLeft -=1;
	}
}
