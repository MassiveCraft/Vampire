package com.massivecraft.vampire.altar;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.massivecraft.mcore3.util.Txt;
import com.massivecraft.vampire.InfectionReason;
import com.massivecraft.vampire.Lang;
import com.massivecraft.vampire.Permission;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.VPlayers;

public class AltarEvil extends Altar
{
	public AltarEvil()
	{
		this.name = Lang.altarEvilName;
		this.desc = Lang.altarEvilDesc;
		
		this.coreMaterial = Material.GOLD_BLOCK;
		
		this.materialCounts = new HashMap<Material, Integer>();
		this.materialCounts.put(Material.OBSIDIAN, 30);
		this.materialCounts.put(Material.WEB, 5);
		this.materialCounts.put(Material.DEAD_BUSH, 5);
		this.materialCounts.put(Material.DIAMOND_BLOCK, 2);
		
		this.recipe = new Recipe();
		this.recipe.materialQuantities.put(Material.MUSHROOM_SOUP, 1);
		this.recipe.materialQuantities.put(Material.BONE, 10);
		this.recipe.materialQuantities.put(Material.SULPHUR, 10);
		this.recipe.materialQuantities.put(Material.REDSTONE, 10);
	}

	@Override
	public void applyEffect(Player player)
	{
		player.sendMessage(Txt.parse(Lang.altarEvilUse));
		VPlayer vplayer = VPlayers.i.get(player);
		vplayer.infectionAdd(0.01D, InfectionReason.ALTAR, null);
		
		vplayer.fxSmokeRun();
		player.getWorld().strikeLightningEffect(player.getLocation().add(0, 3, 0));
	}
	
	@Override
	public boolean validateUser(Player player)
	{
		if ( ! Permission.ALTAR_EVIL.has(player, true)) return false;
		
		VPlayer vplayer = VPlayers.i.get(player);

		// Is Infected
		if (vplayer.infected())
		{
			player.sendMessage(Txt.parse(Lang.altarEvilAlreadyInfected));
			return false;
		}
		
		// Is Vampire
		if (vplayer.vampire())
		{
			player.sendMessage(Txt.parse(Lang.altarEvilAlreadyVampire));
			return false;
		}
		
		return true;
	}
}
