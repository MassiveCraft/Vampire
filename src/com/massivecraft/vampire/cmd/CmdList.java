package com.massivecraft.vampire.cmd;

import java.util.*;

import org.bukkit.ChatColor;

import com.massivecraft.mcore4.cmd.VisibilityMode;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;
import com.massivecraft.mcore4.util.Txt;
import com.massivecraft.vampire.*;

public class CmdList extends VCommand
{	
	public CmdList()
	{
		this.addAliases("l", "list");
		this.addOptionalArg("page", "1");
		
		this.setVisibilityMode(VisibilityMode.SECRET);
		
		this.addRequirements(ReqHasPerm.get(Permission.LIST.node));
	}
	
	@Override
	public void perform()
	{
		Integer pageHumanBased = this.argAs(0, Integer.class, 1);
		if (pageHumanBased == null) return;
		
		List<String> vampiresOnline = new ArrayList<String>();
		List<String> vampiresOffline = new ArrayList<String>();
		List<String> infectedOnline = new ArrayList<String>();
		List<String> infectedOffline = new ArrayList<String>();
		
		// TODO: Will fail for console!
		for (VPlayer vplayer : VPlayerColls.i.get(sender).getAll())
		{
			if (vplayer.vampire())
			{
				if (vplayer.isOnline())
				{
					vampiresOnline.add(ChatColor.WHITE.toString() + vplayer.getPlayer().getDisplayName());
				}
				else
				{
					vampiresOffline.add(ChatColor.WHITE.toString() + vplayer.getId());
				}
			}
			else if (vplayer.infected())
			{
				if (vplayer.isOnline())
				{
					infectedOnline.add(ChatColor.WHITE.toString() + vplayer.getPlayer().getDisplayName());
				}
				else
				{
					infectedOffline.add(ChatColor.WHITE.toString() + vplayer.getId());
				}
			}
		}

		// Create Messages
		List<String> lines = new ArrayList<String>();
		
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
		this.sendMessage(Txt.getPage(lines, pageHumanBased, "Vampire Player List"));	
	}
}
