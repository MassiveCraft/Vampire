package com.massivecraft.vampire.cmd;

import java.util.*;

import com.massivecraft.mcore1.cmd.VisibilityMode;
import com.massivecraft.vampire.*;

public class CmdList extends VCommand
{	
	public CmdList()
	{
		this.addAliases("list", "ls", "l");
		
		this.setDesc("list vampires on the server");
		
		this.setVisibilityMode(VisibilityMode.SECRET);
		
		this.setDescPermission(Permission.COMMAND_LIST.node);
	}
	
	@Override
	public void perform()
	{
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
					vampiresOnline.add(vplayer.getPlayer().getDisplayName());
				}
				else
				{
					vampiresOffline.add(vplayer.getId());
				}
			}
			else if (vplayer.isInfected())
			{
				if (vplayer.isOnline())
				{
					infectedOnline.add(vplayer.getPlayer().getDisplayName());
				}
				else
				{
					infectedOffline.add(vplayer.getId());
				}
			}
			else if (vplayer.isExvampire())
			{
				if (vplayer.isOnline())
				{
					exvampiresOnline.add(vplayer.getPlayer().getDisplayName());
				}
				else
				{
					exvampiresOffline.add(vplayer.getId());
				}
			}
		}
		
		String sep = p().txt.parse("<i>, <white>");
		
		// Create Messages
		List<String> messages = new ArrayList<String>();
		messages.add(p().txt.titleize("Vampire Player List"));
		messages.add(p().txt.parse("<h>= Vampires Online: <white>") + p().txt.implode(vampiresOnline, sep));
		messages.add(p().txt.parse("<h>= Vampires Offline: <white>") + p().txt.implode(vampiresOffline, sep));
		messages.add(p().txt.parse("<h>= Infected Online: <white>") + p().txt.implode(infectedOnline, sep));
		messages.add(p().txt.parse("<h>= Infected Offline: <white>") + p().txt.implode(infectedOffline, sep));
		messages.add(p().txt.parse("<h>= Exvampires Online: <white>") + p().txt.implode(exvampiresOnline, sep));
		messages.add(p().txt.parse("<h>= Exvampires Offline: <white>") + p().txt.implode(exvampiresOffline, sep));
		
		// Send them
		this.sendMessage(messages);	
	}
}
