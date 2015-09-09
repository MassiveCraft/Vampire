package com.massivecraft.vampire.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.VersionCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.Vampire;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.entity.MConf;

public class CmdVampire extends VCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdVampireShow cmdVampireShow = new CmdVampireShow();
	public CmdVampireModeBloodlust cmdVampireModeBloodlust = new CmdVampireModeBloodlust();
	public CmdVampireModeIntend cmdVampireModeIntend = new CmdVampireModeIntend();
	public CmdVampireModeNightvision cmdVampireModeNightvision = new CmdVampireModeNightvision();
	public CmdVampireOffer cmdVampireOffer = new CmdVampireOffer();
	public CmdVampireAccept cmdVampireAccept = new CmdVampireAccept();
	public CmdVampireShriek cmdVampireShriek = new CmdVampireShriek();
	public CmdVampireList cmdVampireList = new CmdVampireList();
	public CmdVampireSet cmdVampireSet = new CmdVampireSet();
	public VersionCommand cmdVersion = new VersionCommand(Vampire.get(), Perm.VERSION.node, "v", "version");
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampire()
	{
		// Add SubCommands
		this.addSubCommand(this.cmdVampireShow);
		this.addSubCommand(this.cmdVampireModeBloodlust);
		this.addSubCommand(this.cmdVampireModeIntend);
		this.addSubCommand(this.cmdVampireModeNightvision);
		this.addSubCommand(this.cmdVampireOffer);
		this.addSubCommand(this.cmdVampireAccept);
		this.addSubCommand(this.cmdVampireShriek);
		this.addSubCommand(this.cmdVampireList);
		this.addSubCommand(this.cmdVampireSet);
		this.addSubCommand(this.cmdVersion);
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.BASECOMMAND.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().aliasesV;
	}
	
}
