package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore1.cmd.VisibilityMode;
import com.massivecraft.mcore1.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.P;
import com.massivecraft.vampire.Permission;

public class CmdVersion extends VCommand
{
	
	public CmdVersion()
	{
		this.addAliases("version");
		
		this.setDesc("display current version");
		
		this.setVisibilityMode(VisibilityMode.SECRET);
		
		this.setDescPermission(Permission.COMMAND_VERSION.node);
		this.addRequirements(new ReqHasPerm(Permission.COMMAND_VERSION.node));
	}
	
	@Override
	public void perform()
	{
		this.msg("<i>You are running %s", P.p.getDescription().getFullName());
	}
}
