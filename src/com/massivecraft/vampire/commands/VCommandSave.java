package com.massivecraft.vampire.commands;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.config.Conf;

public class VCommandSave extends VCommandFile {
	
	public VCommandSave() {
		permissions = "vampire.admin.command.save";
		helpNameAndParams = "save [config | players | all]";
		helpDescription = "Save data from disk.";
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
					this.sendMessage(this.savePlayers());
					this.sendMessage(this.saveConfiguration());
				}
		
				if (meansConfiguration(what)) {
					this.sendMessage(this.saveConfiguration());
				}
		
				if (meansPlayers(what)) {
					this.sendMessage(this.savePlayers());
				}
			}
		}
		else playerSender.sendMessage(Conf.colorSystem + "Only OP is able to use this command.");
	}
}
