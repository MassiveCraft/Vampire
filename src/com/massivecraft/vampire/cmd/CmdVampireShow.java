package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.Vampire;
import com.massivecraft.vampire.entity.MConf;
import com.massivecraft.vampire.entity.UConf;
import com.massivecraft.vampire.entity.UPlayer;
import com.massivecraft.vampire.entity.UPlayerColl;
import com.massivecraft.vampire.entity.UPlayerColls;
import com.massivecraft.vampire.util.SunUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdVampireShow extends VCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Parameter playerReaderParameter = new Parameter(UPlayerColls.get().getForUniverse(MassiveCore.DEFAULT).getTypeEntity(), true, "player", "you");
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireShow()
	{
		// Parameters
		this.addParameter(playerReaderParameter);
		this.addParameter(Vampire.get().playerAspect.getMultiverse().typeUniverse(), "univ", "you");
		
		// Requirements
		this.addRequirements(new RequirementHasPerm(Perm.SHOW));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().aliasesVampireShow;
	}
	
	@Override
	public void perform() throws MassiveException
	{
		Multiverse mv = Vampire.get().playerAspect.getMultiverse();
		String universe = this.readArgAt(1, senderIsConsole ? MassiveCore.DEFAULT : mv.getUniverse(me));
		
		UPlayerColl playerColl = UPlayerColls.get().getForUniverse(universe);
		Type<UPlayer> playerType = playerColl.getTypeEntity();
		this.playerReaderParameter.setType(playerType);
		
		UPlayer uplayer = this.readArgAt(0, vme);
		
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
			You = uplayer.getDisplayName(sender);
			//you = uplayer.getId();
			are = "is";
		}
		
		message(Txt.titleize(Txt.upperCaseFirst(universe)+" Vampire "+uplayer.getDisplayName(sender)));
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
