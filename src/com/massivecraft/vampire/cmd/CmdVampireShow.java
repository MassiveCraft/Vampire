package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.cmd.arg.AR;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.Vampire;
import com.massivecraft.vampire.entity.MLang;
import com.massivecraft.vampire.entity.UConf;
import com.massivecraft.vampire.entity.UPlayer;
import com.massivecraft.vampire.entity.UPlayerColl;
import com.massivecraft.vampire.entity.UPlayerColls;
import com.massivecraft.vampire.util.SunUtil;

public class CmdVampireShow extends VCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireShow()
	{
		// Aliases
		this.addAliases("s", "show");
		
		// Args
		this.addOptionalArg("player", "you");
		this.addOptionalArg("univ", "you");
		
		// Requirements
		this.addRequirements(new ReqHasPerm(Perm.SHOW.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		if ( vme == null && ! this.argIsSet(0))
		{
			msg(MLang.get().consolePlayerArgRequired);
			return;
		}
		
		Multiverse mv = Vampire.get().playerAspect.getMultiverse();
		String universe = this.arg(1, mv.argReaderUniverse(), senderIsConsole ? MassiveCore.DEFAULT : mv.getUniverse(me));
		
		UPlayerColl playerColl = UPlayerColls.get().getForUniverse(universe);
		AR<UPlayer> playerReader = playerColl.getAREntity();
		UPlayer uplayer = this.arg(0, playerReader, vme);
		
		Player player = uplayer.getPlayer();
		UConf uconf = UConf.get(player);
		
		boolean self = uplayer == vme;
		
		// Test permissions
		if (!self && !Perm.SHOW_OTHER.has(sender, true)) return;
		
		String You = "You";
		//String you = "you";
		String are = "are";
		if ( ! self)
		{
			You = uplayer.getDisplayName();
			//you = uplayer.getId();
			are = "is";
		}
		
		msg(Txt.titleize(Txt.upperCaseFirst(universe)+" Vampire "+uplayer.getDisplayName()));
		if (uplayer.isVampire())
		{
			msg("<i>"+You+" <i>"+are+" a vampire.");
			msg(uplayer.getReasonDesc(self));
		}
		else if (uplayer.isInfected())
		{
			msg("<i>"+You+" <i>"+are+" infected at <h>%d%%<i>.", percent(uplayer.getInfection()));
			msg(uplayer.getReasonDesc(self));
			return;
		}
		else
		{
			msg("<i>"+You+" <i>"+are+" neither vampire nor infected with the dark disease.");
			return;
		}
		
		//msg(uplayer.bloodlust() ? MLang.get().xIsOn : MLang.get().xIsOff, "Bloodlust");
		//msg(uplayer.intend() ? MLang.get().xIsOn : MLang.get().xIsOff, "Infect intent");
		
		this.msg(uplayer.bloodlustMsg());
		this.msg(uplayer.intendMsg());
		this.msg(uplayer.usingNightVisionMsg());
		
		msg("<k>Temperature <v>%d%%", (int)Math.round(uplayer.getTemp()*100));
		if (player == null)
		{
			msg("<k>Irradiation <v>%d%%", percent(uplayer.getRad()));
		}
		else
		{
			int rad = percent(uplayer.getRad());
			int sun = percent(SunUtil.calcSolarRad(player.getWorld()));
			double terrain = 1d-SunUtil.calcTerrainOpacity(player.getLocation().getBlock());
			double armor = 1d-SunUtil.calcArmorOpacity(player);
			int base = percent(uconf.baseRad);
			msg("<k>Irradiation <v>X% <k>= <yellow>sun <lime>*terrain <blue>*armor <silver>-base");
			msg("<k>Irradiation <v>%+d%% <k>= <yellow>%d <lime>*%.2f <blue>*%.2f <silver>%+d", rad, sun, terrain, armor, base);
			
		}
		
	}
	
	public static int percent(double quota)
	{
		return (int)Math.round(quota*100);
	}
	
}
