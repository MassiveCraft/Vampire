package com.massivecraft.vampire.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.config.Conf;

public class VCommandChatColor extends VCommand
{
	public VCommandChatColor() {
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = true;
		senderMustBeVampire = true;
		requiredParameters.add("state");
	}
	
	@Override
	public void perform()
	{
		Player playerSender = (Player)sender;
		if(playerSender.isOp())
		{
			if(!Conf.allowOPToUseAdminCommand) playerSender.sendMessage(Conf.colorSystem + "Config file do not allow OP to use it, you can change this.");
			else
			{
				String statement = parameters.get(0).toLowerCase();
				if(statement.compareTo("enable") == 0)
				{
					Conf.enableVampireNameColorInChat = true;
				}
				else if(statement.compareTo("disable") == 0)
				{
					Conf.enableVampireNameColorInChat = false;
				}
				else
				{
					playerSender.sendMessage(Conf.colorSystem + statement + " is not a valid parameter.");
					playerSender.sendMessage(Conf.colorSystem + "Use enable or disable as parameter.");
				}
			}
		}
		else playerSender.sendMessage(Conf.colorSystem + "Only OP is able to use this command.");
	
	}
}
