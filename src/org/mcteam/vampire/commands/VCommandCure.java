package org.mcteam.vampire.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.mcteam.vampire.*;


public class VCommandCure extends VCommand {
	public VCommandCure() {
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		requiredParameters.add("playername");
		permissions = "vampire.admin.command.cure";
		helpNameAndParams = "cure [playername]";
		helpDescription = "Instantly cure from vampirism";
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