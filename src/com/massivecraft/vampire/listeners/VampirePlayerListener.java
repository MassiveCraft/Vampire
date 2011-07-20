package com.massivecraft.vampire.listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.P;
import com.massivecraft.vampire.config.Conf;
import com.massivecraft.vampire.config.Lang;
import com.massivecraft.vampire.util.TextUtil;


public class VampirePlayerListener extends PlayerListener {
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		
		if ( ! (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) ) {
			return;
		}
		
		VPlayer vplayer = VPlayer.get(event.getPlayer());
		Material itemMaterial = event.getMaterial();
		
		if(vplayer.isVampire())
		{
			if (Conf.foodMaterials.contains(itemMaterial))
			{
				vplayer.sendMessage(Lang.vampiresCantEatFoodMessage);
				event.setCancelled(true);
			}
			else if(itemMaterial == Material.PORK) //Vampire can eat fresh pork blood
			{
				vplayer.bloodDrink(5 * Conf.creatureTypeBloodQuality.get(CreatureType.PIG), "the "+CreatureType.PIG.getName().toLowerCase());
				event.setCancelled(true);
				event.getPlayer().setItemInHand(null);
			}
			
			if (Conf.jumpMaterials.contains(event.getMaterial())) 
			{
				vplayer.jump(Conf.jumpDeltaSpeed, false);
			}
		}
		
		if (vplayer.isInfected() && itemMaterial == Material.BREAD) {
			vplayer.infectionHeal(Conf.infectionBreadHealAmount);
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
	public void onPlayerChat(PlayerChatEvent event)
	{		
		if ( ! Conf.allowNoSlashCommand) {
			return;
		}
		
		if ( ! (event.getMessage().startsWith("v ") || event.getMessage().equals("v")))
		{
			//Handle the chat message
			//Color the player name if he is a vampire
			if(VPlayer.get(event.getPlayer()).isVampire() && Conf.enableVampireNameColorInChat)
			{ 
				event.getPlayer().getServer().broadcastMessage(Conf.vampireChatNameColor + "<" + event.getPlayer().getName() + ">" + Conf.vampireChatMessageColor + event.getMessage());
				event.setCancelled(true);
			}
		}
		else
		{
			List<String> parameters = TextUtil.split(event.getMessage().trim());
			parameters.remove(0);
			CommandSender sender = event.getPlayer();
			P.instance.handleCommand(sender, parameters);
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onPlayerAnimation(PlayerAnimationEvent event) {
		VPlayer vplayer = VPlayer.get(event.getPlayer());
		if ( ! vplayer.isVampire()) {
			return;
		}
			
		if (event.getAnimationType() == PlayerAnimationType.ARM_SWING && Conf.jumpMaterials.contains(event.getPlayer().getItemInHand().getType())) {
			vplayer.jump(Conf.jumpDeltaSpeed, true);
		}
	}
	
	
}
