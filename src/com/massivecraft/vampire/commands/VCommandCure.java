package com.massivecraft.vampire.commands;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;


public class VCommandCure extends VCommand {
	public VCommandCure() {
		aliases.add("cure");

		requiredParameters.add("playername");
		
		helpDescription = "cure a vampire";
		
		permission = "vampire.command.cure";
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform()
	{
		String playername = parameters.get(0);
		Player player = P.instance.getServer().getPlayer(playername);
		if (player == null) {
			this.sendMessage("Player not found");
			return;
		}
		this.sendMessage(player.getDisplayName() + " was cured from vampirism.");
		VPlayer vplayer = VPlayer.get(player);
		vplayer.cure();
	}
}