package com.bukkit.mcteam.vampire.listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.player.PlayerListener;

import com.bukkit.mcteam.util.TextUtil;
import com.bukkit.mcteam.vampire.Conf;
import com.bukkit.mcteam.vampire.VPlayer;
import com.bukkit.mcteam.vampire.Vampire;

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
	
	// Used to allow usage of only "v" instead of "/v"
	@Override
	public void onPlayerChat(PlayerChatEvent event) {
		if ( ! Conf.allowNoSlashCommand) {
			return;
		}
		
		if ( ! (event.getMessage().startsWith("v ") || event.getMessage().equals("v"))) {
			return;
		}
		
		List<String> parameters = TextUtil.split(event.getMessage().trim());
		parameters.remove(0);
		CommandSender sender = event.getPlayer();			
		Vampire.instance.handleCommand(sender, parameters);
		event.setCancelled(true);
	}
	
	@Override
	public void onPlayerAnimation(PlayerAnimationEvent event) {
		if (event.getAnimationType() == PlayerAnimationType.ARM_SWING && Conf.dashMaterials.contains(event.getPlayer().getItemInHand().getType())) {
			//VPlayer.get(event.getPlayer()).jump(5);
		}
	}
	
}
