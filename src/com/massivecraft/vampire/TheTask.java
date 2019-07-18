package com.massivecraft.vampire;

import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.vampire.entity.MConf;
import com.massivecraft.vampire.entity.UPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;

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
	public MassivePlugin getPlugin()
	{
		return Vampire.get();
	}
	
	@Override
	public long getDelayMillis()
	{
		return MConf.get().taskDelayMillis;
	}
	
	@Override
	public void setDelayMillis(long delayMillis)
	{
		MConf.get().changed(MConf.get().taskDelayMillis, delayMillis);
		MConf.get().taskDelayMillis = delayMillis;
	}
	
	@Override
	public void invoke(long now)
	{
		// Tick each online player
		for (Player player : Bukkit.getServer().getOnlinePlayers())
		{
			if (MUtil.isntPlayer(player)) continue;
			
			UPlayer uplayer = UPlayer.get(player);
			try {
				uplayer.tick(now - this.getPreviousMillis());
			}
			catch (NullPointerException ex) {
				Bukkit.getServer().getLogger().log(Level.SEVERE, "While executing Vampire.TheTask, NullPointerException: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
	
}
