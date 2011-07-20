package com.massivecraft.vampire.commands;

import java.util.ArrayList;

import com.massivecraft.vampire.P;


public class VCommandVersion extends VCommand {
	public VCommandVersion() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		permissions = "vampire.default.command.version";
		helpNameAndParams = "version";
		helpDescription = "Would display "+P.instance.getDescription().getFullName();
	}
	
	@Override
	public void perform() {
		this.sendMessage("You are running "+P.instance.getDescription().getFullName());
	}
}
