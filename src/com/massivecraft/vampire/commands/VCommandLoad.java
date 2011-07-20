package com.massivecraft.vampire.commands;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.config.Conf;

public class VCommandLoad extends VCommandFile {
	
	public VCommandLoad() {
		permissions = "vampire.admin.command.load";
		helpNameAndParams = "load [config | players | all]";
		helpDescription = "Load data from disk.";
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
				String what = parameters.get(0);
		
				if (meansAll(what)) {
					this.sendMessage(this.loadPlayers());
					this.sendMessage(this.loadConfiguration());
				}
				
				if (meansConfiguration(what)) {
					this.sendMessage(this.loadConfiguration());
				}
		
				if (meansPlayers(what)) {
					this.sendMessage(this.loadPlayers());
				}
			}
		}
		else playerSender.sendMessage(Conf.colorSystem + "Only OP is able to use this command.");
	}
}
