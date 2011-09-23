package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;
import com.massivecraft.vampire.*;

public class CmdTurn extends VCommand
{

	public CmdTurn() {	
		aliases.add("turn");

		requiredArgs.add("playername");
		optionalArgs.put("isTrueBlood", "0");
		
		helpShort = "instantly turn player";
		
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
		
		if(vyou.isVampire())
		{
			this.msg(you.getDisplayName() + " is already a vampire.");
			return;
		}
		
		you.setFoodLevel(20);
		
		//If there is an optional paramater that is "trueblood", then turn the human into TrueBlood vampire
		if(this.argAsBool(1, false))
		{
			vyou.setIsTrueBlood(true);
			this.msg(you.getDisplayName() + " was turned into a True Blood vampire.");
		}
		else
		{
			this.msg(you.getDisplayName() + " was turned into a vampire.");
		}
		
		vyou.turn();
	}
}
