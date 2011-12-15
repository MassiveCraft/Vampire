package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore1.cmd.HelpCommand;
import com.massivecraft.vampire.config.Conf;

public class CmdBase extends VCommand
{
	public CmdAccept cmdAccept = new CmdAccept();
	
	public CmdBase()
	{
		super();
		this.addAliases(Conf.baseCommandAliases);
		this.addSubCommand(HelpCommand.getInstance());
		this.addSubCommand(new CmdIntend());
		this.addSubCommand(new CmdInfect());
		this.addSubCommand(cmdAccept);
		this.addSubCommand(new CmdList());
		this.addSubCommand(new CmdSetfood());
		this.addSubCommand(new CmdSetinfection());
		this.addSubCommand(new CmdTurn());
		this.addSubCommand(new CmdCure());
		this.addSubCommand(new CmdVersion());
		
		this.setDesc("The vampire base command");
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}
