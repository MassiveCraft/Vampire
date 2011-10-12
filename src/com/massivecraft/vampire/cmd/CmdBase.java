package com.massivecraft.vampire.cmd;

import com.massivecraft.vampire.config.Conf;

public class CmdBase extends VCommand
{
	public CmdAccept cmdAccept = new CmdAccept();
	
	public CmdBase()
	{
		super();
		this.aliases.addAll(Conf.baseCommandAliases);
		this.allowNoSlashAccess = Conf.allowNoSlashCommand;
		
		this.subCommands.add(p.cmdHelp);
		this.subCommands.add(new CmdIntend());
		this.subCommands.add(new CmdInfect());
		this.subCommands.add(cmdAccept);
		this.subCommands.add(new CmdList());
		this.subCommands.add(new CmdSetfood());
		this.subCommands.add(new CmdSetinfection());
		this.subCommands.add(new CmdTurn());
		this.subCommands.add(new CmdCure());
		this.subCommands.add(new CmdVersion());
		
		this.setHelpShort("The vampire base command");
		this.helpLong.add(p.txt.parse("<i>This command contains all vampire stuff."));
	}
	
	@Override
	public void perform()
	{
		this.commandChain.add(this);
		p.cmdHelp.execute(this.sender, this.args, this.commandChain);
	}

}
