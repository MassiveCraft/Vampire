package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.command.editor.CommandEditSingleton;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.entity.MConf;
import com.massivecraft.vampire.entity.MLang;

import java.util.List;

public class CmdVampireEditLang extends CommandEditSingleton<MLang>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireEditLang()
	{
		super(MLang.get());
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.LANG));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().aliasesVampireEditLang;
	}
	
}
