package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.cmd.arg.AR;
import com.massivecraft.vampire.Vampire;
import com.massivecraft.vampire.entity.MLang;
import com.massivecraft.vampire.entity.UPlayer;
import com.massivecraft.vampire.entity.UPlayerColl;
import com.massivecraft.vampire.entity.UPlayerColls;

public abstract class CmdVampireSetAbstract<T> extends VCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public boolean targetMustBeOnline;
	public AR<T> argReader;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireSetAbstract()
	{
		// Aliases
		this.addRequiredArg("val");
		
		// Args
		this.addOptionalArg("player", "you");
		this.addOptionalArg("univ", "you");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		if ( vme == null && ! this.argIsSet(1))
		{
			msg(MLang.get().consolePlayerArgRequired);
			return;
		}
		
		Multiverse mv = Vampire.get().playerAspect.getMultiverse();
		String universe = this.arg(2, mv.argReaderUniverse(), senderIsConsole ? MassiveCore.DEFAULT : mv.getUniverse(me));
		
		UPlayerColl playerColl = UPlayerColls.get().getForUniverse(universe);
		AR<UPlayer> playerReader = playerColl.getAREntity();
		UPlayer uplayer = this.arg(1, playerReader, vme);
				
		Player player = uplayer.getPlayer();
		
		if (targetMustBeOnline && player == null)
		{
			msg("<h>%s <b>is not online.", uplayer.getDisplayName());
			return;
		}
		
		T val = this.arg(0, argReader);
		
		T res = this.set(uplayer, player, val);
		
		if (res == null) return;
		
		msg("<i>%s <i>now has %s = %s.", uplayer.getDisplayName(), this.getAliases().get(0), res.toString());
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract T set(UPlayer uplayer, Player player, T val);
	
}
