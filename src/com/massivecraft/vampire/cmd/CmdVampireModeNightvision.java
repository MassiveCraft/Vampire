package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.entity.MConf;

import java.util.List;

public class CmdVampireModeNightvision extends CmdVampireModeAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireModeNightvision()
	{
		// Requirements
		this.addRequirements(new RequirementHasPerm(Perm.MODE_NIGHTVISION));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().aliasesVampireModeNightvision;
	}
	
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
