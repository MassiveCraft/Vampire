package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;

public class CmdFeed extends VCommand
{
	public CmdFeed() {
		aliases.add("feed");

		requiredArgs.add("playername");
		optionalArgs.put("blood", "100");
		
		helpShort = "feed a vampire";
		
		permission = Permission.COMMAND_FEED.node;
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform()
	{
		Player you = this.argAsBestPlayerMatch(0);
		if (you == null) return;
		
		VPlayer vyou = VPlayers.i.get(you);
		
		if ( ! vyou.isVampire()) {
			this.msg(you.getDisplayName() + " is not a vampire.");
			return;
		}
		
		double blood = this.argAsDouble(1, 100D);

		String msg = you.getDisplayName() + " was fed from " + String.format("%1$.1f", vyou.bloodGet());
		vyou.bloodAlter(blood);
		msg += " to " + String.format("%1$.1f", vyou.bloodGet()) + " blood.";
		
		this.msg(msg);
	}
}
