package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.entity.MConf;

import java.util.List;

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
	public CmdVampireFlask cmdVampireDonate = new CmdVampireFlask();
	public CmdVampireShriek cmdVampireShriek = new CmdVampireShriek();
	public CmdVampireList cmdVampireList = new CmdVampireList();
	public CmdVampireSet cmdVampireSet = new CmdVampireSet();
	//public CmdVampireEditConfig cmdVampireEditConfig = new CmdVampireEditConfig();
	public CmdVampireEditLang cmdVampireEditLang = new CmdVampireEditLang();
	public CmdVampireVersion cmdVampireVersion = new CmdVampireVersion();
	
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
		//this.addChild(this.cmdVampireEditConfig);
		this.addChild(this.cmdVampireEditLang);
		this.addChild(this.cmdVampireVersion);
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.BASECOMMAND));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().getAliasesVampire();
	}
	
}
