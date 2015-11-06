package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.vampire.*;

public class CmdVampireModeNightvision extends CmdVampireModeAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireModeNightvision()
	{
		// Aliases
		this.addAliases("nv", "nightvision");
		
		// Requirements
		this.addRequirements(new RequirementHasPerm(Perm.MODE_NIGHTVISION.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	protected void set(boolean val)
	{
		vme.setUsingNightVision(val);
	}
	
	@Override
	protected boolean get()
	{
		return vme.isUsingNightVision();
	}
	
}
