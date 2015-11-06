package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.command.MassiveCommand;
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
	public void senderFields(boolean set)
	{
		this.vme = set ? UPlayer.get(this.sender) : null;
	}
	
}
