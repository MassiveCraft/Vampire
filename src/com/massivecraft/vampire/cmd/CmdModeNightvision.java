package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.*;

public class CmdModeNightvision extends CmdModeAbstract
{	
	public CmdModeNightvision()
	{
		this.addAliases("n", "nightvision");
		this.addRequirements(new ReqHasPerm(VPerm.MODE_NIGHTVISION.node));
	}
	
	protected void set(boolean val)
	{
		vme.setUsingNightVision(val);
	}
	
	protected boolean get()
	{
		return vme.isUsingNightVision();
	}
}
