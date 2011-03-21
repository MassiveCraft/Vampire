package com.bukkit.mcteam.vampire.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.bukkit.mcteam.vampire.*;

public class VCommandBlood extends VCommand {
	
	public VCommandBlood() {
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = true;
		senderMustBeVampire = true;
		permissions = "vampire.default.command.blood";
		helpNameAndParams = "";
		helpDescription = "See current blood supply (must be vampire)";
	}
	
	@Override
	public void perform() {
		VPlayer vplayer = VPlayer.get((Player)sender);
		vplayer.bloodSendMeterMessage();
	}
}