package org.mcteam.vampire.commands;

import java.util.*;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcteam.vampire.Conf;
import org.mcteam.vampire.VPlayer;
import org.mcteam.vampire.Vampire;


public class VCommand {
	public List<String> requiredParameters;
	public List<String> optionalParameters;
	public String permissions;
	public String helpNameAndParams;
	public String helpDescription;
	public boolean senderMustBePlayer;
	public boolean senderMustBeVampire;
	public CommandSender sender;
	public List<String> parameters;
	
	public VCommand() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		permissions = "";
		senderMustBePlayer = false;
		senderMustBeVampire = false;
		helpNameAndParams = "fail!";
		helpDescription = "no description";
	}
	
	public String getName() {
		String name = this.getClass().getName().toLowerCase();
		if (name.lastIndexOf('.') > 0) {
		    name = name.substring(name.lastIndexOf('.')+1);
		}
		return name.substring(8);
	}
	
	public String getBaseName() {
		//Vampire.log(""+Vampire.instance.getDescription().getCommands());
		return "v";
	}
	
	public void execute(CommandSender sender, List<String> parameters) {
		this.sender = sender;
		this.parameters = parameters;
		
		if ( ! validateCall()) {
			sendMessage("try /help vampire");
			return;
		}
		
		perform();
	}
	
	public void perform() {
		
	}
	
	public void helpRegister() {
		Vampire.helpPlugin.registerCommand(this.getBaseName()+ " " +this.helpNameAndParams, this.helpDescription, Vampire.instance, false, permissions);
	}
	
	public void sendMessage(String message) {
		sender.sendMessage(Conf.colorSystem+message);
	}
	
	public void sendMessage(List<String> messages) {
		for(String message : messages) {
			this.sendMessage(message);
		}
	}
	
	// Test if the number of params is correct.
	public boolean validateCall() { // Kolla upp help pluginen!
		if( ! testPermission(sender)) {
			sendMessage("You do not have sufficient permissions to use this command.");
			return false;
		}
		
		if ( this.senderMustBePlayer && ! (sender instanceof Player)) {
			sendMessage("This command can only be used by ingame players.");
			return false;
		}
		
		Player player = (Player)sender;
		
		if ( this.senderMustBeVampire && ! VPlayer.get(player).isVampire()) {
			this.sendMessage("Only vampires can use this command.");
			return false;
		}
		
		if (parameters.size() < requiredParameters.size()) {
			int missing = requiredParameters.size() - parameters.size();
			sendMessage("Missing parameters. You must enter "+missing+" more.");
			return false;
		}
		
		if (parameters.size() > requiredParameters.size() + optionalParameters.size()) {
			sendMessage("To many parameters.");
			return false;
		}
		
		return true;
	}
	
	public boolean testPermission(CommandSender sender) {
		if (sender.isOp()) {
			return true;
		}
		
		if (this.permissions.length() == 0) {
			return true;
		}
		
		if ( ! (sender instanceof Player)) {
			return false;
		}
		
		if (Vampire.Permissions == null) {
			return false;
		}
		
		Player player = (Player)sender;
		return Vampire.Permissions.has(player, this.permissions);		
	}
}
