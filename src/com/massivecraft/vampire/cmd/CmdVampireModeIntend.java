package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.*;

public class CmdVampireModeIntend extends CmdVampireModeAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireModeIntend()
	{
		// Aliases
		this.addAliases("i", "intend");
		
		// Requirements
		this.addRequirements(new ReqHasPerm(Perm.MODE_INTENT.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	protected void set(boolean val)
	{
		vme.setIntending(val);
	}
	
	@Override
	protected boolean get()
	{
		return vme.isIntending();
	}
	
}
