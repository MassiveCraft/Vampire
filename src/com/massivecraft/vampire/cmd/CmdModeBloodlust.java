package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore4.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.*;

public class CmdModeBloodlust extends CmdModeAbstract
{	
	public CmdModeBloodlust()
	{
		this.addAliases("b", "bloodlust");
		this.addRequirements(new ReqHasPerm(Permission.MODE_BLOODLUST.node));
	}
	
	protected void set(boolean val)
	{
		vme.setBloodlusting(val);
	}
	
	protected boolean get()
	{
		return vme.isBloodlusting();
	}
}
