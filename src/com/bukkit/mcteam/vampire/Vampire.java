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
import com.bukkit.mcteam.vampire.commands.VCommand;
import com.bukkit.mcteam.vampire.commands.VCommandCure;
import com.bukkit.mcteam.vampire.commands.VCommandDefault;
import com.bukkit.mcteam.vampire.commands.VCommandInfect;
import com.bukkit.mcteam.vampire.commands.VCommandList;
import com.bukkit.mcteam.vampire.commands.VCommandLoad;
import com.bukkit.mcteam.vampire.commands.VCommandSave;
import com.bukkit.mcteam.vampire.commands.VCommandSettime;
import com.bukkit.mcteam.vampire.commands.VCommandTurn;
import com.bukkit.mcteam.vampire.commands.VCommandVersion;
import com.bukkit.mcteam.vampire.listeners.VampireBlockListener;
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
	public List<VCommand> commands = new ArrayList<VCommand>();
	
	// Listeners
	private final VampirePlayerListener playerListener = new VampirePlayerListener();
	private final VampireEntityListener entityListener = new VampireEntityListener();
	private final VampireBlockListener blockListener = new VampireBlockListener();
	
	public Vampire() {
		Vampire.instance = this;
		
		// Add the commands
		commands.add(new VCommandDefault());
		commands.add(new VCommandInfect());
		commands.add(new VCommandLoad());
		commands.add(new VCommandSave());
		commands.add(new VCommandSettime());
		commands.add(new VCommandTurn());
		commands.add(new VCommandCure());
		commands.add(new VCommandList());
		commands.add(new VCommandVersion());
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
		pm.registerEvent(Event.Type.PLAYER_CHAT, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGED, this.entityListener, Event.Priority.High, this);
		pm.registerEvent(Event.Type.ENTITY_TARGET, this.entityListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DEATH, this.entityListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_RIGHTCLICKED, this.blockListener, Event.Priority.Normal, this);
		log("Enabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		List<String> parameters = new ArrayList<String>(Arrays.asList(args));
		this.handleCommand(sender, parameters);
		return true;
	}
	
	public void handleCommand(CommandSender sender, List<String> parameters) {
		if (parameters.size() == 0) {
			this.commands.get(0).execute(sender, parameters);
			return;
		}
		
		String command = parameters.get(0).toLowerCase();
		parameters.remove(0);
		
		for (VCommand vampcommand : this.commands) {
			if (command.equals(vampcommand.getName())) {
				vampcommand.execute(sender, parameters);
				return;
			}
		}
		
		sender.sendMessage(Conf.colorSystem+"Unknown vampire command \""+command+"\".");
	}
	
	// -------------------------------------------- //
	// Logging
	// -------------------------------------------- //
	public static void log(String msg) {
		Logger.getLogger("Minecraft").info("["+instance.getDescription().getFullName()+"] "+msg);
	}
	
	
}
