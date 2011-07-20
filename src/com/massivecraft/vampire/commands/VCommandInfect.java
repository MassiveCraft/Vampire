package com.massivecraft.vampire.commands;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;


public class VCommandInfect extends VCommand {
	
	public VCommandInfect() {
		aliases.add("infect");

		requiredParameters.add("playername");
		optionalParameters.add("amount");
		
		helpDescription = "set infection (0 to 100)";
		
		permission = "vampire.command.infect";
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform() {
		String playername = parameters.get(0);
		double amount = 1.0;
		if (parameters.size() == 2) {
			amount = Double.parseDouble(parameters.get(1));
		}
		Player player = P.instance.getServer().getPlayer(playername);
		if (player == null) {
			this.sendMessage("Player not found");
			return;
		}
		VPlayer vplayer = VPlayer.get(player);
		vplayer.infectionSet(amount);
		this.sendMessage(player.getDisplayName() + " now has infection " + vplayer.infectionGet());
	}
}
