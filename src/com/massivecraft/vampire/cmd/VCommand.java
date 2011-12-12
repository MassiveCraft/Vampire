package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore1.cmd.MCommand;
import com.massivecraft.vampire.P;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.VPlayers;

public abstract class VCommand extends MCommand
{
	public VCommand()
	{
		super();
	}
	
	@Override
	public P p()
	{
		return P.p;
	}
	
	public VPlayer vme()
	{
		return VPlayers.i.get(this.getMe());
	}
}
