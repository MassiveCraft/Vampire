package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.entity.MConf;

import java.util.List;

public class CmdVampireModeBloodlust extends CmdVampireModeAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireModeBloodlust()
	{
		// Requirements
		this.addRequirements(new RequirementHasPerm(Perm.MODE_BLOODLUST));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().getAliasesVampireModeBloodlust();
	}
	
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
