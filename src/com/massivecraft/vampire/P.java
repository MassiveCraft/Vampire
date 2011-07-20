package com.massivecraft.vampire;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

import com.massivecraft.vampire.commands.*;
import com.massivecraft.vampire.config.*;
import com.massivecraft.vampire.gson.Gson;
import com.massivecraft.vampire.gson.GsonBuilder;
import com.massivecraft.vampire.listeners.*;


public class P extends JavaPlugin {
	// -------------------------------------------- //
	// Fields
	// -------------------------------------------- //
	public static P instance;
	
	public static Timer timer;
	public static Random random = new Random();
	public static final Gson gson = new GsonBuilder()
	.setPrettyPrinting()
	.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE)
	.create();
	
	// Commands
	public List<VCommand> commands = new ArrayList<VCommand>();
	private String baseCommand;
	
	// Listeners
	private final VampirePlayerListener playerListener = new VampirePlayerListener();
	private final VampireEntityListener entityListener = new VampireEntityListener();
	private final VampireEntityListenerMonitor entityListenerMonitor = new VampireEntityListenerMonitor();
	
	public P() {
		P.instance = this;
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
		// Add the commands
		commands.add(new VCommandBlood());
		commands.add(new VCommandHelp());
		commands.add(new VCommandList());
		commands.add(new VCommandInfect());
		commands.add(new VCommandTurn());
		commands.add(new VCommandCure());
		commands.add(new VCommandFeed());
		commands.add(new VCommandTime());
		commands.add(new VCommandLoad());
		commands.add(new VCommandSave());
		commands.add(new VCommandVersion());
		
		timer = new Timer();
		
		// Ensure basefolder exists!
		this.getDataFolder().mkdirs();
		
		// Load Conf from disk
		Conf.load();
		Lang.load();
		CommonConf.load();
		TrueBloodConf.load();
		
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
		pm.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_ANIMATION, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, this.entityListener, Event.Priority.High, this);
		pm.registerEvent(Event.Type.ENTITY_TARGET, this.entityListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, this.entityListenerMonitor, Event.Priority.High, this);
		pm.registerEvent(Event.Type.ENTITY_DEATH, this.entityListenerMonitor, Event.Priority.Monitor, this);
		log("Enabled");
	}
	
	// -------------------------------------------- //
	// Commands
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public String getBaseCommand() {
		if (this.baseCommand != null) {
			return this.baseCommand;
		}
		
		Map<String, Object> Commands = (Map<String, Object>)this.getDescription().getCommands();
		this.baseCommand = Commands.keySet().iterator().next();
		return this.baseCommand;
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
		
		String commandName = parameters.get(0).toLowerCase();
		parameters.remove(0);
		
		for (VCommand vampcommand : this.commands) {
			if (vampcommand.getAliases().contains(commandName)) {
				vampcommand.execute(sender, parameters);
				return;
			}
		}
		
		sender.sendMessage(Conf.colorSystem+"Unknown vampire command \""+commandName+"\". Try "+Conf.colorCommand+"/"+this.getBaseCommand()+" help");
	}
	
	
	// -------------------------------------------- //
	// Logging
	// -------------------------------------------- //
	public static void log(String msg) {
		log(Level.INFO, msg);
	}
	
	public static void log(Level level, String msg) {
		Logger.getLogger("Minecraft").log(level, "["+instance.getDescription().getFullName()+"] "+msg);
	}
}
