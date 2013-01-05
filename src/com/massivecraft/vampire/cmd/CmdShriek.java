package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore5.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.*;
import com.massivecraft.vampire.cmdreq.ReqIsVampire;

public class CmdShriek extends VCommand
{
	public CmdShriek()
	{
		this.addAliases("shriek");
		
		this.addRequirements(ReqIsVampire.get());
		this.addRequirements(ReqHasPerm.get(VPerm.SHRIEK.node));
	}
	
	@Override
	public void perform()
	{
		vme.shriek();
	}
}
