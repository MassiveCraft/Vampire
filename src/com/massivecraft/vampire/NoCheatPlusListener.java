package com.massivecraft.vampire;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.neatmonster.nocheatplus.checks.moving.RunningCheck.RunningCheckEvent;

public class NoCheatPlusListener implements Listener
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public P p;
	public NoCheatPlusListener(P p)
	{
		this.p = p;
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
	}
	
	// -------------------------------------------- //
	// LE EVENT! \:D/
	// -------------------------------------------- //
	
	@EventHandler(ignoreCancelled = true)
    public void onRunFlyCheck(final RunningCheckEvent event)
	{
        if (p.noCheatExemptedPlayerNames.contains(event.getPlayer().getName())) event.setCancelled(true);
	}
}
