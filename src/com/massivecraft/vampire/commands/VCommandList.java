package com.massivecraft.vampire.commands;

import java.util.*;

import org.bukkit.ChatColor;

import com.massivecraft.vampire.*;
import com.massivecraft.vampire.util.TextUtil;


public class VCommandList extends VCommand {
	
	public VCommandList() {
		aliases.add("list");
		
		helpDescription = "List vampires on the server";
		
		permission = "vampire.command.list";
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
		
		for (VPlayer vplayer : VPlayer.findAll()) {
			if (vplayer.isVampire()) {
				if (vplayer.isOnline()) {
					vampiresOnline.add(vplayer.getPlayer().getDisplayName());
				} else {
					vampiresOffline.add(vplayer.getPlayerName());
				}
			} else if (vplayer.isInfected()) {
				if (vplayer.isOnline()) {
					infectedOnline.add(vplayer.getPlayer().getDisplayName());
				} else {
					infectedOffline.add(vplayer.getPlayerName());
				}
			} else if (vplayer.isExvampire()) {
				if (vplayer.isOnline()) {
					exvampiresOnline.add(vplayer.getPlayer().getDisplayName());
				} else {
					exvampiresOffline.add(vplayer.getPlayerName());
				}
			}
		}
		
		// Create Messages
		List<String> messages = new ArrayList<String>();
		messages.add(" ");
		messages.add("= Vampires Online: "+ChatColor.WHITE + TextUtil.implode(vampiresOnline, ", "));
		messages.add("= Vampires Offline: "+ChatColor.WHITE + TextUtil.implode(vampiresOffline, ", "));
		messages.add("= Infected Online: "+ChatColor.WHITE + TextUtil.implode(infectedOnline, ", "));
		messages.add("= Infected Offline: "+ChatColor.WHITE + TextUtil.implode(infectedOffline, ", "));
		messages.add("= Exvampires Online: "+ChatColor.WHITE + TextUtil.implode(exvampiresOnline, ", "));
		messages.add("= Exvampires Offline: "+ChatColor.WHITE + TextUtil.implode(exvampiresOffline, ", "));
		
		// Send them
		this.sendMessage(messages);	
	}
}
