package com.bukkit.mcteam.vampire.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.bukkit.mcteam.vampire.*;

public class VCommandTurn extends VCommand {

	public VCommandTurn() {	
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		requiredParameters.add("playername");
		permissions = "vampire.admin.command.turn";
		helpNameAndParams = "turn [playername]";
		helpDescription = "Instantly turn a player into a vampire.";
	}
	
	@Override
	public void perform() {
		String playername = parameters.get(0);
		Player player = Vampire.instance.getServer().getPlayer(playername);
		if (player == null) {
			this.sendMessage("Player not found");
			return;
		}
		this.sendMessage(player.getDisplayName() + " was turned into a vampire.");
		VPlayer vplayer = VPlayer.get(player);
		vplayer.turn();
	}
}
