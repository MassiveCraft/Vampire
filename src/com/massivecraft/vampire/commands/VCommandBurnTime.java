package com.massivecraft.vampire.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.config.Conf;

public class VCommandBurnTime extends VCommand
{
	public VCommandBurnTime()
	{
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		senderMustBeVampire = false;

		requiredParameters.add("from");
		requiredParameters.add("to");
	}
	
	@Override
	public void perform()
	{
		Player playerSender = (Player)sender;
		if(playerSender.isOp())
		{
			if(!Conf.allowOPToUseAdminCommand) playerSender.sendMessage(Conf.colorSystem + "Config file does not allow OP to use it, you can change this.");
			else
			{
				String fromTime = parameters.get(0);
				String toTime = parameters.get(1);
				
				Conf.combustFromTime = Integer.parseInt(fromTime);
				Conf.combustToTime = Integer.parseInt(toTime);
				Conf.save();
				
				this.sendMessage("Day time set from " + fromTime + " to " + toTime + ". Vampires will burn during this time.");
			}
		}
		else playerSender.sendMessage(Conf.colorSystem + "Only OP is able to use this command.");
	}
}
