package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore1.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.*;
import com.massivecraft.vampire.cmd.req.ReqIsVampire;

public class CmdInfect extends VCommand
{
	public CmdInfect()
	{
		this.addAliases("infect");
		
		requiredArgs.add("playername");
		
		this.setDesc("infect others that are willing");
		
		this.setDescPermission(Permission.COMMAND_INFECT.node);
		this.addRequirements(new ReqHasPerm(Permission.COMMAND_INFECT.node));
		this.addRequirements(ReqIsVampire.getInstance());
	}
	
	@Override
	public void perform()
	{
		Player you = this.argAs(0, Player.class, "match");
		if (you == null) return;
		VPlayer vyou = VPlayers.i.get(you);
		vme.offerInfectionTo(vyou);
	}
}
