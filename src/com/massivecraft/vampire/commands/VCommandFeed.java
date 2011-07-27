package com.massivecraft.vampire.commands;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;

public class VCommandFeed extends VCommand
{
	public VCommandFeed() {
		aliases.add("feed");

		requiredParameters.add("playername");
		optionalParameters.add("blood");
		
		helpDescription = "feed a vampire";
		
		permission = Permission.COMMAND_FEED;
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform()
	{
		String playername = parameters.get(0);
		Player player = P.instance.getServer().getPlayer(playername);
		if (player == null) {
			sendMessage("Player not found");
			return;
		}
		
		VPlayer vplayer = VPlayer.get(player);
		
		if ( ! vplayer.isVampire()) {
			sendMessage(player.getDisplayName() + " is not a vampire.");
			return;
		}
		
		double blood = 100D;
		
		if (parameters.size() == 2) {
			try {
				blood = Double.parseDouble(parameters.get(1)); 
			} catch (Exception e) {
				
			}
		}
		
		String msg = player.getDisplayName() + " was fed from " + String.format("%1$.1f", vplayer.bloodGet());
		vplayer.bloodAlter(blood);
		msg += " to " + String.format("%1$.1f", vplayer.bloodGet()) + " blood.";
		
		sendMessage(msg);
	}
}
