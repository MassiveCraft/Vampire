package com.massivecraft.vampire.commands;

import org.bukkit.entity.Player;

public class VCommandTime extends VCommand {

	public VCommandTime() {
		aliases.add("time");

		requiredParameters.add("time");
		
		helpDescription = "set world time";
		
		permission = "vampire.command.time";
		senderMustBePlayer = true;
		senderMustBeVampire = false;
	}
	
	// TODO: Fix this logic
	
	@Override
	public void perform() {
		String parameter = parameters.get(0).toLowerCase();
		sender.sendMessage(parameter);
		Player player = (Player)this.sender;
		
		if(parameter.compareTo("get") == 0) sender.sendMessage("The time is "+ player.getWorld().getTime());
		else
		{
			long ticks = Long.parseLong(parameters.get(0));
			player.getWorld().setTime(ticks);
			sender.sendMessage("The time in "+player.getWorld().getName()+" is now "+ticks);
		}
	}
}
