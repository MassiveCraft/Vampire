package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.cmd.arg.ARSenderEntity;
import com.massivecraft.mcore.cmd.arg.ArgReader;
import com.massivecraft.mcore.usys.Multiverse;
import com.massivecraft.vampire.*;

public abstract class CmdSetAbstract<T> extends VCommand
{
	boolean targetMustBeOnline;
	ArgReader<T> argReader;
	
	public CmdSetAbstract()
	{
		this.addRequiredArg("val");
		this.addOptionalArg("player", "you");
		this.addOptionalArg("univ", "you");
	}
	
	@Override
	public void perform()
	{
		if ( vme == null && ! this.argIsSet(1))
		{
			msg(Lang.consolePlayerArgRequired);
			return;
		}
		
		Multiverse mv = P.p.playerAspect.getMultiverse();
		String universe = this.arg(2, mv.argReaderUniverse(), senderIsConsole ? MCore.DEFAULT : mv.getUniverse(me));
		if (universe == null) return;
		
		VPlayerColl playerColl = VPlayerColls.i.getForUniverse(universe);
		ArgReader<VPlayer> playerReader = ARSenderEntity.getStartAny(playerColl);
		VPlayer vplayer = this.arg(1, playerReader, vme);
		if (vplayer == null) return;
		
		Player player = vplayer.getPlayer();
		
		if (targetMustBeOnline && player == null)
		{
			msg("<h>%s <b>is not online.", vplayer.getDisplayName());
			return;
		}
		
		T val = this.arg(0, argReader);
		if (val == null) return;
		
		T res = this.set(vplayer, player, val);
		
		if (res == null) return;
		
		msg("<i>%s <i>now has %s = %s.", vplayer.getDisplayName(), this.aliases.get(0), res.toString());
	}
	
	public abstract T set(VPlayer vplayer, Player player, T val);
	
}
