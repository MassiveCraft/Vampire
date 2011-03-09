package com.bukkit.mcteam.vampire.listeners;

import org.bukkit.Material;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRightClickEvent;

import com.bukkit.mcteam.vampire.Conf;
import com.bukkit.mcteam.vampire.VPlayer;

public class VampireBlockListener extends BlockListener {
	
	@Override
	public void onBlockRightClick(BlockRightClickEvent event) {
		Material material = event.getBlock().getType();
		
		if (material == Conf.altarInfectMaterial) {
			VPlayer.get(event.getPlayer()).useAltarInfect(event.getBlock());
		} else if (material == Conf.altarCureMaterial) {
			VPlayer.get(event.getPlayer()).useAltarCure(event.getBlock());
		}
	}
	
}
