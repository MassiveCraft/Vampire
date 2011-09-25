package com.massivecraft.vampire.listeners;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.vampire.P;
import com.massivecraft.vampire.config.Conf;

public class VampireBlockListener extends BlockListener
{
	public P p;
	
	public VampireBlockListener(P p)
	{
		this.p = p;
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event)
	{
		if (event.isCancelled()) return;
		Material material = event.getBlock().getType();
		if ( ! Conf.dropSelfOverrideMaterials.contains(material)) return;
		
		event.setCancelled(true);
		event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(material, 1));
		event.getBlock().setType(Material.AIR);
	}
}
