package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;


public class CmdCure extends VCommand
{
	public CmdCure()
	{
		aliases.add("cure");

		requiredArgs.add("playername");
		
		helpShort = "cure a vampire";
		
		permission = Permission.COMMAND_CURE.node;
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform()
	{
		Player you = this.argAsBestPlayerMatch(0);
		if (you == null) return;
		
		this.msg(you.getDisplayName() + " was cured from vampirism.");
		VPlayer vyou = VPlayers.i.get(you);
		vyou.cure();
	}
}