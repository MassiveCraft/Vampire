package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore3.util.Txt;
import com.massivecraft.vampire.Conf;
import com.massivecraft.vampire.Lang;
import com.massivecraft.vampire.Permission;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.util.SunUtil;

public class CmdShow extends VCommand
{
	public CmdShow()
	{
		this.addAliases("s", "show");
		this.addOptionalArg("player", "you");
		this.setDesc("Show player info");
	}
	
	@Override
	public void perform()
	{
		if ( vme == null && ! this.argIsSet(0))
		{
			msg(Lang.consolePlayerArgRequired);
			return;
		}
		VPlayer vplayer = this.argAs(0, VPlayer.class, "matchany", vme);
		if (vplayer == null) return;
		
		Player player = vplayer.getPlayer();
		
		boolean self = vplayer == vme;
		
		// Test permissions
		if (self)
		{
			if ( ! Permission.SHOW_SELF.has(sender, true)) return;
		}
		else
		{
			if ( ! Permission.SHOW_OTHER.has(sender, true)) return;
		}
		
		String You = "You";
		//String you = "you";
		String are = "are";
		if ( ! self)
		{
			You = vplayer.getId();
			//you = vplayer.getId();
			are = "is";
		}
		
		msg(Txt.titleize("Vampire info for "+vplayer.getId()));
		if (vplayer.vampire())
		{
			msg("<i>"+You+" "+are+" a vampire.");
			msg(vplayer.reasonDesc(self));
		}
		else if (vplayer.infected())
		{
			msg("<i>"+You+" "+are+" infected at <h>%d%%<i>.", percent(vplayer.infection()));
			msg(vplayer.reasonDesc(self));
			return;
		}
		else
		{
			msg("<i>"+You+" "+are+" a normal human.");
			return;
		}
		
		//msg(vplayer.bloodlust() ? Lang.xIsOn : Lang.xIsOff, "Bloodlust");
		//msg(vplayer.intend() ? Lang.xIsOn : Lang.xIsOff, "Infect intent");
		
		msg("<k>Bloodlust " + (vplayer.bloodlust() ? Lang.on : Lang.off));
		msg("<k>Infect intent " + (vplayer.intend() ? Lang.on : Lang.off));
		msg("<k>Temperature <v>%d%%", (int)Math.round(vplayer.temp()*100));
		if (player == null)
		{
			msg("<k>Irradiation <v>%d%%", percent(vplayer.rad()));
		}
		else
		{
			int rad = percent(vplayer.rad());
			int sun = percent(SunUtil.calcSolarRad(player.getWorld()));
			double terrain = 1d-SunUtil.calcTerrainOpacity(player.getLocation().getBlock());
			double armor = 1d-SunUtil.calcArmorOpacity(player);
			int base = percent(Conf.baseRad);
			msg("<k>Irradiation <v>X% <k>= <yellow>sun <lime>*terrain <blue>*armor <silver>-base");
			msg("<k>Irradiation <v>%+d%% <k>= <yellow>%d <lime>*%.2f <blue>*%.2f <silver>%+d", rad, sun, terrain, armor, base);
			
		}
		
	}
	
	public static int percent(double quota)
	{
		return (int)Math.round(quota*100);
	}
}
