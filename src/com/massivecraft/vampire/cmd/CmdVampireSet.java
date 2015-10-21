package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.cmd.Visibility;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
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
		// Children
		this.addChild(this.cmdVampireSetVampire);
		this.addChild(this.cmdVampireSetInfection);
		this.addChild(this.cmdVampireSetFood);
		this.addChild(this.cmdVampireSetHealth);
		
		// Aliases
		this.addAliases("set");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.SET.node));
		
		// Visibility
		this.setVisibility(Visibility.SECRET);
	}
	
}
