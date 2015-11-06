package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.vampire.*;

public class CmdVampireModeBloodlust extends CmdVampireModeAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireModeBloodlust()
	{
		// Aliases
		this.addAliases("bloodlust");
		
		// Requirements
		this.addRequirements(new RequirementHasPerm(Perm.MODE_BLOODLUST.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	protected void set(boolean val)
	{
		vme.setBloodlusting(val);
	}
	
	@Override
	protected boolean get()
	{
		return vme.isBloodlusting();
	}
	
}
