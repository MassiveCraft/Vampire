package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
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
		this.addRequirements(RequirementHasPerm.get(Perm.SHRIEK));
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
