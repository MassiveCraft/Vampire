package com.massivecraft.vampire;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.mcore.ModuloRepeatTask;
import com.massivecraft.vampire.entity.MConf;
import com.massivecraft.vampire.entity.UPlayer;

public class TheTask extends ModuloRepeatTask
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TheTask i = new TheTask();
	public static TheTask get() { return i; }
	
	
	// -------------------------------------------- //
	// OVERRIDE: MODULO REPEAT TASK
	// -------------------------------------------- //
	
	@Override
	public long getDelayMillis()
	{
		return MConf.get().taskDelayMillis;
	}
	
	@Override
	public void setDelayMillis(long delayMillis)
	{
		MConf.get().taskDelayMillis = delayMillis;
		MConf.get().changed();
	}
	
	@Override
	public void invoke(long now)
	{
		// Tick each online player
		for (Player player: Bukkit.getOnlinePlayers())
		{
			UPlayer uplayer = UPlayer.get(player);
			uplayer.tick(now - this.getPreviousMillis());
		}
	}
	
}
