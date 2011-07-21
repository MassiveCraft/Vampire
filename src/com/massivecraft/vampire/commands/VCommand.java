package com.massivecraft.vampire.commands;

import java.util.*;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.vampire.P;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.config.Conf;
import com.massivecraft.vampire.util.TextUtil;


public class VCommand {
	public List<String> aliases;
	public List<String> requiredParameters;
	public List<String> optionalParameters;
	
	public String helpNameAndParams;
	public String helpDescription;
	
	public CommandSender sender;
	public Player player;
	public VPlayer me;
	
	public String permission;
	public boolean senderMustBePlayer;
	public boolean senderMustBeVampire;
	
	public List<String> parameters;
	
	public VCommand() {
		aliases = new ArrayList<String>();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		
		helpNameAndParams = "fail!";
		helpDescription = "no description";
		
		permission = null; // No permission required
		senderMustBePlayer = true;
		senderMustBeVampire = false;
	}
	
	public List<String> getAliases() {
		return aliases;
	}
	
	public void execute(CommandSender sender, List<String> parameters) {
		this.sender = sender;
		this.parameters = parameters;
		
		if ( ! validateCall()) {
			return;
		}
		
		if (sender instanceof Player) {
			this.player = (Player)sender;
			this.me = VPlayer.get(this.player);
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
		
		if ( this.senderMustBePlayer && ! (sender instanceof Player)) {
			sendMessage("This command can only be used by ingame players.");
			return false;
		}
		
		Player player = (Player)sender;
		
		if (( this.senderMustBeVampire && ! VPlayer.get(player).isVampire())) {
			this.sendMessage("Only vampires can use this command.");
			return false;
		}
		
		if( permission != null && ! sender.hasPermission(permission)) {
			sendMessage("You lack the permissions to "+this.helpDescription.toLowerCase()+".");
			return false;
		}
		
		if (parameters.size() < requiredParameters.size()) {
			sendMessage("Usage: "+this.getUseageTemplate(false));
			return false;
		}
		
		return true;
	}
	
	// -------------------------------------------- //
	// Help and usage description
	// -------------------------------------------- //
	
	public String getUseageTemplate(boolean withDescription) {
		String ret = "";
		
		ret += Conf.colorCommand;
		
		ret += P.instance.getBaseCommand()+ " " +TextUtil.implode(this.getAliases(), ",")+" ";
		
		List<String> parts = new ArrayList<String>();
		
		for (String requiredParameter : this.requiredParameters) {
			parts.add("["+requiredParameter+"]");
		}
		
		for (String optionalParameter : this.optionalParameters) {
			parts.add("*["+optionalParameter+"]");
		}
		
		ret += Conf.colorParameter;
		
		ret += TextUtil.implode(parts, " ");
		
		if (withDescription) {
			ret += "  "+Conf.colorSystem + this.helpDescription;
		}
		return ret;
	}
	
	public String getUseageTemplate() {
		return getUseageTemplate(true);
	}
	
	
}
