package com.massivecraft.vampire;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.mcore5.util.MUtil;
import com.massivecraft.mcore5.util.ThrownPotionUtil;

public class HolyWaterUtil
{
	public final static List<Integer> POTION_VALUES = MUtil.list(16415, 16431);
	public final static String NAME = "Holy Water";
	public final static List<String> LORE = MUtil.list("Purges darkness and evil");
	public final static PotionEffect EFFECT = new PotionEffect(PotionEffectType.REGENERATION, 20, 0);
	
	public static ItemStack createItemStack()
	{
		ItemStack ret = new ItemStack(Material.POTION, 1, POTION_VALUES.get(0).shortValue());
		
		PotionMeta meta = (PotionMeta)ret.getItemMeta();
		meta.setDisplayName(NAME);
		meta.setLore(LORE);
		meta.addCustomEffect(EFFECT, false);
		ret.setItemMeta(meta);
		
		return ret;
	}
	
	public static boolean isHolyWater(ThrownPotion thrownPotion)
	{
		return isHolyWater(ThrownPotionUtil.getPotionValue(thrownPotion));
	}
	
	public static boolean isHolyWater(ItemStack itemStack)
	{
		return isHolyWater(itemStack.getDurability());
	}
	
	private static boolean isHolyWater(int durability)
	{
		return POTION_VALUES.contains(durability);
	}
	
}
