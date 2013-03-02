package com.massivecraft.vampire.cmdreq;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqAbstract;
import com.massivecraft.vampire.Lang;
import com.massivecraft.vampire.VPlayer;

public class ReqIsVampire extends ReqAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ReqIsVampire i = new ReqIsVampire();
	public static ReqIsVampire get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, MCommand command)
	{
		VPlayer vplayer = VPlayer.get(sender);
		if (vplayer == null) return false;
		return vplayer.isVampire();
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MCommand command)
	{
		return String.format(Lang.onlyVampsCanX, (command == null ? "do that" : command.getDesc()));
	}
	
}
