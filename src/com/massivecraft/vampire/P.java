package com.massivecraft.vampire;

import java.lang.reflect.Modifier;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Event;

import com.google.gson.GsonBuilder;
import com.massivecraft.vampire.cmd.*;
import com.massivecraft.vampire.config.*;
import com.massivecraft.vampire.listeners.*;
import com.massivecraft.vampire.zcore.MPlugin;


public class P extends MPlugin
{
	// Our single plugin instance
	public static P p;
	
	// Listeners
	public VampirePlayerListener playerListener;
	public VampireEntityListener entityListener;
	public VampireEntityListenerMonitor entityListenerMonitor;
	public VampireBlockListener blockListener;
	
	public CmdHelp cmdHelp;
	public CmdBase cmdBase;
	
	public static Random random = new Random();
	
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
		
		// Load Conf from disk
		Conf.load();
		Lang.load();
		
		VPlayers.i.loadFromDisc();
		
		// Add Base Commands
		this.cmdHelp = new CmdHelp();
		this.cmdBase = new CmdBase();
		this.getBaseCommands().add(cmdBase);
	
		// Start timer
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new VampireTask(), 0, Conf.taskInterval);
	
		// Register events
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerListener, Event.Priority.Low, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_ANIMATION, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, this.entityListener, Event.Priority.High, this);
		pm.registerEvent(Event.Type.ENTITY_TARGET, this.entityListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, this.entityListenerMonitor, Event.Priority.High, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, this.blockListener, Event.Priority.Highest, this);
		
		
		postEnable();
	}
	
	// -------------------------------------------- //
	// LANG AND TAGS
	// -------------------------------------------- //
	
	@Override
	public void addLang()
	{
		super.addLang();
		this.lang.put("command.sender_must_me_vampire", "<b>Only vampires can use this command.");
	}
	
	@Override
	public void addTags()
	{
		super.addTags();
		/*this.tags.put("i", "§b");
		this.tags.put("h", "§a");*/
	}
	
	@Override
	public GsonBuilder getGsonBuilder()
	{
		return new GsonBuilder()
		.setPrettyPrinting()
		.disableHtmlEscaping()
		.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE);
	}
}
