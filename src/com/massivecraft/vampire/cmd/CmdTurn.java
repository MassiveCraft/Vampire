package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;
import com.massivecraft.vampire.config.Lang;
import com.massivecraft.vampire.zcore.CommandVisibility;

public class CmdTurn extends VCommand
{

	public CmdTurn()
	{
		aliases.add("turn");

		requiredArgs.add("playername");
		
		helpShort = "instantly turn player";
		
		this.visibility = CommandVisibility.SECRET;
		
		permission = Permission.COMMAND_TURN.node;
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform()
	{
		Player you = this.argAsBestPlayerMatch(0);
		if (you == null) return;
		VPlayer vyou = VPlayers.i.get(you);
		
		you.setFoodLevel(20);
		vyou.turn();
		
		this.msg(p.txt.parse(Lang.xWasTurned, you.getDisplayName()));
	}
}
