package com.massivecraft.vampire;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import com.massivecraft.mcore1.MPlugin;
import com.massivecraft.vampire.cmd.*;
import com.massivecraft.vampire.config.*;
import com.massivecraft.vampire.listeners.*;


public class P extends MPlugin
{
	// Our single plugin instance
	public static P p;
	
	// Listeners
	public VampirePlayerListener playerListener;
	public VampireEntityListener entityListener;
	public VampireEntityListenerMonitor entityListenerMonitor;
	public VampireBlockListener blockListener;
	
	// Command
	public CmdBase cmdBase;
	
	public P()
	{
		P.p = this;
		
		playerListener = new VampirePlayerListener(this);
		entityListener = new VampireEntityListener(this);
		entityListenerMonitor = new VampireEntityListenerMonitor(this);
		blockListener = new VampireBlockListener(this);
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
		this.registerEvent(Event.Type.PLAYER_JOIN, this.playerListener, Event.Priority.Low);
		this.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Normal);
		this.registerEvent(Event.Type.PLAYER_CHAT, this.playerListener, Event.Priority.Normal);
		this.registerEvent(Event.Type.PLAYER_ANIMATION, this.playerListener, Event.Priority.Normal);
		this.registerEvent(Event.Type.ENTITY_DAMAGE, this.entityListener, Event.Priority.High);
		this.registerEvent(Event.Type.ENTITY_TARGET, this.entityListener, Event.Priority.Normal);
		this.registerEvent(Event.Type.ENTITY_REGAIN_HEALTH, this.entityListener, Event.Priority.Normal);
		this.registerEvent(Event.Type.FOOD_LEVEL_CHANGE, this.entityListener, Event.Priority.Normal);
		this.registerEvent(Event.Type.ENTITY_DAMAGE, this.entityListenerMonitor, Event.Priority.High);
		this.registerEvent(Event.Type.BLOCK_BREAK, this.blockListener, Event.Priority.Highest);
		
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
