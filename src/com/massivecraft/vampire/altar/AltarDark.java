package com.massivecraft.vampire.altar;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.vampire.InfectionReason;
import com.massivecraft.vampire.Lang;
import com.massivecraft.vampire.Permission;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.util.FxUtil;

public class AltarDark extends Altar
{
	public AltarDark()
	{
		this.name = Lang.altarDarkName;
		this.desc = Lang.altarDarkDesc;
		
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
	public boolean worship(VPlayer vplayer, Player player)
	{
		return Permission.ALTAR_DARK.has(player, true);
	}

	@Override
	public boolean isPaymentRequired(VPlayer vplayer, Player player)
	{
		return vplayer.healthy();
	}

	@Override
	public void effectFree(VPlayer vplayer, Player player)
	{
		if (vplayer.vampire())
		{
			vplayer.msg(Lang.altarDarkFreeVampire);
		}
		else if (vplayer.infected())
		{
			vplayer.msg(Lang.altarDarkFreeInfected);
		}
	}

	@Override
	public void effectPaid(VPlayer vplayer, Player player)
	{
		vplayer.msg(Lang.altarDarkPaid);
		player.getWorld().strikeLightningEffect(player.getLocation().add(0, 3, 0));
		vplayer.fxSmokeBurstRun();
		
		vplayer.infectionAdd(0.01D, InfectionReason.ALTAR, null);
	}
	
	@Override
	public void effectCommon(VPlayer vplayer, Player player)
	{
		vplayer.msg(Lang.altarDarkCommon);
		vplayer.fxSmokeRun();
		FxUtil.ensure(PotionEffectType.BLINDNESS, player, 12*20);
	}
}
