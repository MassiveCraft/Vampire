package com.massivecraft.vampire.cmdreq;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqAbstract;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.vampire.entity.MLang;
import com.massivecraft.vampire.entity.UPlayer;

public class ReqIsVampire extends ReqAbstract
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
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
	public boolean apply(CommandSender sender, MassiveCommand command)
	{
		if (MUtil.isntSender(sender)) return false;
		UPlayer uplayer = UPlayer.get(sender);
		if (uplayer == null) return false;
		return uplayer.isVampire();
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand command)
	{
		return String.format(MLang.get().onlyVampsCanX, (command == null ? "do that" : command.getDesc()));
	}
	
}
