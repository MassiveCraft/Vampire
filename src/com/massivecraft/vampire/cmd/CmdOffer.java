package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore4.cmd.arg.ARDouble;
import com.massivecraft.mcore4.cmd.arg.AROnlinePlayerMatch;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;
import com.massivecraft.mcore4.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore4.util.MUtil;
import com.massivecraft.vampire.*;

public class CmdOffer extends VCommand
{
	public CmdOffer()
	{
		this.addAliases("o", "offer");
		
		this.addRequiredArg("playername");
		this.addOptionalArg("amount", "4.0");
		
		this.addRequirements(ReqHasPerm.get(Permission.TRADE_OFFER.node));
		this.addRequirements(ReqIsPlayer.get());
	}
	
	@Override
	public void perform()
	{
		Player you = this.arg(0, AROnlinePlayerMatch.get());
		if (you == null) return;
		VPlayer vyou = VPlayer.get(you);
		
		Double unlimitedAmount = this.arg(1, ARDouble.get(), 4D);
		if (unlimitedAmount == null) return;
		
		double amount = MUtil.limitNumber(unlimitedAmount, 0D, 20D);
		if (amount != unlimitedAmount)
		{
			msg("<b>amount must be between 0.0 and 20.0");
			return;
		}
		
		vme.tradeOffer(vyou, amount);
	}
}
