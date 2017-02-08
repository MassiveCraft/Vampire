package com.massivecraft.vampire.entity;

import java.util.List;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.command.editor.annotation.EditorType;
import com.massivecraft.massivecore.command.type.TypeMillisDiff;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

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
	
	public List<String> aliasesV = MUtil.list("v");
	
	public List<String> aliasesEditConfigInner = MUtil.list("config");
	
	public List<String> aliasesEditLangInner = MUtil.list("lang");

	@EditorType(TypeMillisDiff.class)
	public long taskDelayMillis = 500L; // Half a second 
	
	// Should the vampire strength be allowed together with skull splitter.
	public boolean combatDamageFactorWithMcmmoAbilities = false;

}
