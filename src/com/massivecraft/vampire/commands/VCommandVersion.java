package com.massivecraft.vampire.commands;

import com.massivecraft.vampire.P;

public class VCommandVersion extends VCommand {
	
	public VCommandVersion() {
		aliases.add("version");
		
		helpDescription = "display current version";
		
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform() {
		this.sendMessage("You are running "+P.instance.getDescription().getFullName());
	}
}
