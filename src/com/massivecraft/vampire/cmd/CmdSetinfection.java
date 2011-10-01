package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;
import com.massivecraft.vampire.config.Lang;
import com.massivecraft.vampire.zcore.CommandVisibility;


public class CmdSetinfection extends VCommand
{
	
	public CmdSetinfection()
	{
		aliases.add("seti");

		requiredArgs.add("playername");
		optionalArgs.put("amount", "1.0");
		
		helpShort = "set infection (0 to 100)";
		
		this.visibility = CommandVisibility.SECRET;
		
		permission = Permission.COMMAND_SETINFECTION.node;
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform()
	{
		Player you = this.argAsBestPlayerMatch(0);
		if (you == null) return;
		
		double amount = this.argAsDouble(1, 1.0);
		
		VPlayer vyou = VPlayers.i.get(you);
		vyou.setInfection(amount);
		this.msg(p.txt.parse(Lang.xNowHasYInfection, you.getDisplayName(), vyou.getInfection()));
	}
}
