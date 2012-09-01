package com.massivecraft.vampire;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.massivecraft.mcore4.MPlugin;
import com.massivecraft.mcore4.cmd.arg.AHPlayerWrapperColls;
import com.massivecraft.vampire.cmd.CmdBase;
import com.massivecraft.vampire.keyboard.BloodlustToggle;
import com.massivecraft.vampire.keyboard.Shriek;

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
		Conf.i.load();
		Lang.i.load();
		
		// Initialize collections
		// VPlayerColl.i.init();
		
		// Add Base Commands
		this.cmdBase = new CmdBase();
		this.cmdBase.register();
		
		// Add Argument Handlers
		this.cmd.setArgHandler(VPlayer.class, new AHPlayerWrapperColls<VPlayer>(VPlayerColls.i));
		// TODO: Do this automatically?
		
		// Start timer
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TheTask(), 0, Conf.taskInterval);
	
		// Register events
		new TheListener(this);
		this.noCheatPlusSetup();
		
		// Register Key Bindings
		BloodlustToggle.get().register();
		Shriek.get().register();
		
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
}