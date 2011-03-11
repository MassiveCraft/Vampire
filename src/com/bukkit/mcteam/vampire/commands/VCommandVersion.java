package com.bukkit.mcteam.vampire.commands;

import java.util.ArrayList;

import com.bukkit.mcteam.vampire.Vampire;

public class VCommandVersion extends VCommand {
	public VCommandVersion() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		permissions = "vampire.default.command.version";
		helpNameAndParams = "version";
		helpDescription = "Would display "+Vampire.instance.getDescription().getFullName();
	}
	
	@Override
	public void perform() {
		this.sendMessage("You are running "+Vampire.instance.getDescription().getFullName());
	}
}
