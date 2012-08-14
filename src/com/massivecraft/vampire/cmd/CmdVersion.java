package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore4.cmd.VisibilityMode;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.P;
import com.massivecraft.vampire.Permission;

public class CmdVersion extends VCommand
{
	public CmdVersion()
	{
		this.addAliases("v", "version");
		this.setVisibilityMode(VisibilityMode.SECRET);
		this.addRequirements(ReqHasPerm.get(Permission.VERSION.node));
	}
	
	@Override
	public void perform()
	{
		this.msg("<i>You are running %s", P.p.getDescription().getFullName());
	}
}
