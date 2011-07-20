package com.massivecraft.vampire.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;
import com.massivecraft.vampire.config.Conf;


public class VCommandTurn extends VCommand {

	public VCommandTurn() {	
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		requiredParameters.add("playername");
		optionalParameters.add("isTrueBlood");
		permissions = "vampire.admin.command.turn";
		helpNameAndParams = "turn [playername]";
		helpDescription = "Instantly turn a player into a vampire.";
	}
	
	@Override
	public void perform()
	{
		Player playerSender = (Player)sender;
		if(playerSender.isOp())
		{
			if(!Conf.allowOPToUseAdminCommand) playerSender.sendMessage(Conf.colorSystem + "Config file do not allow OP to use it, you can change this.");
			else
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
						String opt = parameters.get(1).toLowerCase();
						if(opt.compareTo("trueblood") == 0)
						{
							vplayer.setIsTrueBlood(true);
						}
						this.sendMessage(player.getDisplayName() + " was turned into a True Blood vampire.");
					}
					else this.sendMessage(player.getDisplayName() + " was turned into a vampire.");
					
					vplayer.turn();
				}
			}
		}
		else playerSender.sendMessage(Conf.colorSystem + "Only OP is able to use this command.");
	}
}
