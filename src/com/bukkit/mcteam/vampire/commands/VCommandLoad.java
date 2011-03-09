package com.bukkit.mcteam.vampire.commands;

public class VCommandLoad extends VCommandFile {
	
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
