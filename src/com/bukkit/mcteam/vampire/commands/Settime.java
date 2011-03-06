package com.bukkit.mcteam.vampire.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Settime extends CommandBase {

	public Settime() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBeOp = true;
		senderMustBePlayer = true;
		
		requiredParameters.add("ticks");
	}
	
	@Override
	public void perform() {
		long ticks = Long.parseLong(parameters.get(0));
		
		Player player = (Player)this.sender;
		player.getWorld().setTime(ticks);
		
		sender.sendMessage("The time in "+player.getWorld().getName()+" is now "+ticks);
	}
}
