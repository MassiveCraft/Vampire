package com.massivecraft.vampire;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.massivecraft.mcore2.util.Txt;
import com.massivecraft.vampire.config.Lang;

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

	// TODO: Fix the log info messages!
	
	@Override
	public void applyEffect(Player player)
	{
		VPlayer vplayer = VPlayers.i.get(player);
		//p.log(this.getId() + " was cured from being a vampire by a healing altar.");
		player.sendMessage(Txt.parse(Lang.altarGoodUse));
		vplayer.cureVampirism();
		player.getWorld().strikeLightningEffect(player.getLocation().add(0, 3, 0));
	}
	
	@Override
	public boolean validateUser(Player player)
	{
		VPlayer vplayer = VPlayers.i.get(player);

		// Is Infected
		if (vplayer.isInfected())
		{
			player.sendMessage(Txt.parse(Lang.altarGoodInfected));
			vplayer.setInfection(0);
			player.sendMessage(Txt.parse(Lang.infectionMessageCured));
			return false;
		}
		
		// Is Healthy
		if ( ! vplayer.isVampire())
		{
			player.sendMessage(Txt.parse(Lang.altarGoodHealthy));
			return false;
		}
		
		return true;
	}
}
