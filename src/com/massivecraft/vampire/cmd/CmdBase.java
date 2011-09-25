package com.massivecraft.vampire.cmd;

import com.massivecraft.vampire.config.Conf;

public class CmdBase extends VCommand
{
	public CmdBase()
	{
		super();
		this.aliases.addAll(Conf.baseCommandAliases);
		
		this.subCommands.add(p.cmdHelp);
		this.subCommands.add(new CmdIntend());
		this.subCommands.add(new CmdList());
		this.subCommands.add(new CmdAdminInfect());
		this.subCommands.add(new CmdAdminTurn());
		this.subCommands.add(new CmdAdminCure());
		this.subCommands.add(new CmdAdminFeed());
		this.subCommands.add(new CmdVersion());
		
		this.helpShort = "The vampire base command";
		this.helpLong.add(p.txt.tags("<i>This command contains all vampire stuff."));
	}
	
	@Override
	public void perform()
	{
		this.commandChain.add(this);
		p.cmdHelp.execute(this.sender, this.args, this.commandChain);
	}

}
