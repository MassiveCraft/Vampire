package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;

public abstract class CmdSetAbstract<T> extends VCommand
{
	boolean targetMustBeOnline;
	Class<T> classOfT;
	
	public CmdSetAbstract()
	{
		this.addRequiredArg("val");
		this.addOptionalArg("player", "you");
	}
	
	@Override
	public void perform()
	{
		if ( vme == null && ! this.argIsSet(1))
		{
			msg(Lang.consolePlayerArgRequired);
			return;
		}
		VPlayer vplayer = this.argAs(1, VPlayer.class, "matchany", vme);
		if (vplayer == null) return;
		Player player = vplayer.getPlayer();
		
		if (targetMustBeOnline && player == null)
		{
			msg("<b>%s is not online.", vplayer.getId());
			return;
		}
		
		T val = this.argAs(0, classOfT);
		if (val == null) return;
		
		T res = this.set(vplayer, player, val);
		
		if (res == null) return;
		
		msg("<i>%s now has %s = %s.", vplayer.getId(), this.aliases.get(0), res.toString());
	}
	
	public abstract T set(VPlayer vplayer, Player player, T val);
	
}
