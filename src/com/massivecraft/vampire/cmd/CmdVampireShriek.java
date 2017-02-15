package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.cmdreq.ReqIsVampire;
import com.massivecraft.vampire.entity.MConf;

import java.util.List;

public class CmdVampireShriek extends VCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireShriek()
	{
		// Requirements
		this.addRequirements(ReqIsVampire.get());
		this.addRequirements(RequirementHasPerm.get(Perm.SHRIEK));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().aliasesVampireShriek;
	}
	
	@Override
	public void perform()
	{
		vme.shriek();
	}
	
}
