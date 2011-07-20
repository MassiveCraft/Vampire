package com.massivecraft.vampire.commands;

public class VCommandTime extends VCommand {

	public VCommandTime() {
		aliases.add("time");

		optionalParameters.add("time");
		
		helpDescription = "get or set time (0 -> 23999)";
		
		permission = "vampire.command.time";
		senderMustBePlayer = true;
		senderMustBeVampire = false;
	}

	@Override
	public void perform() {
		if (parameters.size() == 0) {
			sendMessage("The time in "+player.getWorld().getName()+" is currently " + player.getWorld().getTime() % 24000);
			return;
		}
		
		long ticks = Long.parseLong(parameters.get(0));
		player.getWorld().setTime(ticks);
		sendMessage("The time in "+player.getWorld().getName()+" was changed to " + ticks % 24000);
	}
}
