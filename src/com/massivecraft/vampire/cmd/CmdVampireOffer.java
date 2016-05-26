package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeDouble;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import com.massivecraft.massivecore.util.MUtil;
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
		this.addAliases("offer");
		
		// Parameters
		this.addParameter(TypePlayer.get(), "player");
		this.addParameter(TypeDouble.get(), "amount", "4.0");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.TRADE_OFFER));
		this.addRequirements(RequirementIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Player you = this.readArg();
		UPlayer vyou = UPlayer.get(you);
		
		double unlimitedAmount = this.readArg(4D);
		
		double amount = MUtil.limitNumber(unlimitedAmount, 0D, 20D);
		if (amount != unlimitedAmount)
		{
			msg("<b>amount must be between 0.0 and 20.0");
			return;
		}
		
		vme.tradeOffer(vyou, amount);
	}
	
}
