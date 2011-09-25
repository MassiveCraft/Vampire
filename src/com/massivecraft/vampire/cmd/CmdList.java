package com.massivecraft.vampire.cmd;

import java.util.*;

import com.massivecraft.vampire.*;
import com.massivecraft.vampire.zcore.util.TextUtil;


public class CmdList extends VCommand {
	
	public CmdList()
	{
		aliases.add("list");
		
		helpShort = "List vampires on the server";
		
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
		
		String sep = p.txt.parse("<i>, ");
		
		// Create Messages
		List<String> messages = new ArrayList<String>();
		messages.add(p.txt.titleize("Vampire Player List"));
		messages.add(p.txt.parse("<h>= Vampires Online: <i>") + TextUtil.implode(vampiresOnline, sep));
		messages.add(p.txt.parse("<h>= Vampires Offline: <i>") + TextUtil.implode(vampiresOffline, sep));
		messages.add(p.txt.parse("<h>= Infected Online: <i>") + TextUtil.implode(infectedOnline, sep));
		messages.add(p.txt.parse("<h>= Infected Offline: <i>") + TextUtil.implode(infectedOffline, sep));
		messages.add(p.txt.parse("<h>= Exvampires Online: <i>") + TextUtil.implode(exvampiresOnline, sep));
		messages.add(p.txt.parse("<h>= Exvampires Offline: <i>") + TextUtil.implode(exvampiresOffline, sep));
		
		// Send them
		this.msg(messages);	
	}
}
