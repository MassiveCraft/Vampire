package com.massivecraft.vampire;


import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChatEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.massivecraft.vampire.commands.*;
import com.massivecraft.vampire.config.*;
import com.massivecraft.vampire.listeners.VampireEntityListener;
import com.massivecraft.vampire.listeners.VampireEntityListenerMonitor;
import com.massivecraft.vampire.listeners.VampirePlayerListener;


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
	
	// Listeners
	private final VampirePlayerListener playerListener = new VampirePlayerListener();
	private final VampireEntityListener entityListener = new VampireEntityListener();
	private final VampireEntityListenerMonitor entityListenerMonitor = new VampireEntityListenerMonitor();
	
	public P()
	{
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
		commands.add(new VCommandInfect());
		commands.add(new VCommandLoad());
		commands.add(new VCommandSave());
		commands.add(new VCommandTime());
		commands.add(new VCommandTurn());
		commands.add(new VCommandCure());
		commands.add(new VCommandList());
		commands.add(new VCommandVersion());
		commands.add(new VCommandHelp());
		commands.add(new VCommandBurnTime());
		commands.add(new VCommandChatColor());
		commands.add(new VCommandFeed());
		
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
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			List<String> parameters = new ArrayList<String>(Arrays.asList(args));
			this.handleCommand(sender, parameters);
			return true;
		}
		return false;
	}
	
	public void handleChat(PlayerChatEvent event)
	{
		//Color the player name if he is a vampire
		if(VPlayer.get(event.getPlayer()).isVampire() && Conf.enableVampireNameColorInChat)
		{ 
			event.getPlayer().getServer().broadcastMessage(Conf.vampireChatNameColor + "<" + event.getPlayer().getName() + ">" + Conf.vampireChatMessageColor + event.getMessage());
			event.setCancelled(true);
		}
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
		
		sender.sendMessage(Conf.colorSystem+"Unknown vampire command \""+command+"\". Try /help vampire"); // TODO test help messages exists....
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
