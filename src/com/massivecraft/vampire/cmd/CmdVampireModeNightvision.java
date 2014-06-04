package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.*;

public class CmdVampireModeNightvision extends CmdVampireModeAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireModeNightvision()
	{
		// Aliases
		this.addAliases("n", "nightvision");
		
		// Requirements
		this.addRequirements(new ReqHasPerm(Perm.MODE_NIGHTVISION.node));
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
