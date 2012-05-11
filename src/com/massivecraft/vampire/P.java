package com.massivecraft.vampire;

import org.bukkit.Bukkit;

import com.massivecraft.mcore2.MPlugin;
import com.massivecraft.vampire.cmd.CmdBase;
import com.massivecraft.vampire.cmdarg.AHVPlayer;

public class P extends MPlugin 
{
	// Our single plugin instance
	public static P p;
	
	// Command
	public CmdBase cmdBase;
	
	// Listeners
	public TheListener theListener;
	
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
		//Conf.load();
		//Conf.save();
		//Lang.load();
		
		// Add Base Commands
		this.cmdBase = new CmdBase();
		this.cmdBase.register();
		
		// Add Argument Handlers
		this.cmd.setArgHandler(VPlayer.class, AHVPlayer.getInstance());
		
		// Start timer
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TheTask(), 0, Conf.taskInterval);
	
		// Register events
		new TheListener(this);
		
		postEnable();
	}
}
