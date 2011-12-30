package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore1.cmd.VisibilityMode;
import com.massivecraft.mcore1.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.*;
import com.massivecraft.vampire.config.Lang;


public class CmdSetinfection extends VCommand
{
	public CmdSetinfection()
	{
		this.addAliases("seti");

		requiredArgs.add("playername");
		optionalArgs.put("amount", "1.0");
		
		this.setDesc("set infection (0 to 100)");
		
		this.setVisibilityMode(VisibilityMode.SECRET);
		
		this.setDescPermission(Permission.COMMAND_SETINFECTION.node);
		this.addRequirements(new ReqHasPerm(Permission.COMMAND_SETINFECTION.node));
	}
	
	@Override
	public void perform()
	{
		Player you = this.argAs(0, Player.class, "match");
		if (you == null) return;
		
		Double amount = this.argAs(1, Double.class, 1.0);
		if (amount == null) return;
		
		VPlayer vyou = VPlayers.i.get(you);
		vyou.setInfection(amount);
		this.msg(Lang.xNowHasYInfection, you.getDisplayName(), vyou.getInfection());
	}
}
