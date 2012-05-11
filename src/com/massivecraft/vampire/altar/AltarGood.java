package com.massivecraft.vampire.altar;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.massivecraft.mcore2.util.Txt;
import com.massivecraft.vampire.Lang;
import com.massivecraft.vampire.Permission;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.VPlayers;

public class AltarGood extends Altar
{
	public AltarGood()
	{
		this.name = "good altar";
		this.desc = "<i>This altar looks bright and nice.";
		
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
	public void applyEffect(Player player)
	{
		VPlayer vplayer = VPlayers.i.get(player);
		
		vplayer.fxEnderRun();
		player.getWorld().strikeLightningEffect(player.getLocation().add(0, 3, 0));
		
		// Is Infected
		if (vplayer.infected())
		{
			player.sendMessage(Txt.parse(Lang.altarGoodInfected));
			vplayer.infection(0);
			player.sendMessage(Txt.parse(Lang.infectionMessageCured));
		}
		
		if (vplayer.vampire())
		{
			player.sendMessage(Txt.parse(Lang.altarGoodUse));
			vplayer.vampire(false);
		}
	}
	
	@Override
	public boolean validateUser(Player player)
	{
		if ( ! Permission.ALTAR_GOOD.has(player, true)) return false;
		
		VPlayer vplayer = VPlayers.i.get(player);
		
		// Is Healthy
		if (vplayer.healthy())
		{
			player.sendMessage(Txt.parse(Lang.altarGoodHealthy));
			return false;
		}
		
		return true;
	}
}
