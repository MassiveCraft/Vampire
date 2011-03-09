package com.bukkit.mcteam.vampire.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.bukkit.mcteam.vampire.*;

public class VCommandDefault extends VCommand {
	
	public VCommandDefault() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBeOp = false;
		senderMustBePlayer = false;
	}
	
	@Override
	public void perform() {
		if ( ! (sender instanceof Player)) {
			this.sendMessage("The base command can ony be used by ingame players");
			return;
		}
		
		VPlayer vplayer = VPlayer.get((Player)sender);
		if ( ! vplayer.isVampire()) {
			this.sendMessage(Conf.messageCommandBaseNoVampire);
			return;
		}
		
		vplayer.bloodSendMeterMessage();
		//TODO this should display damage reduction etc... ?
	}
}
