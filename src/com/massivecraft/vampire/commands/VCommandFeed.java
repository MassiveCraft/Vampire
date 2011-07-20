package com.massivecraft.vampire.commands;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;

public class VCommandFeed extends VCommand
{
	public VCommandFeed() {
		aliases.add("feed");

		requiredParameters.add("playername");
		requiredParameters.add("blood");
		
		helpDescription = "feed a vampire";
		
		permission = "vampire.command.feed";
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	// TODO fix this logic...
	
	@Override
	public void perform()
	{
		String playername = parameters.get(0);
		Player player = P.instance.getServer().getPlayer(playername);
		if (player == null) {
			this.sendMessage("Player not found");
			return;
		}
		double bloodQuantity = Double.parseDouble(parameters.get(1));
		
		if(bloodQuantity <= 100D)
		{
			this.sendMessage(player.getDisplayName() + " has been fed.");
			VPlayer vplayer = VPlayer.get(player);
			vplayer.bloodAlter(bloodQuantity);
		}
		else
		{
			bloodQuantity = 100D;
		}
	}
}
