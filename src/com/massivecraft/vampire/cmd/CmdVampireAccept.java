package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.vampire.Perm;

public class CmdVampireAccept extends VCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireAccept()
	{
		// Aliases
		this.addAliases("a", "accept");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.TRADE_ACCEPT.node));
		this.addRequirements(ReqIsPlayer.get());
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
