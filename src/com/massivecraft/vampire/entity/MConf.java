package com.massivecraft.vampire.entity;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.command.editor.annotation.EditorType;
import com.massivecraft.massivecore.command.type.TypeMillisDiff;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

@EditorName("config")
public class MConf extends Entity<MConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MConf i;
	public static MConf get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Aliases
	public List<String> aliasesVampire = MUtil.list("v", "vampire");
	public List<String> aliasesVampireShow = MUtil.list("show");
	public List<String> aliasesVampireModeBloodlust = MUtil.list("bloodlust");
	public List<String> aliasesVampireModeIntend = MUtil.list("intend");
	public List<String> aliasesVampireModeNightvision = MUtil.list("nv", "nightvision");
	public List<String> aliasesVampireOffer = MUtil.list("offer");
	public List<String> aliasesVampireAccept = MUtil.list("accept");
	public List<String> aliasesVampireFlask = MUtil.list("flask");
	public List<String> aliasesVampireShriek = MUtil.list("shriek");
	public List<String> aliasesVampireList = MUtil.list("list");
	public List<String> aliasesVampireSet = MUtil.list("set");
	public List<String> aliasesVampireSetVampire = MUtil.list("vampire");
	public List<String> aliasesVampireSetInfection = MUtil.list("infection");
	public List<String> aliasesVampireSetFood = MUtil.list("food");
	public List<String> aliasesVampireSetHealth = MUtil.list("health");
	public List<String> aliasesVampireEditConfig = MUtil.list("config");
	public List<String> aliasesVampireEditLang = MUtil.list("lang");
	public List<String> aliasesVampireVersion = MUtil.list("v", "version");

	@EditorType(TypeMillisDiff.class)
	public long taskDelayMillis = 500L; // Half a second 
	
	// Should the vampire strength be allowed together with skull splitter.
	public boolean combatDamageFactorWithMcmmoAbilities = false;

}
