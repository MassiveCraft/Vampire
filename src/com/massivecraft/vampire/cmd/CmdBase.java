package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore5.cmd.HelpCommand;
import com.massivecraft.vampire.ConfServer;

public class CmdBase extends VCommand
{
	public CmdShow cmdShow = new CmdShow();
	public CmdModeBloodlust cmdModeBloodlust = new CmdModeBloodlust();
	public CmdModeIntend cmdModeIntend = new CmdModeIntend();
	public CmdModeNightvision cmdModeNightvision = new CmdModeNightvision();
	public CmdOffer cmdOffer = new CmdOffer();
	public CmdAccept cmdAccept = new CmdAccept();
	public CmdShriek cmdShriek = new CmdShriek();
	public CmdVersion cmdVersion = new CmdVersion();
	public CmdList cmdList = new CmdList();
	public CmdSet cmdSet = new CmdSet();
	
	public CmdBase()
	{
		super();
		this.addAliases(ConfServer.baseCommandAliases);
		
		this.addSubCommand(HelpCommand.getInstance());
		this.addSubCommand(cmdShow);
		this.addSubCommand(cmdModeBloodlust);
		this.addSubCommand(cmdModeIntend);
		this.addSubCommand(cmdModeNightvision);
		this.addSubCommand(cmdOffer);
		this.addSubCommand(cmdAccept);
		this.addSubCommand(cmdShriek);
		this.addSubCommand(cmdVersion);
		this.addSubCommand(cmdList);
		this.addSubCommand(cmdSet);
		
		this.setDesc("The vampire base command");
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}
