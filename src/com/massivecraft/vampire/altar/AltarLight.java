package com.massivecraft.vampire.altar;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.vampire.Conf;
import com.massivecraft.vampire.HolyWaterUtil;
import com.massivecraft.vampire.Lang;
import com.massivecraft.vampire.VPerm;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.util.ResourceUtil;

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
		
		this.resources = MUtil.list(
			new ItemStack(Material.WATER_BUCKET, 1),
			new ItemStack(Material.DIAMOND, 1),
			new ItemStack(Material.SUGAR, 20),
			new ItemStack(Material.WHEAT, 20)
		);
	}
	
	@Override
	public void use(VPlayer vplayer, Player player)
	{
		Conf conf = Conf.get(player);
		vplayer.msg("");
		vplayer.msg(this.desc);
		
		if ( ! VPerm.ALTAR_LIGHT.has(player, true)) return;
		
		if ( ! vplayer.isVampire() && playerHoldsWaterBottle(player))
		{
			if ( ! ResourceUtil.playerRemoveAttempt(player, conf.holyWaterResources, Lang.altarLightWaterResourceSuccess, Lang.altarLightWaterResourceFail)) return;
			ResourceUtil.playerAdd(player, HolyWaterUtil.createItemStack());
			vplayer.msg(Lang.altarLightWaterResult);
			vplayer.runFxEnderBurst();
			return;
		}
		
		vplayer.msg(Lang.altarLightCommon);
		vplayer.runFxEnder();
		
		if (vplayer.isVampire())
		{
			if ( ! ResourceUtil.playerRemoveAttempt(player, this.resources, Lang.altarResourceSuccess, Lang.altarResourceFail)) return;
			vplayer.msg(Lang.altarLightVampire);
			player.getWorld().strikeLightningEffect(player.getLocation().add(0, 3, 0));
			vplayer.runFxEnderBurst();
			vplayer.setVampire(false);
			return;
		}
		else if (vplayer.isHealthy())
		{
			vplayer.msg(Lang.altarLightHealthy);
		}
		else if (vplayer.isInfected())
		{
			vplayer.msg(Lang.altarLightInfected);
			vplayer.setInfection(0);
			vplayer.runFxEnderBurst();
		}
	}
	
	protected static boolean playerHoldsWaterBottle(Player player)
	{
		if (player.getItemInHand().getType() != Material.POTION) return false;
		return player.getItemInHand().getDurability() == 0;
	}
}
