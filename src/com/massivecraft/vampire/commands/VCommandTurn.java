package com.massivecraft.vampire.commands;

import org.bukkit.entity.Player;
import com.massivecraft.vampire.*;

public class VCommandTurn extends VCommand {

	public VCommandTurn() {	
		aliases.add("turn");

		requiredParameters.add("playername");
		optionalParameters.add("isTrueBlood");
		
		helpDescription = "instantly turn player";
		
		permission = "vampire.command.turn";
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
		
		VPlayer vplayer = VPlayer.get(player);
		
		if(vplayer.isVampire())
		{
			this.sendMessage(player.getDisplayName() + " is already a vampire.");
		}
		else
		{
			vplayer.bloodSet(100);
			
			//If there is an optional paramater that is "trueblood", then turn the human into TrueBlood vampire
			if(parameters.size() > 1)
			{
				vplayer.setIsTrueBlood(true);
				this.sendMessage(player.getDisplayName() + " was turned into a True Blood vampire.");
			} else {
				this.sendMessage(player.getDisplayName() + " was turned into a vampire.");
			}
			
			vplayer.turn();
		}
	}
}
