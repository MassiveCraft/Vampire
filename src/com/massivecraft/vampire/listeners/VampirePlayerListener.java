package com.massivecraft.vampire.listeners;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import com.massivecraft.vampire.P;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.VPlayers;
import com.massivecraft.vampire.config.GeneralConf;
import com.massivecraft.vampire.config.Lang;
import com.massivecraft.vampire.config.VampireTypeConf;
import com.massivecraft.vampire.zcore.util.TextUtil;


public class VampirePlayerListener extends PlayerListener
{
	public static P p = P.p; 
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		
		if ( ! (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) )
		{
			return;
		}
		
		VPlayer vplayer = VPlayers.i.get(event.getPlayer());
		VampireTypeConf vconf = vplayer.getConf();
		Material itemMaterial = event.getMaterial();
		
		if(vplayer.isVampire())
		{
			if (vconf.canEat.containsKey(itemMaterial))
			{
				if ( ! vconf.canEat.get(itemMaterial))
				{
					vplayer.msg(p.txt.parse(Lang.vampiresCantEatThat, TextUtil.getMaterialName(itemMaterial)));
					event.setCancelled(true);
				}
			}
				
			if (vplayer.getConf().jumpMaterials.contains(event.getMaterial())) 
			{
				vplayer.jump(vplayer.getConf().jumpDeltaSpeed, false);
			}
		}
		
		if (vplayer.isInfected() && itemMaterial == Material.BREAD)
		{
			vplayer.infectionHeal(GeneralConf.infectionBreadHealAmount);
		}		
		
		if ( action != Action.RIGHT_CLICK_BLOCK)
		{
			return;
		}
		
		Material blockMaterial = event.getClickedBlock().getType();
		
		if (blockMaterial == GeneralConf.altarInfectMaterial)
		{
			vplayer.useAltarInfect(event.getClickedBlock());
		} 
		else if (blockMaterial == vplayer.getConf().altarCureMaterial)
		{
			vplayer.useAltarCure(event.getClickedBlock());
		}
	}
	
	@Override
	public void onPlayerChat(PlayerChatEvent event)
	{		
		if (event.isCancelled()) return;
		
		Player me = event.getPlayer();
		VPlayer vme = VPlayers.i.get(me);
		
		if (vme.getConf().nameColorize == false) return;
		
		me.setDisplayName(""+vme.getConf().nameColor+ChatColor.stripColor(me.getDisplayName()));
		
		
		/*
		if (event.getMessage().startsWith("v ") || event.getMessage().equals("v"))
		{
			List<String> parameters = TextUtil.split(event.getMessage().trim());
			parameters.remove(0);
			CommandSender sender = event.getPlayer();
			P.p.handleCommand(sender, parameters);
			event.setCancelled(true);
		}*/
	}
	
	@Override
	public void onPlayerAnimation(PlayerAnimationEvent event)
	{
		VPlayer vplayer = VPlayers.i.get(event.getPlayer());
		if ( ! vplayer.isVampire()) return;
			
		if (event.getAnimationType() == PlayerAnimationType.ARM_SWING && vplayer.getConf().jumpMaterials.contains(event.getPlayer().getItemInHand().getType()))
		{
			vplayer.jump(vplayer.getConf().jumpDeltaSpeed, true);
		}
	}
	
	
}
