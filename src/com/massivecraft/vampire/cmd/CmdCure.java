package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore1.cmd.VisibilityMode;
import com.massivecraft.mcore1.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.*;
import com.massivecraft.vampire.config.Lang;


public class CmdCure extends VCommand
{
	public CmdCure()
	{
		this.addAliases("cure");

		requiredArgs.add("playername");
		
		this.setDesc("cure a vampire");
		this.setVisibilityMode(VisibilityMode.SECRET);
		
		this.setDescPermission(Permission.COMMAND_CURE.node);
		this.addRequirements(new ReqHasPerm(Permission.COMMAND_CURE.node));
	}
	
	@Override
	public void perform()
	{
		Player you = this.argAs(0, Player.class, "match");
		if (you == null) return;
		VPlayer vyou = VPlayers.i.get(you);
		if (vyou.isHealthy())
		{
			this.msg(Lang.xIsHealthy, you.getDisplayName());
			return;
		}
		
		vyou.cureVampirism();
		this.msg(Lang.xWasCured, you.getDisplayName());
	}
}