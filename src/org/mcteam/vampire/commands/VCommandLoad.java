package org.mcteam.vampire.commands;

public class VCommandLoad extends VCommandFile {
	
	public VCommandLoad() {
		permissions = "vampire.admin.command.load";
		helpNameAndParams = "load [config | players | all]";
		helpDescription = "Load data from disk.";
	}
	
	@Override
	public void perform() {
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
