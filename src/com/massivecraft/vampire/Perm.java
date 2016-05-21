package com.massivecraft.vampire;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.PermissionUtil;

public enum Perm
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	BASECOMMAND("basecommand"),
	
	SHOW("show"),
	SHOW_OTHER("show.other"),
	SHRIEK("shriek"),
	MODE_BLOODLUST("mode.bloodlust"),
	MODE_INTENT("mode.intent"),
	MODE_NIGHTVISION("mode.nightvision"),
	TRADE_OFFER("trade.offer"),
	TRADE_ACCEPT("trade.accept"),
	COMBAT_INFECT("combat.infect"),
	COMBAT_CONTRACT("combat.contract"),
	LIST("list"),
	SET("set"),
	SET_VAMPIRE_TRUE("set.vampire.true"),
	SET_VAMPIRE_FALSE("set.vampire.false"),
	SET_INFECTION("set.infection"),
	SET_FOOD("set.food"),
	SET_HEALTH("set.health"),
	VERSION("version"),
	
	ALTAR_DARK("altar.dark"),
	ALTAR_LIGHT("altar.light"),
	
	IS_VAMPIRE("is.vampire"),
	IS_HUMAN("is.human"),
	
	// END OF LIST
	;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public final String node;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	Perm(final String permissionNode)
	{
		this.node = "vampire."+permissionNode;
    }
	
	// -------------------------------------------- //
	// HAS
	// -------------------------------------------- //
	
	public boolean has(CommandSender sender, boolean informSenderIfNot)
	{
		return PermissionUtil.hasPermission(sender, this.node, informSenderIfNot);
	}
	
	public boolean has(CommandSender sender)
	{
		return has(sender, false);
	}
	
}
