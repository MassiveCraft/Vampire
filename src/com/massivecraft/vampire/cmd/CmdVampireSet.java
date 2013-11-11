package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore.cmd.VisibilityMode;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.Perm;

public class CmdVampireSet extends VCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdVampireSetVampire cmdVampireSetVampire = new CmdVampireSetVampire();
	public CmdVampireSetInfection cmdVampireSetInfection = new CmdVampireSetInfection();
	public CmdVampireSetFood cmdVampireSetFood = new CmdVampireSetFood();
	public CmdVampireSetHealth cmdVampireSetHealth = new CmdVampireSetHealth();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireSet()
	{
		// Add SubCommands
		this.addSubCommand(this.cmdVampireSetVampire);
		this.addSubCommand(this.cmdVampireSetInfection);
		this.addSubCommand(this.cmdVampireSetFood);
		this.addSubCommand(this.cmdVampireSetHealth);
		
		// Aliases
		this.addAliases("set");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.SET.node));
		
		this.setVisibilityMode(VisibilityMode.SECRET);
	}
	
}
