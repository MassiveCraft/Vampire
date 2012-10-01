package com.massivecraft.vampire.cmdreq;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore4.cmd.MCommand;
import com.massivecraft.mcore4.cmd.req.IReq;
import com.massivecraft.vampire.Lang;
import com.massivecraft.vampire.VPlayer;

public class ReqIsVampire implements IReq
{
	@Override
	public boolean test(CommandSender sender, MCommand command)
	{
		VPlayer vplayer = VPlayer.get(sender);
		if (vplayer == null) return false; // Probably some kind of console user ^^
		return vplayer.isVampire();
	}

	@Override
	public String createErrorMessage(CommandSender sender, MCommand command)
	{
		return String.format(Lang.onlyVampsCanX, command.getDesc());
	}
	
	protected static ReqIsVampire instance = new ReqIsVampire();
	public static ReqIsVampire get()
	{
		return instance;
	}
}
