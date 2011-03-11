package com.bukkit.mcteam.vampire.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class VCommandTime extends VCommand {

	public VCommandTime() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = true;
		requiredParameters.add("ticks");
		permissions = "vampire.admin.command.time";
		helpNameAndParams = "time [ticks]";
		helpDescription = "Set world time. 0 to 23999.";
	}
	
	@Override
	public void perform() {
		long ticks = Long.parseLong(parameters.get(0));
		
		Player player = (Player)this.sender;
		player.getWorld().setTime(ticks);
		
		sender.sendMessage("The time in "+player.getWorld().getName()+" is now "+ticks);
	}
}
