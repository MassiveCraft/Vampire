package com.massivecraft.vampire.commands;

public class VCommandSave extends VCommandFile {
	
	public VCommandSave() {
		aliases.add("save");
		
		helpDescription = "save [config | players | all] to disk";
		
		permission = "vampire.command.save";
	}
	
	@Override
	public void perform()
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
