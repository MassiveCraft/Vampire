package com.massivecraft.vampire.cmd;

public class CmdBase extends VCommand
{
	public CmdIntent cmdIntent = new CmdIntent();
	public CmdList cmdList = new CmdList();
	public CmdAdminFeed cmdFeed = new CmdAdminFeed();
	public CmdAdminInfect cmdInfect = new CmdAdminInfect();
	public CmdAdminTurn cmdTurn = new CmdAdminTurn();
	public CmdAdminCure cmdCure = new CmdAdminCure();
	public CmdVersion cmdVersion = new CmdVersion();
	
	
	public CmdBase()
	{
		super();
		this.aliases.add("v");
		
		this.subCommands.add(p.cmdHelp);
		this.subCommands.add(this.cmdIntent);
		this.subCommands.add(this.cmdList);
		this.subCommands.add(this.cmdInfect);
		this.subCommands.add(this.cmdTurn);
		this.subCommands.add(this.cmdCure);
		this.subCommands.add(this.cmdFeed);
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
