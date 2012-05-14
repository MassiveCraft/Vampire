package com.massivecraft.vampire.altar;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.vampire.Conf;
import com.massivecraft.vampire.Lang;
import com.massivecraft.vampire.P;
import com.massivecraft.vampire.Permission;
import com.massivecraft.vampire.VPlayer;

public class AltarLight extends Altar
{
	public AltarLight()
	{
		this.name = Lang.altarLightName;
		this.desc = Lang.altarLightDesc;
		
		this.coreMaterial = Material.LAPIS_BLOCK;
		
		this.materialCounts = new HashMap<Material, Integer>();
		this.materialCounts.put(Material.GLOWSTONE, 30);
		this.materialCounts.put(Material.YELLOW_FLOWER, 5);
		this.materialCounts.put(Material.RED_ROSE, 5);
		this.materialCounts.put(Material.DIAMOND_BLOCK, 2);
		
		this.recipe = new Recipe();
		this.recipe.materialQuantities.put(Material.WATER_BUCKET, 1);
		this.recipe.materialQuantities.put(Material.DIAMOND, 1);
		this.recipe.materialQuantities.put(Material.SUGAR, 20);
		this.recipe.materialQuantities.put(Material.WHEAT, 20);
	}
	
	@Override
	public boolean worship(VPlayer vplayer, Player player)
	{
		return Permission.ALTAR_LIGHT.has(player, true);
	}

	@Override
	public boolean isPaymentRequired(VPlayer vplayer, Player player)
	{
		return vplayer.vampire();
	}

	@Override
	public void effectFree(VPlayer vplayer, final Player player)
	{
		if (playerHoldsWaterBottle(player))
		{
			final int amount = player.getItemInHand().getAmount();
			vplayer.msg(Lang.altarLightWater);
			Bukkit.getScheduler().scheduleSyncDelayedTask(P.p, new Runnable()
			{
				@Override
				public void run()
				{
					player.setItemInHand(new ItemStack(Material.POTION, amount, Conf.holyWaterPotionValue));
				}
			});
		}
		else if (vplayer.healthy())
		{
			vplayer.msg(Lang.altarLightFreeHealthy);
		}
		else if (vplayer.infected())
		{
			vplayer.msg(Lang.altarLightFreeInfected);
			vplayer.infection(0);
			vplayer.fxEnderBurstRun();
		}
	}

	@Override
	public void effectPaid(VPlayer vplayer, Player player)
	{
		vplayer.msg(Lang.altarLightPaid);
		player.getWorld().strikeLightningEffect(player.getLocation().add(0, 3, 0));
		vplayer.fxEnderBurstRun();
		
		vplayer.vampire(false);
	}
	
	@Override
	public void effectCommon(VPlayer vplayer, Player player)
	{
		vplayer.msg(Lang.altarLightCommon);
		vplayer.fxEnderRun();
	}
	
	protected static boolean playerHoldsWaterBottle(Player player)
	{
		if (player.getItemInHand().getType() != Material.POTION) return false;
		return player.getItemInHand().getDurability() == 0;
	}
}
