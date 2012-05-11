package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore2.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.*;
import com.massivecraft.vampire.cmdreq.ReqIsVampire;

public class CmdOffer extends VCommand
{
	public CmdOffer()
	{
		this.addAliases("offer");
		
		this.addRequiredArg("playername");
		
		this.addRequirements(ReqHasPerm.get(Permission.TRADE_OFFER.node));
		this.addRequirements(ReqIsVampire.get());
	}
	
	@Override
	public void perform()
	{
		Player you = this.argAs(0, Player.class, "match");
		if (you == null) return;
		VPlayer vyou = VPlayers.i.get(you);
		vme.infectionOffer(vyou);
	}
}
