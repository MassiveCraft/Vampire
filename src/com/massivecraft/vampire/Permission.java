package com.massivecraft.vampire;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.vampire.config.Conf;

public enum Permission {
	COMMAND_CURE("cure a vampire", "vampire.command.cure"),
	COMMAND_FEED("feed a vampire", "vampire.command.feed"),
	COMMAND_INFECT("set infection", "vampire.command.infect"),
	COMMAND_LIST("list the vampires on the server", "vampire.command.list"),
	COMMAND_LOAD("load from disk", "vampire.command.load"),
	COMMAND_SAVE("save to disk", "vampire.command.save"),
	COMMAND_TIME_GET("get the current time", "vampire.command.time.get"),
	COMMAND_TIME_SET("set the current time", "vampire.command.time.set"),
	COMMAND_TURN("instantly turn player", "vampire.command.turn"),
	;
	
	public final String description;
	public final String permissionNode;
	
	Permission(final String description, final String permissionNode) {
        this.description = description;
		this.permissionNode = permissionNode;
    }
	
	public boolean has(CommandSender sender) {
		if( P.permissionHandler == null) {
			return sender.hasPermission(permissionNode);
		}
		
		if (sender instanceof Player) {
			return P.permissionHandler.has((Player)sender, permissionNode);
		}
		
		return sender.isOp();
	}
	
	public boolean test(CommandSender sender) {
		if (has(sender)) {
			return true;
		}
		sender.sendMessage(this.getForbiddenMessage());
		return false;
	}
	
	public String getForbiddenMessage() {
		return Conf.colorSystem+"You don't have permission to "+this.description;
	}
	
}
