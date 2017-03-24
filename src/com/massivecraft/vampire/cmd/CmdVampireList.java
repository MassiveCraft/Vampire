package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.Vampire;
import com.massivecraft.vampire.entity.MConf;
import com.massivecraft.vampire.entity.UPlayer;
import com.massivecraft.vampire.entity.UPlayerColls;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class CmdVampireList extends VCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireList()
	{
		// Parameters
		this.addParameter(Parameter.getPage());
		this.addParameter(Vampire.get().playerAspect.getMultiverse().typeUniverse(), "universe", "you");
		
		// Visibility
		this.setVisibility(Visibility.SECRET);
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.LIST));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().aliasesVampireList;
	}
	
	@Override
	public void perform() throws MassiveException
	{
		int page = this.readArg();
		
		Multiverse mv = Vampire.get().playerAspect.getMultiverse();
		String universe = this.readArg(senderIsConsole ? MassiveCore.DEFAULT : mv.getUniverse(me));
		
		List<String> vampiresOnline = new ArrayList<>();
		List<String> vampiresOffline = new ArrayList<>();
		List<String> infectedOnline = new ArrayList<>();
		List<String> infectedOffline = new ArrayList<>();
		
		for (UPlayer uplayer : UPlayerColls.get().getForUniverse(universe).getAll())
		{
			if (uplayer.isVampire())
			{
				if (uplayer.isOnline())
				{
					vampiresOnline.add(ChatColor.WHITE.toString() + uplayer.getDisplayName(sender));
				}
				else
				{
					vampiresOffline.add(ChatColor.WHITE.toString() + uplayer.getDisplayName(sender));
				}
			}
			else if (uplayer.isInfected())
			{
				if (uplayer.isOnline())
				{
					infectedOnline.add(ChatColor.WHITE.toString() + uplayer.getDisplayName(sender));
				}
				else
				{
					infectedOffline.add(ChatColor.WHITE.toString() + uplayer.getDisplayName(sender));
				}
			}
		}

		// Create Messages
		List<String> lines = new ArrayList<>();
		
		if (vampiresOnline.size() > 0)
		{
			lines.add("<h>=== Vampires Online ===");
			lines.add(Txt.implodeCommaAndDot(vampiresOnline, "<i>"));
		}
		
		if (vampiresOffline.size() > 0)
		{
			lines.add("<h>=== Vampires Offline ===");
			lines.add(Txt.implodeCommaAndDot(vampiresOffline, "<i>"));
		}
		
		if (infectedOnline.size() > 0)
		{
			lines.add("<h>=== Infected Online ===");
			lines.add(Txt.implodeCommaAndDot(infectedOnline, "<i>"));
		}
		
		if (infectedOffline.size() > 0)
		{
			lines.add("<h>=== Infected Offline ===");
			lines.add(Txt.implodeCommaAndDot(infectedOffline, "<i>"));
		}
		
		// Send them
		lines = Txt.parseWrap(lines);
		this.message(Txt.getPage(lines, page, Txt.upperCaseFirst(universe)+" Vampire Players", this));	
	}
	
}
