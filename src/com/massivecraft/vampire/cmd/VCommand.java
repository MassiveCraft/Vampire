package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.vampire.entity.UPlayer;

public abstract class VCommand extends MassiveCommand
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
