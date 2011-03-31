package org.mcteam.vampire.listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.mcteam.vampire.Conf;
import org.mcteam.vampire.VPlayer;
import org.mcteam.vampire.Vampire;
import org.mcteam.vampire.util.TextUtil;


public class VampirePlayerListener extends PlayerListener {
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		
		if ( ! (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) ) {
			return;
		}
		
		VPlayer vplayer = VPlayer.get(event.getPlayer());
		Material itemMaterial = event.getMaterial();
		
		if (Conf.foodMaterials.contains(itemMaterial) && vplayer.isVampire()) {
			vplayer.sendMessage(Conf.vampiresCantEatFoodMessage);
			event.setCancelled(true);
		}
		
		if (vplayer.isInfected() && itemMaterial == Material.BREAD) {
			vplayer.infectionHeal(Conf.infectionBreadHealAmount);
		}
		
		if (Conf.jumpMaterials.contains(event.getMaterial()) && vplayer.isVampire()) {
			vplayer.jump(5, false);
		}
		
		if ( action != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		Material blockMaterial = event.getClickedBlock().getType();
		
		if (blockMaterial == Conf.altarInfectMaterial) {
			vplayer.useAltarInfect(event.getClickedBlock());
		} else if (blockMaterial == Conf.altarCureMaterial) {
			vplayer.useAltarCure(event.getClickedBlock());
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
		VPlayer vplayer = VPlayer.get(event.getPlayer());
		if ( ! vplayer.isVampire()) {
			return;
		}
			
		if (event.getAnimationType() == PlayerAnimationType.ARM_SWING && Conf.jumpMaterials.contains(event.getPlayer().getItemInHand().getType())) {
			vplayer.jump(5, true);
		}
	}
	
}
