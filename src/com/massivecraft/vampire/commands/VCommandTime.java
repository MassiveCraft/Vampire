package com.massivecraft.vampire.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.config.Conf;

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
		Player playerSender = (Player)sender;
		if(playerSender.isOp())
		{
			if(!Conf.allowOPToUseAdminCommand) playerSender.sendMessage(Conf.colorSystem + "Config file do not allow OP to use it, you can change this.");
			else
			{
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
		else playerSender.sendMessage(Conf.colorSystem + "Only OP is able to use this command.");
	}
}
