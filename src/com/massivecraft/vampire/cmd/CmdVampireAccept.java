package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.vampire.Perm;

public class CmdVampireAccept extends VCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireAccept()
	{
		// Aliases
		this.addAliases("accept");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.TRADE_ACCEPT.node));
		this.addRequirements(RequirementIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		vme.tradeAccept();
	}
	
}
