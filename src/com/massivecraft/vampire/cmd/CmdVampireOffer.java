package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore.cmd.arg.ARDouble;
import com.massivecraft.mcore.cmd.arg.ARPlayer;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.vampire.*;
import com.massivecraft.vampire.entity.UPlayer;

public class CmdVampireOffer extends VCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireOffer()
	{
		// Aliases
		this.addAliases("o", "offer");
		
		// Args
		this.addRequiredArg("playername");
		this.addOptionalArg("amount", "4.0");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.TRADE_OFFER.node));
		this.addRequirements(ReqIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		Player you = this.arg(0, ARPlayer.getStart());
		if (you == null) return;
		UPlayer vyou = UPlayer.get(you);
		
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
