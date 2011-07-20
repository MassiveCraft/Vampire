package com.massivecraft.vampire.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.*;
import com.massivecraft.vampire.config.Conf;


public class VCommandCure extends VCommand {
	public VCommandCure() {
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		requiredParameters.add("playername");
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
				this.sendMessage(player.getDisplayName() + " was cured from vampirism.");
				VPlayer vplayer = VPlayer.get(player);
				vplayer.cure();
			}
		}
		else playerSender.sendMessage(Conf.colorSystem + "Only OP is able to use this command.");
	}
}