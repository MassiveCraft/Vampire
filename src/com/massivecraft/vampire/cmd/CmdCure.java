package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;
import com.massivecraft.vampire.config.Lang;
import com.massivecraft.vampire.zcore.CommandVisibility;


public class CmdCure extends VCommand
{
	public CmdCure()
	{
		aliases.add("cure");

		requiredArgs.add("playername");
		
		helpShort = "cure a vampire";
		
		this.visibility = CommandVisibility.SECRET;
		
		permission = Permission.COMMAND_CURE.node;
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform()
	{
		Player you = this.argAsBestPlayerMatch(0);
		if (you == null) return;
		VPlayer vyou = VPlayers.i.get(you);
		vyou.cureVampirism();
		this.msg(p.txt.parse(Lang.xWasCured, you.getDisplayName()));
	}
}