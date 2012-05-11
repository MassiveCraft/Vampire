package com.massivecraft.vampire.cmdreq;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore3.cmd.MCommand;
import com.massivecraft.mcore3.cmd.req.IReq;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.VPlayers;

public class ReqIsVampire implements IReq
{
	@Override
	public boolean test(CommandSender sender, MCommand command)
	{
		VPlayer vplayer = VPlayers.i.get(sender);
		if (vplayer == null) return false; // Probably some kind of console user ^^
		return vplayer.vampire();
	}

	@Override
	public String createErrorMessage(CommandSender sender, MCommand command)
	{
		return "<b>Only vampires can "+command.getDesc()+".";
	}
	
	protected static ReqIsVampire instance = new ReqIsVampire();
	public static ReqIsVampire get()
	{
		return instance;
	}
}
