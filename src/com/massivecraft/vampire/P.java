package com.massivecraft.vampire;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;

import com.massivecraft.mcore4.MPlugin;
import com.massivecraft.mcore4.usys.Aspect;
import com.massivecraft.mcore4.usys.AspectColl;
import com.massivecraft.vampire.cmd.CmdBase;
import com.massivecraft.vampire.integration.nocheatplus.NoCheatPlusFeatures;
import com.massivecraft.vampire.integration.spout.SpoutFeatures;

public class P extends MPlugin 
{
	// Our single plugin instance
	public static P p;
	
	// Command
	public CmdBase cmdBase;
	
	// noCheatExemptedPlayerNames
	// http://dev.bukkit.org/server-mods/nocheatplus/
	// https://gist.github.com/2638309
	public Set<String> noCheatExemptedPlayerNames = new HashSet<String>();
	
	// Aspects
	
	public Aspect playerAspect;
	public Aspect configAspect;
	
	public P()
	{
		P.p = this;
	}

	@Override
	public void onEnable()
	{
		if ( ! preEnable()) return;
		
		// Init aspects
		this.playerAspect = AspectColl.i.get(ConfServer.playerAspectId, true);
		this.playerAspect.register();
		this.playerAspect.desc(
			"<i>Everything player related:", 
			"<i>Is the player a vampire or not?",
			"<i>What was the infection reason?",
			"<i>Check <h>"+ConfServer.configAspectId+" <i>for rules and balancing."
		);
		
		this.configAspect = AspectColl.i.get(ConfServer.configAspectId, true);
		this.configAspect.register();
		this.configAspect.desc(
			"<i>Config options for balancing:", 
			"<i>What is the splash potion radius for holy water?",
			"<i>What items are considered wooden stakes?",
			"<i>Check <h>"+ConfServer.playerAspectId+" <i>for player state."
		);
		
		// Load Conf from disk
		Lang.i.load();
		ConfServer.i.load();
		ConfColls.i.init();
		VPlayerColls.i.init();
		
		// Initialize collections
		// VPlayerColl.i.init();
		
		// Add Base Commands
		this.cmdBase = new CmdBase();
		this.cmdBase.register();
		
		// Start timer
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TheTask(), 0, ConfServer.taskInterval);
	
		// Register events
		new TheListener(this);
		
		// Integration
		this.integrate(SpoutFeatures.get(), NoCheatPlusFeatures.get());
		
		postEnable();
	}
	
}