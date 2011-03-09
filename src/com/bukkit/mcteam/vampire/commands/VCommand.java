package com.bukkit.mcteam.vampire.commands;

import java.util.*;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bukkit.mcteam.vampire.Conf;

public class VCommand {
	public List<String> requiredParameters;
	public List<String> optionalParameters;
	public boolean senderMustBeOp;
	public boolean senderMustBePlayer;
	public CommandSender sender;
	public List<String> parameters;
	
	public VCommand() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBeOp = true;
		senderMustBePlayer = false;
	}
	
	public String getName() {
		String name = this.getClass().getName().toLowerCase();
		if (name.lastIndexOf('.') > 0) {
		    name = name.substring(name.lastIndexOf('.')+1);
		}
		return name.substring(8);
	}
	
	public void execute(CommandSender sender, List<String> parameters) {
		this.sender = sender;
		this.parameters = parameters;
		
		if ( ! validateCall()) {
			return;
		}
		
		perform();
	}
	
	public void perform() {
		
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
		//plugin.log("validate parameters:" + parameters);
		//plugin.log("this.getRequiredParameters():" + requiredParameters);
		
		if (senderMustBeOp) {
			if( ! assertOp(sender)) {
				return false;
			}
		}
		
		if ( this.senderMustBePlayer && ! (sender instanceof Player)) {
			sendMessage("This command can only be used by ingame players.");
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
	
	public boolean assertOp(CommandSender sender) {
		if ( ! sender.isOp()) {
			sendMessage("You do not have sufficient permissions to use this command.");
			return false;
		}
		return true;
	}
}
