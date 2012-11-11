package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore5.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.*;

public class CmdModeNightvision extends CmdModeAbstract
{	
	public CmdModeNightvision()
	{
		this.addAliases("n", "nightvision");
		this.addRequirements(new ReqHasPerm(Permission.MODE_NIGHTVISION.node));
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
