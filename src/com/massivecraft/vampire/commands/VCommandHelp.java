package com.massivecraft.vampire.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;
import com.massivecraft.vampire.config.Lang;

public class VCommandHelp extends VCommand
{
	public VCommandHelp() {
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform()
	{
		VPlayer vplayer = VPlayer.get((Player)sender);
		vplayer.sendMessage(Lang.helpMessages);
	}
}
