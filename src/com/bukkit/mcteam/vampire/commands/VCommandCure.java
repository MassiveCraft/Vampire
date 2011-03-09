package com.bukkit.mcteam.vampire.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.bukkit.mcteam.vampire.*;

public class VCommandCure extends VCommand {
	public VCommandCure() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBeOp = true;
		senderMustBePlayer = false;
		
		requiredParameters.add("playername");
	}
	
	@Override
	public void perform() {
		String playername = parameters.get(0);
		Player player = Vampire.instance.getServer().getPlayer(playername);
		if (player == null) {
			this.sendMessage("Player not found");
			return;
		}
		this.sendMessage(player.getDisplayName() + " was cured from vampirism.");
		VPlayer vplayer = VPlayer.get(player);
		vplayer.cure();
	}
}
