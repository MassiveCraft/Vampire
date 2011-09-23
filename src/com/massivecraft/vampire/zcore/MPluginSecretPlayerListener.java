package com.massivecraft.vampire.zcore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPreLoginEvent;

import com.massivecraft.vampire.zcore.persist.EM;
import com.massivecraft.vampire.zcore.persist.Entity;
import com.massivecraft.vampire.zcore.persist.EntityCollection;
import com.massivecraft.vampire.zcore.persist.PlayerEntityCollection;

public class MPluginSecretPlayerListener extends PlayerListener
{
	private MPlugin p;
	public MPluginSecretPlayerListener(MPlugin p)
	{
		this.p = p;
	}
	
	@Override
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		if (event.isCancelled()) return;

		List<String> args = new ArrayList<String>(Arrays.asList(event.getMessage().split("\\s+")));
		String label = args.remove(0).substring(1);
		
		if (p.handleCommand(label, args, event.getPlayer()))
		{
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onPlayerPreLogin(PlayerPreLoginEvent event)
	{
		for (EntityCollection<? extends Entity> ecoll : EM.class2Entities.values())
		{
			if (ecoll instanceof PlayerEntityCollection)
			{
				ecoll.get(event.getName());
			}
		}
	}
}
