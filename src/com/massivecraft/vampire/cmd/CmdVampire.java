package com.massivecraft.vampire.cmd;

import java.util.List;

import com.massivecraft.massivecore.command.MassiveCommandVersion;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.vampire.Vampire;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.entity.MConf;

public class CmdVampire extends VCommand
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdVampire i = new CmdVampire();
	public static CmdVampire get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdVampireShow cmdVampireShow = new CmdVampireShow();
	public CmdVampireModeBloodlust cmdVampireModeBloodlust = new CmdVampireModeBloodlust();
	public CmdVampireModeIntend cmdVampireModeIntend = new CmdVampireModeIntend();
	public CmdVampireModeNightvision cmdVampireModeNightvision = new CmdVampireModeNightvision();
	public CmdVampireOffer cmdVampireOffer = new CmdVampireOffer();
	public CmdVampireAccept cmdVampireAccept = new CmdVampireAccept();
	public CmdVampireDonate cmdVampireDonate = new CmdVampireDonate();
	public CmdVampireShriek cmdVampireShriek = new CmdVampireShriek();
	public CmdVampireList cmdVampireList = new CmdVampireList();
	public CmdVampireSet cmdVampireSet = new CmdVampireSet();
	public MassiveCommandVersion cmdVersion = new MassiveCommandVersion(Vampire.get()).setAliases("v", "version").addRequirements(RequirementHasPerm.get(Perm.VERSION));
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampire()
	{
		// Children
		this.addChild(this.cmdVampireShow);
		this.addChild(this.cmdVampireModeBloodlust);
		this.addChild(this.cmdVampireModeIntend);
		this.addChild(this.cmdVampireModeNightvision);
		this.addChild(this.cmdVampireOffer);
		this.addChild(this.cmdVampireAccept);
		this.addChild(this.cmdVampireDonate);
		this.addChild(this.cmdVampireShriek);
		this.addChild(this.cmdVampireList);
		this.addChild(this.cmdVampireSet);
		this.addChild(this.cmdVersion);
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.BASECOMMAND));
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
