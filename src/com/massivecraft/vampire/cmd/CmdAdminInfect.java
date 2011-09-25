package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;


public class CmdAdminInfect extends VCommand {
	
	public CmdAdminInfect() {
		aliases.add("ainfect");

		requiredArgs.add("playername");
		optionalArgs.put("amount", "1.0");
		
		helpShort = "set infection (0 to 100)";
		
		permission = Permission.COMMAND_INFECT.node;
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform() {
		Player you = this.argAsBestPlayerMatch(0);
		if (you == null) return;
		
		double amount = this.argAsDouble(1, 1.0);
		
		VPlayer vyou = VPlayers.i.get(you);
		vyou.setInfection(amount);
		
		// TODO: Improve Look and feel of this message
		this.msg(you.getDisplayName() + " now has infection " + vyou.getInfection());
	}
}
