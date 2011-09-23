package com.massivecraft.vampire.cmd;

import java.util.*;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.vampire.P;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.VPlayers;
import com.massivecraft.vampire.zcore.MCommand;

public abstract class VCommand extends MCommand<P>
{
	public VPlayer vme;
	public boolean senderMustBeVampire;
	
	
	public VCommand()
	{
		super(P.p);
		senderMustBeVampire = false;
	}
	
	@Override
	public void execute(CommandSender sender, List<String> args, List<MCommand<?>> commandChain)
	{
		if (sender instanceof Player)
		{
			this.vme = VPlayers.i.get((Player)sender);
		}
		super.execute(sender, args, commandChain);
	}
	
	@Override
	public boolean validSenderType(CommandSender sender, boolean informSenderIfNot)
	{
		boolean superValid = super.validSenderType(sender, informSenderIfNot);
		if ( ! superValid) return false;
		
		if (this.senderMustBeVampire && ! VPlayers.i.get((Player)sender).isVampire())
		{
			sender.sendMessage(p.txt.get("command.sender_must_me_vampire"));
			return false;
		}
			
		return true;
	}
	
	// -------------------------------------------- //
	// Argument Readers
	// -------------------------------------------- //
	
	public VPlayer argAsVPlayer(int idx, VPlayer def)
	{
		String name = this.argAsString(idx);
		if (name == null) return def;
		return VPlayers.i.get(name); 
	}
	
	public VPlayer argAsPPlayer(int idx)
	{
		return this.argAsVPlayer(idx, null);
	}
	
	
}
