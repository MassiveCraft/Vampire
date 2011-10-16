package com.massivecraft.vampire.util;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.vampire.P;

public class SmokeUtil
{	
	public static void spawnAtLocation(Location location)
	{
		for (int i = 0; i <= 8; i++)
		{
			if (P.random.nextBoolean())
			{
				location.getWorld().playEffect(location, Effect.SMOKE, i);
			}
		}
	}
	
	public static void spawnAtPlayerFeet(Player player)
	{
		spawnAtLocation(player.getLocation());
	}
	
	public static void spawnAtPlayerHead(Player player)
	{
		spawnAtLocation(player.getLocation().add(0, 1, 0));
	}
	
	public static void spawnAtPlayer(Player player)
	{
		spawnAtPlayerHead(player);
		spawnAtPlayerFeet(player);
	}
	
	public static void smokeifyPlayer(Player player, long stopsAfterTicks)
	{
		smokeifyPlayer(player, stopsAfterTicks, 5);
	}
	
	public static void smokeifyPlayer(Player player, long stopsAfterTicks, long intervalTicks)
	{
		if (player == null) return;
		SmokeTask task = new SmokeTask(player, (long)((double)stopsAfterTicks / intervalTicks));
		int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(P.p, task, 0, intervalTicks);
		task.id = taskId;
	}
}
