package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore.cmd.VisibilityMode;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.P;
import com.massivecraft.vampire.VPerm;

public class CmdVersion extends VCommand
{
	public CmdVersion()
	{
		this.addAliases("v", "version");
		this.setVisibilityMode(VisibilityMode.SECRET);
		this.addRequirements(ReqHasPerm.get(VPerm.VERSION.node));
	}
	
	@Override
	public void perform()
	{
		this.msg("<i>You are running %s", P.p.getDescription().getFullName());
	}
}
