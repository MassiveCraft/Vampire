package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore1.cmd.VisibilityMode;
import com.massivecraft.mcore1.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.*;
import com.massivecraft.vampire.config.Lang;

public class CmdTurn extends VCommand
{

	public CmdTurn()
	{
		this.addAliases("turn");

		requiredArgs.add("playername");
		
		this.setDesc("instantly turn player");
		
		this.setVisibilityMode(VisibilityMode.SECRET);
		
		this.setDescPermission(Permission.COMMAND_TURN.node);
		this.addRequirements(new ReqHasPerm(Permission.COMMAND_TURN.node));
	}
	
	@Override
	public void perform()
	{
		Player you = this.argAs(0, Player.class, "match");
		if (you == null) return;
		VPlayer vyou = VPlayers.i.get(you);
		
		you.setFoodLevel(20);
		vyou.turn();
		
		this.msg(Lang.xWasTurned, you.getDisplayName());
	}
}
