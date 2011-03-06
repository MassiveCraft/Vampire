package com.bukkit.mcteam.vampire;


import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

import com.bukkit.mcteam.gson.Gson;
import com.bukkit.mcteam.gson.GsonBuilder;
import com.bukkit.mcteam.vampire.commands.CommandBase;
import com.bukkit.mcteam.vampire.commands.Cure;
import com.bukkit.mcteam.vampire.commands.Default;
import com.bukkit.mcteam.vampire.commands.Infect;
import com.bukkit.mcteam.vampire.commands.Listing;
import com.bukkit.mcteam.vampire.commands.Load;
import com.bukkit.mcteam.vampire.commands.Save;
import com.bukkit.mcteam.vampire.commands.Settime;
import com.bukkit.mcteam.vampire.commands.Turn;
import com.bukkit.mcteam.vampire.commands.Version;
import com.bukkit.mcteam.vampire.listeners.VampireEntityListener;
import com.bukkit.mcteam.vampire.listeners.VampirePlayerListener;

public class Vampire extends JavaPlugin {
	// -------------------------------------------- //
	// Fields
	// -------------------------------------------- //
	public static Vampire instance;
	
	public static Timer timer = new Timer();
	public static Random random = new Random();
	public static final Gson gson = new GsonBuilder()
	.setPrettyPrinting()
	.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE)
	.create();
	
	// Commands
	public List<CommandBase> commands = new ArrayList<CommandBase>();
	
	// Listeners
	private final VampirePlayerListener playerListener = new VampirePlayerListener();
	private final VampireEntityListener entityListener = new VampireEntityListener();
	
	public Vampire() {
		Vampire.instance = this;
		
		// Add the commands
		commands.add(new Default());
		commands.add(new Infect());
		commands.add(new Load());
		commands.add(new Save());
		commands.add(new Settime());
		commands.add(new Turn());
		commands.add(new Cure());
		commands.add(new Listing());
		commands.add(new Version());
	}
	
	// -------------------------------------------- //
	// Important interface implementations and overrides
	// -------------------------------------------- //
	@Override
	public void onDisable() {
		timer.cancel();
		VPlayer.save();
		log("Disabled");
	}

	@Override
	public void onEnable() {
		// Ensure basefolder exists!
		this.getDataFolder().mkdirs();
		
		// Load Conf from disk
		Conf.load();
		
		// Do an interesting test
		if (Conf.regenBloodPerHealth < Conf.playerBloodQuality) {
			log("WARNING!! regenBloodPerHealth < playerBloodQuality. This means that vampires can feed on eachother back and forth to survive.");
		}
		
		// Load VPlayers from disk
		VPlayer.load();
		
		// Start timer
		timer.schedule(new VampireTask(), 0, //initial delay
		        Conf.timerInterval); //subsequent rate
		
		// Register events
		PluginManager pm = this.getServer().getPluginManager();
		//pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_ITEM, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGED, this.entityListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_TARGET, this.entityListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DEATH, this.entityListener, Event.Priority.Normal, this);
		
		log("Enabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		List<String> parameters = new ArrayList<String>(Arrays.asList(args));

		if (parameters.size() == 0) {
			this.commands.get(0).execute(sender, parameters);
			return true;
		}
		
		String command = parameters.get(0).toLowerCase();
		parameters.remove(0);
		
		for (CommandBase vampcommand : this.commands) {
			if (command.equals(vampcommand.getName())) {
				vampcommand.execute(sender, parameters);
				return true;
			}
		}
		
		return false;
	}
	
	// -------------------------------------------- //
	// Logging
	// -------------------------------------------- //
	public static void log(String msg) {
		Logger.getLogger("Minecraft").info("["+instance.getDescription().getFullName()+"] "+msg);
	}
	
	
}
