package com.massivecraft.vampire.zcore.persist;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerEntity extends Entity
{
	public Player getPlayer()
	{
		return Bukkit.getPlayer(this.getId());
	}
	
	// -------------------------------------------- //
	// Message Sending Helpers
	// -------------------------------------------- //
	
	public void msg(String msg)
	{
		Player player = this.getPlayer();
		if (player == null) return;
		player.sendMessage(msg);
	}
	
	public void msg(List<String> msgs)
	{
		for(String msg : msgs)
		{
			this.msg(msg);
		}
	}
	
}
