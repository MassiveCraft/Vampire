package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore1.cmd.VisibilityMode;
import com.massivecraft.vampire.*;
import com.massivecraft.vampire.config.Lang;

public class CmdSetfood extends VCommand
{
	public CmdSetfood()
	{
		this.addAliases("setf");

		requiredArgs.add("playername");
		optionalArgs.put("food", "20");
		
		this.setDesc("set foodlevel (0 to 20)");
		
		this.setVisibilityMode(VisibilityMode.SECRET);
		
		this.setDescPermission(Permission.COMMAND_SETFOOD.node);
	}
	
	@Override
	public void perform()
	{
		Player you = this.argAs(0, Player.class, "match");
		if (you == null) return;
		
		Integer targetFood = this.argAs(1, Integer.class, 20);
		if (targetFood == null) return;
		
		you.setFoodLevel(VPlayer.limitNumber(targetFood, 0, 20));
		
		this.msg(Lang.xNowHasFoodY, you.getDisplayName(), you.getFoodLevel());
	}
}
