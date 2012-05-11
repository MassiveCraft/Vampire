package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore2.cmd.MCommand;
import com.massivecraft.vampire.P;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.VPlayers;

public abstract class VCommand extends MCommand
{
	public P p;
	public VCommand()
	{
		super();
		this.p = P.p;
	}
	
	@Override
	public P p()
	{
		return P.p;
	}
	
	public VPlayer vme;
	@Override
	public void fixSenderVars()
	{
		this.vme = VPlayers.i.get(this.me);
	}
}
