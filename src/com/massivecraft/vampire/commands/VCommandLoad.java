package com.massivecraft.vampire.commands;

import com.massivecraft.vampire.Permission;

public class VCommandLoad extends VCommandFile {
	
	public VCommandLoad() {
		aliases.add("load");
		
		helpDescription = "load [config | players | all] from disk";
		
		permission = Permission.COMMAND_LOAD;
	}
	
	@Override
	public void perform()
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
