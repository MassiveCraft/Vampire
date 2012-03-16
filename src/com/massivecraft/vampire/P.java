package com.massivecraft.vampire;

import org.bukkit.Bukkit;

import com.massivecraft.mcore2.MPlugin;
import com.massivecraft.vampire.cmd.*;
import com.massivecraft.vampire.config.*;
import com.massivecraft.vampire.listeners.*;


public class P extends MPlugin
{
	// Our single plugin instance
	public static P p;
	
	// Listeners
	public VampireListener vampireListener;
	
	// Command
	public CmdBase cmdBase;
	
	public P()
	{
		P.p = this;
	}

	@Override
	public void onEnable()
	{
		if ( ! preEnable()) return;
		
		// Create and load VPlayers
		VPlayers.i.loadOldFormat();
		
		// Load Conf from disk
		Conf.load();
		Lang.load();
		
		// Add Base Commands
		this.cmdBase = new CmdBase();
		this.cmd.addCommand(this.cmdBase);
	
		// Start timer
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new VampireTask(), 0, Conf.taskInterval);
	
		// Register events
		vampireListener = new VampireListener(this);
		
		postEnable();
	}
	
	@Override
	public void onDisable()
	{
		Conf.save();
		Lang.save();
		super.onDisable();
	}
}
