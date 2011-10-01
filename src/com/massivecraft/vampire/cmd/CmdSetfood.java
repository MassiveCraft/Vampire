package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;
import com.massivecraft.vampire.config.Lang;
import com.massivecraft.vampire.zcore.CommandVisibility;

public class CmdSetfood extends VCommand
{
	public CmdSetfood()
	{
		aliases.add("setf");

		requiredArgs.add("playername");
		optionalArgs.put("food", "20");
		
		helpShort = "set foodlevel (0 to 20)";
		
		this.visibility = CommandVisibility.SECRET;
		
		permission = Permission.COMMAND_SETFOOD.node;
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform()
	{
		Player you = this.argAsBestPlayerMatch(0);
		if (you == null) return;
		
		int targetFood = this.argAsInt(1, 20);
		you.setFoodLevel(VPlayer.limitNumber(targetFood, 0, 20));
		
		this.msg(p.txt.parse(Lang.xNowHasFoodY, you.getDisplayName(), you.getFoodLevel()));
	}
}
