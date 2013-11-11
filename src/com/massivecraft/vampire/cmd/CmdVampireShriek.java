package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.*;
import com.massivecraft.vampire.cmdreq.ReqIsVampire;

public class CmdVampireShriek extends VCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireShriek()
	{
		// Aliases
		this.addAliases("shriek");
		
		// Requirements
		this.addRequirements(ReqIsVampire.get());
		this.addRequirements(ReqHasPerm.get(Perm.SHRIEK.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		vme.shriek();
	}
	
}
