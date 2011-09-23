package com.massivecraft.vampire.cmd;

public class CmdBase extends VCommand
{
	public CmdFeed cmdFeed = new CmdFeed();
	public CmdInfect cmdInfect = new CmdInfect();
	public CmdTurn cmdTurn = new CmdTurn();
	public CmdCure cmdCure = new CmdCure();
	public CmdList cmdList = new CmdList();
	public CmdVersion cmdVersion = new CmdVersion();
	
	public CmdBase()
	{
		super();
		this.aliases.add("v");
		
		this.subCommands.add(p.cmdHelp);
		this.subCommands.add(this.cmdFeed);
		this.subCommands.add(this.cmdInfect);
		this.subCommands.add(this.cmdTurn);
		this.subCommands.add(this.cmdCure);
		this.subCommands.add(this.cmdList);
		this.subCommands.add(this.cmdVersion);
		
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
