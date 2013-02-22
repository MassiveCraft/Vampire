package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore.cmd.HelpCommand;
import com.massivecraft.mcore.cmd.VisibilityMode;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.VPerm;

public class CmdSet extends VCommand
{
	public CmdSetVampire cmdSetVampire = new CmdSetVampire();
	public CmdSetInfection cmdSetInfection = new CmdSetInfection();
	public CmdSetFood cmdSetFood = new CmdSetFood();
	public CmdSetHealth cmdSetHealth = new CmdSetHealth();
	
	
	public CmdSet()
	{
		super();
		this.addAliases("set");
		
		this.addSubCommand(cmdSetVampire);
		this.addSubCommand(cmdSetInfection);
		this.addSubCommand(cmdSetFood);
		this.addSubCommand(cmdSetHealth);
		
		this.setVisibilityMode(VisibilityMode.SECRET);
		this.addRequirements(ReqHasPerm.get(VPerm.SET.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}
