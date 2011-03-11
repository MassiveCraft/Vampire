package com.bukkit.mcteam.vampire.commands;

public class VCommandSave extends VCommandFile {
	
	public VCommandSave() {
		permissions = "vampire.admin.command.save";
		helpNameAndParams = "save [config | players | all]";
		helpDescription = "Save data from disk.";
	}
	
	@Override
	public void perform() {
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
