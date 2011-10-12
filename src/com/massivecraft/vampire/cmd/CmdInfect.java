package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;

public class CmdInfect extends VCommand
{
	
	public CmdInfect()
	{
		aliases.add("infect");
		
		requiredArgs.add("playername");
		
		this.setHelpShort("infect others that are willing");
		
		permission = Permission.COMMAND_INFECT.node;
		senderMustBePlayer = true;
		senderMustBeVampire = true;
	}
	
	@Override
	public void perform()
	{
		Player you = this.argAsBestPlayerMatch(0);
		if (you == null) return;
		VPlayer vyou = VPlayers.i.get(you);
		vme.offerInfectionTo(vyou);
	}
}
