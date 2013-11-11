package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.vampire.entity.UPlayer;

public abstract class VCommand extends MCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public UPlayer vme;
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void fixSenderVars()
	{
		this.vme = UPlayer.get(this.sender);
	}
	
}
