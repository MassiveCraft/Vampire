package com.massivecraft.vampire.cmd;

import java.util.*;

import org.bukkit.ChatColor;

import com.massivecraft.mcore1.cmd.VisibilityMode;
import com.massivecraft.vampire.*;

public class CmdList extends VCommand
{	
	public CmdList()
	{
		this.addAliases("list", "ls", "l");
		
		this.addOptionalArg("page", "1");
		
		this.setDesc("list vampires on the server");
		
		this.setVisibilityMode(VisibilityMode.SECRET);
		
		this.setDescPermission(Permission.COMMAND_LIST.node);
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
		List<String> exvampiresOnline = new ArrayList<String>();
		List<String> exvampiresOffline = new ArrayList<String>();
		
		for (VPlayer vplayer : VPlayers.i.getAll())
		{
			if (vplayer.isVampire())
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
			else if (vplayer.isInfected())
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
			else if (vplayer.isExvampire())
			{
				if (vplayer.isOnline())
				{
					exvampiresOnline.add(ChatColor.WHITE.toString() + vplayer.getPlayer().getDisplayName());
				}
				else
				{
					exvampiresOffline.add(ChatColor.WHITE.toString() + vplayer.getId());
				}
			}
		}

		// Create Messages
		List<String> lines = new ArrayList<String>();
		
		if (vampiresOnline.size() > 0)
		{
			lines.add("<h>=== Vampires Online ===");
			lines.add(p.txt.implodeCommaAnd(vampiresOnline, "<i>"));
		}
		
		if (vampiresOffline.size() > 0)
		{
			lines.add("<h>=== Vampires Offline ===");
			lines.add(p.txt.implodeCommaAnd(vampiresOffline, "<i>"));
		}
		
		if (infectedOnline.size() > 0)
		{
			lines.add("<h>=== Infected Online ===");
			lines.add(p.txt.implodeCommaAnd(infectedOnline, "<i>"));
		}
		
		if (infectedOffline.size() > 0)
		{
			lines.add("<h>=== Infected Offline ===");
			lines.add(p.txt.implodeCommaAnd(infectedOffline, "<i>"));
		}
		
		if (exvampiresOnline.size() > 0)
		{
			lines.add("<h>=== Exvampires Online ===");
			lines.add(p.txt.implodeCommaAnd(exvampiresOnline, "<i>"));
		}
		
		if (exvampiresOffline.size() > 0)
		{
			lines.add("<h>=== Exvampires Offline ===");
			lines.add(p.txt.implodeCommaAnd(exvampiresOffline, "<i>"));
		}
		
		
		// Send them
		lines = p.txt.parseWrap(lines);
		this.sendMessage(p.txt.getPage(lines, pageHumanBased, "Vampire Player List"));	
	}
}
