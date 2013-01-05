package com.massivecraft.vampire;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.util.Perm;

public enum VPerm
{
	IS_VAMPIRE("is.vampire"),
	IS_HUMAN("is.human"),
	SHOW_SELF("show.self"),
	SHOW_OTHER("show.other"),
	SHRIEK("shriek"),
	MODE_BLOODLUST("mode.bloodlust"),
	MODE_INTENT("mode.intent"),
	MODE_NIGHTVISION("mode.nightvision"),
	ALTAR_DARK("altar.dark"),
	ALTAR_LIGHT("altar.light"),
	TRADE_OFFER("trade.offer"),
	TRADE_ACCEPT("trade.accept"),
	COMBAT_INFECT("combat.infect"),
	COMBAT_CONTRACT("combat.contract"),
	VERSION("version"),
	LIST("list"),
	SET("set"),
	SET_VAMPIRE_TRUE("set.vampire.true"),
	SET_VAMPIRE_FALSE("set.vampire.false"),
	SET_INFECTION("set.infection"),
	SET_FOOD("set.food"),
	SET_HEALTH("set.health"),
	;
	
	public final String node;
	
	VPerm(final String permissionNode)
	{
		this.node = "vampire."+permissionNode;
    }
	
	public boolean has(CommandSender sender, boolean informSenderIfNot)
	{
		return Perm.has(sender, this.node, informSenderIfNot);
	}
	
	public boolean has(CommandSender sender)
	{
		return has(sender, false);
	}
}
