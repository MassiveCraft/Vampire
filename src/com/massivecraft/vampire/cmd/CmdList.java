package com.massivecraft.vampire.cmd;

import java.util.*;

import com.massivecraft.vampire.*;
import com.massivecraft.vampire.zcore.CommandVisibility;
import com.massivecraft.vampire.zcore.util.TextUtil;


public class CmdList extends VCommand
{	
	public CmdList()
	{
		aliases.add("list");
		aliases.add("ls");
		aliases.add("l");
		
		this.setHelpShort("list vampires on the server");
		
		this.visibility = CommandVisibility.SECRET;
		
		permission = Permission.COMMAND_LIST.node;
		senderMustBePlayer = false;
		senderMustBeVampire = false;
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
		
		for (VPlayer vplayer : VPlayers.i.get())
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
		
		String sep = p.txt.parse("<i>, <white>");
		
		// Create Messages
		List<String> messages = new ArrayList<String>();
		messages.add(p.txt.titleize("Vampire Player List"));
		messages.add(p.txt.parse("<h>= Vampires Online: <white>") + TextUtil.implode(vampiresOnline, sep));
		messages.add(p.txt.parse("<h>= Vampires Offline: <white>") + TextUtil.implode(vampiresOffline, sep));
		messages.add(p.txt.parse("<h>= Infected Online: <white>") + TextUtil.implode(infectedOnline, sep));
		messages.add(p.txt.parse("<h>= Infected Offline: <white>") + TextUtil.implode(infectedOffline, sep));
		messages.add(p.txt.parse("<h>= Exvampires Online: <white>") + TextUtil.implode(exvampiresOnline, sep));
		messages.add(p.txt.parse("<h>= Exvampires Offline: <white>") + TextUtil.implode(exvampiresOffline, sep));
		
		// Send them
		this.sendMessage(messages);	
	}
}
