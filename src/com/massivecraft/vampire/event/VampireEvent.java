package com.massivecraft.vampire.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import com.massivecraft.vampire.P;

public abstract class VampireEvent extends Event implements Runnable
{
	@Override
	public void run()
	{
		Bukkit.getPluginManager().callEvent(this);
	}
	
	public void run(long delay)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(P.p, this, delay);
	}
}
