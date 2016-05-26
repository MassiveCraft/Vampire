package com.massivecraft.vampire;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.Identified;
import com.massivecraft.massivecore.util.PermissionUtil;

public enum Perm implements Identified
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	BASECOMMAND,
	
	SHOW,
	SHOW_OTHER,
	SHRIEK,
	MODE_BLOODLUST,
	MODE_INTENT,
	MODE_NIGHTVISION,
	TRADE_OFFER,
	TRADE_ACCEPT,
	COMBAT_INFECT,
	COMBAT_CONTRACT,
	LIST,
	SET,
	SET_VAMPIRE_TRUE,
	SET_VAMPIRE_FALSE,
	SET_INFECTION,
	SET_FOOD,
	SET_HEALTH,
	VERSION,
	
	ALTAR_DARK,
	ALTAR_LIGHT,
	
	IS_VAMPIRE,
	IS_HUMAN,
	
	// END OF LIST
	;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String id;
	@Override public String getId() { return this.id; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	Perm()
	{
		this.id = PermissionUtil.createPermissionId(Vampire.get(), this);
    }
	
	// -------------------------------------------- //
	// HAS
	// -------------------------------------------- //
	
	public boolean has(CommandSender sender, boolean informSenderIfNot)
	{
		return PermissionUtil.hasPermission(sender, this.id, informSenderIfNot);
	}
	
	public boolean has(CommandSender sender)
	{
		return has(sender, false);
	}
	
}
