package com.massivecraft.vampire.altar;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.massivecraft.vampire.Lang;
import com.massivecraft.vampire.Permission;
import com.massivecraft.vampire.VPlayer;

public class AltarGood extends Altar
{
	public AltarGood()
	{
		this.name = Lang.altarGoodName;
		this.desc = Lang.altarGoodDesc;
		
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
		return Permission.ALTAR_GOOD.has(player, true);
	}

	@Override
	public boolean isPaymentRequired(VPlayer vplayer, Player player)
	{
		return vplayer.vampire();
	}

	@Override
	public void effectFree(VPlayer vplayer, Player player)
	{
		if (vplayer.healthy())
		{
			vplayer.msg(Lang.altarGoodFreeHealthy);
		}
		else if (vplayer.infected())
		{
			vplayer.msg(Lang.altarGoodFreeInfected);
			vplayer.infection(0);
		}
	}

	@Override
	public void effectPaid(VPlayer vplayer, Player player)
	{
		player.getWorld().strikeLightningEffect(player.getLocation().add(0, 3, 0));
		vplayer.msg(Lang.altarGoodPaid);
		vplayer.vampire(false);
	}
	
	@Override
	public void effectCommon(VPlayer vplayer, Player player)
	{
		vplayer.msg(Lang.altarGoodCommon);
		vplayer.fxEnderRun();
	}
}
