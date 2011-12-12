package com.massivecraft.vampire.cmd.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore1.cmd.MCommand;
import com.massivecraft.mcore1.cmd.req.IReq;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.VPlayers;

public class ReqIsVampire implements IReq
{
	@Override
	public boolean test(CommandSender sender, MCommand command)
	{
		VPlayer vplayer = VPlayers.i.get(sender);
		return vplayer.isVampire();
	}

	@Override
	public String createErrorMessage(CommandSender sender, MCommand command)
	{
		return "<b>Only vampires can use this command.";
	}
	
	protected static ReqIsVampire instance = new ReqIsVampire();
	public static ReqIsVampire getInstance()
	{
		return instance;
	}
}
