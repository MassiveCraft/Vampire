package com.massivecraft.vampire.altar;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.util.InventoryUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.vampire.HolyWaterUtil;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.Vampire;
import com.massivecraft.vampire.entity.MLang;
import com.massivecraft.vampire.entity.UConf;
import com.massivecraft.vampire.entity.UPlayer;
import com.massivecraft.vampire.util.ResourceUtil;

public class AltarLight extends Altar
{
	public AltarLight()
	{
		this.name = MLang.get().altarLightName;
		this.desc = MLang.get().altarLightDesc;
		
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
	public boolean use(final UPlayer uplayer, final Player player)
	{
		UConf uconf = UConf.get(player);
		uplayer.msg("");
		uplayer.msg(this.desc);
		
		if ( ! Perm.ALTAR_LIGHT.has(player, true)) return false;
		
		if ( ! uplayer.isVampire() && playerHoldsWaterBottle(player))
		{
			if ( ! ResourceUtil.playerRemoveAttempt(player, uconf.holyWaterResources, MLang.get().altarLightWaterResourceSuccess, MLang.get().altarLightWaterResourceFail)) return false;
			Bukkit.getScheduler().scheduleSyncDelayedTask(Vampire.get(), new Runnable()
			{
				public void run() {
					ResourceUtil.playerAdd(player, HolyWaterUtil.createHolyWater());
					uplayer.msg(MLang.get().altarLightWaterResult);
					uplayer.runFxEnderBurst();
				}
			}, 1);
			return true;
		}
		
		uplayer.msg(MLang.get().altarLightCommon);
		uplayer.runFxEnder();
		
		if (uplayer.isVampire())
		{
			if ( ! ResourceUtil.playerRemoveAttempt(player, this.resources, MLang.get().altarResourceSuccess, MLang.get().altarResourceFail)) return false;
			Bukkit.getScheduler().scheduleSyncDelayedTask(Vampire.get(), new Runnable()
			{
				public void run() {
					uplayer.msg(MLang.get().altarLightVampire);
					player.getWorld().strikeLightningEffect(player.getLocation().add(0, 3, 0));
					uplayer.runFxEnderBurst();
					uplayer.setVampire(false);
				}
			}, 1);
			return true;
		}
		else if (uplayer.isHealthy())
		{
			uplayer.msg(MLang.get().altarLightHealthy);
		}
		else if (uplayer.isInfected())
		{
			uplayer.msg(MLang.get().altarLightInfected);
			uplayer.setInfection(0);
			uplayer.runFxEnderBurst();
		}
		return false;
	}
	
	protected static boolean playerHoldsWaterBottle(Player player)
	{
		ItemStack item = InventoryUtil.getWeapon(player);
		if (item.getType() != Material.POTION) return false;
		return item.getDurability() == 0;
	}
	
}
