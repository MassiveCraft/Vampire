package com.bukkit.mcteam.vampire.listeners;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.player.PlayerListener;

import com.bukkit.mcteam.vampire.Conf;
import com.bukkit.mcteam.vampire.VPlayer;

public class VampirePlayerListener extends PlayerListener {
	
	@Override
	public void onPlayerItem(PlayerItemEvent event) {
		VPlayer vplayer = VPlayer.get(event.getPlayer());
		Material material = event.getMaterial();
		
		if (Conf.foodMaterials.contains(material) && vplayer.isVampire()) {
			vplayer.sendMessage(Conf.vampiresCantEatFoodMessage);
			event.setCancelled(true);
		}
		
		if (vplayer.isInfected() && material == Material.BREAD) {
			vplayer.infectionHeal(Conf.infectionBreadHealAmount);
		}
		
		if (Conf.dashMaterials.contains(event.getMaterial()) && vplayer.isVampire()) {
			vplayer.dash();
		}
	}
	
}
