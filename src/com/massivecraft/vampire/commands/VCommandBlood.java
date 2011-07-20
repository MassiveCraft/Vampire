package com.massivecraft.vampire.commands;

public class VCommandBlood extends VCommand {
	
	public VCommandBlood() {
		aliases.add("blood");
		
		helpDescription = "See you current blood supply";
		
		permission = "vampire.command.blood";
		senderMustBePlayer = true;
		senderMustBeVampire = true;
	}
	
	@Override
	public void perform() {	
		me.bloodSendMeterMessage();
	}
}