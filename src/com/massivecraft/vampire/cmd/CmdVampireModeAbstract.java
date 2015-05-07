package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ARBoolean;
import com.massivecraft.vampire.cmdreq.ReqIsVampire;

public abstract class CmdVampireModeAbstract extends VCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireModeAbstract()
	{
		// Args
		this.addArg(ARBoolean.get(), "yes/no", "toggle");
		
		// Requirements
		this.addRequirements(ReqIsVampire.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		boolean val = this.readArg(! this.get());
		this.set(val);
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	protected abstract void set(boolean val);
	protected abstract boolean get();
	
}
