package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.entity.MConf;

import java.util.List;

public class CmdVampireModeIntend extends CmdVampireModeAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireModeIntend()
	{
		// Requirements
		this.addRequirements(new RequirementHasPerm(Perm.MODE_INTENT));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().aliasesVampireModeIntend;
	}
	
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
