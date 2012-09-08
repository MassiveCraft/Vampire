package com.massivecraft.vampire;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.massivecraft.mcore4.MPlugin;
import com.massivecraft.mcore4.cmd.arg.AHPlayerWrapperColls;
import com.massivecraft.vampire.cmd.CmdBase;

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
	
	public P()
	{
		P.p = this;
	}

	@Override
	public void onEnable()
	{
		if ( ! preEnable()) return;
		
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
		
		// Add Argument Handlers
		this.cmd.setArgHandler(VPlayer.class, new AHPlayerWrapperColls<VPlayer>(VPlayerColls.i));
		// TODO: Do this automatically?
		
		// Start timer
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TheTask(), 0, ConfServer.taskInterval);
	
		// Register events
		new TheListener(this);
		this.noCheatPlusSetup();
		this.spoutSetup();
		
		postEnable();
	}
	
	protected void noCheatPlusSetup()
	{
		Plugin noCheatPlus = Bukkit.getPluginManager().getPlugin("NoCheatPlus");
		if (noCheatPlus == null) return;
		if (noCheatPlus.isEnabled() == false) return;
		try
		{
			new NoCheatPlusHook(this);
		}
		catch (Exception e)
		{
			log("NoCheatPlus integration failed :( this Exception was raised:");
			e.printStackTrace();
			return;
		}
		log("NoCheatPlus integration successful :)");
	}
	
	protected void spoutSetup()
	{
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Spout");
		if (plugin == null) return;
		if (plugin.isEnabled() == false) return;
		try
		{
			new NoCheatPlusHook(this);
		}
		catch (Exception e)
		{
			log("Spout integration failed :( this Exception was raised:");
			e.printStackTrace();
			return;
		}
		log("Spout integration successful :)");
	}
}