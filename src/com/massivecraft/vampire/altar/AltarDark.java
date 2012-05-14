package com.massivecraft.vampire.altar;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.mcore3.util.MUtil;
import com.massivecraft.vampire.InfectionReason;
import com.massivecraft.vampire.Lang;
import com.massivecraft.vampire.Permission;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.util.FxUtil;
import com.massivecraft.vampire.util.ResourceUtil;

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
		
		this.resources = MUtil.list(
			new ItemStack(Material.MUSHROOM_SOUP, 1),
			new ItemStack(Material.BONE, 10),
			new ItemStack(Material.SULPHUR, 10),
			new ItemStack(Material.REDSTONE, 10)
		);
	}
	
	@Override
	public void use(VPlayer vplayer, Player player)
	{
		vplayer.msg("");
		vplayer.msg(this.desc);
		
		if ( ! Permission.ALTAR_DARK.has(player, true)) return;
		
		vplayer.msg(Lang.altarDarkCommon);
		FxUtil.ensure(PotionEffectType.BLINDNESS, player, 12*20);
		vplayer.fxSmokeRun();
		
		if (vplayer.healthy())
		{
			if ( ! ResourceUtil.playerRemoveAttempt(player, this.resources, Lang.altarResourceSuccess, Lang.altarResourceFail)) return;
			vplayer.msg(Lang.altarDarkHealthy);
			player.getWorld().strikeLightningEffect(player.getLocation().add(0, 3, 0));
			vplayer.fxSmokeBurstRun();
			vplayer.infectionAdd(0.01D, InfectionReason.ALTAR, null);
		}
		else if (vplayer.vampire())
		{
			vplayer.msg(Lang.altarDarkVampire);
		}
		else if (vplayer.infected())
		{
			vplayer.msg(Lang.altarDarkInfected);
		}
	}
}
