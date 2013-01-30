package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore5.cmd.MCommand;
import com.massivecraft.vampire.VPlayer;

public abstract class VCommand extends MCommand
{
	public VPlayer vme;
	
	@Override
	public void fixSenderVars()
	{
		this.vme = VPlayer.get(this.sender);
	}
}
