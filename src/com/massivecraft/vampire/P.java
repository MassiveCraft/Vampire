package com.massivecraft.vampire;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Event;

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
	
	// Vampire Type Configurations
	public VampireTypeConf confVampTypeOriginal;
	public VampireTypeConf confVampTypeCommon;
	
	public CmdHelp cmdHelp;
	
	public static Random random = new Random();
	
	public P()
	{
		P.p = this;
		
		playerListener = new VampirePlayerListener();
		entityListener = new VampireEntityListener();
		entityListenerMonitor = new VampireEntityListenerMonitor();
	}

	@Override
	public void onEnable()
	{
		if ( ! preEnable()) return;
		
		// Load Conf from disk
		GeneralConf.load();
		Lang.load();
		this.setupVampireTypeConf();
		
		VPlayers.i.loadFromDisc();
		
		// Add Base Commands
		this.cmdHelp = new CmdHelp();
		this.getBaseCommands().add(new CmdBase());
	
		// Start timer
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new VampireTask(), 0, GeneralConf.taskInterval);
	
		// Register events
		PluginManager pm = this.getServer().getPluginManager();
		//pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_ANIMATION, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, this.entityListener, Event.Priority.High, this);
		pm.registerEvent(Event.Type.ENTITY_TARGET, this.entityListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, this.entityListenerMonitor, Event.Priority.High, this);
		
		postEnable();
	}
	
	// -------------------------------------------- //
	// VAMPIRE TYPE CONFIGURATIONS
	// -------------------------------------------- //
	
	public void setupVampireTypeConf()
	{
		VampireTypeConf c;
		
		c = new VampireTypeConf();
		this.confVampTypeOriginal = this.persist.loadOrSaveDefault(c, VampireTypeConf.class, "conf_vampire_original");
		
		c = new VampireTypeConf();
		this.confVampTypeCommon = this.persist.loadOrSaveDefault(c, VampireTypeConf.class, "conf_vampire_common");
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
}
