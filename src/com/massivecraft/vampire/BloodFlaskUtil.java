package com.massivecraft.vampire;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.massivecore.util.InventoryUtil;
import com.massivecraft.massivecore.util.MUtil;

public class BloodFlaskUtil
{
	public final static String BLOOD_FLASK_NAME = ChatColor.RED.toString() + "Blood Flask";
	public final static String BLOOD_FLASK_AMOUNT_SUFFIX = ChatColor.RED.toString() + " unit(s) of blood.";
	public final static String BLOOD_FLASK_VAMPIRIC_TRUE = ChatColor.RED.toString() + "The blood is vampiric.";
	public final static String BLOOD_FLASK_VAMPIRIC_FALSE = ChatColor.RED.toString() + "The blood is not vampiric.";
	public final static PotionEffect BLOOD_FLASK_CUSTOM_EFFECT = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 0);
	
	public static ItemStack createBloodFlask(double amount, boolean isVampiric)
	{
		// Create a new item stack of material potion ...
		ItemStack ret = new ItemStack(Material.POTION);
		
		// ... and convert the isVampiric boolean into a string ...
		String metaVampiric = isVampiric ? BLOOD_FLASK_VAMPIRIC_TRUE : BLOOD_FLASK_VAMPIRIC_FALSE;
		
		// ... create the item lore ...
		List<String> lore = MUtil.list(
		Double.toString(amount) + BLOOD_FLASK_AMOUNT_SUFFIX,
		metaVampiric
		);
		
		// ... and set the item meta ...
		PotionMeta meta = (PotionMeta)ret.getItemMeta();
		meta.setDisplayName(BLOOD_FLASK_NAME);
		meta.setLore(lore);
		meta.addCustomEffect(BLOOD_FLASK_CUSTOM_EFFECT, false);
		ret.setItemMeta(meta);
		
		// ... finally, return the result.
		return ret;
	}
	
	public static boolean getBloodFlaskVampiric(ItemStack item)
	{
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		String stringBoolean = lore.get(1);
		boolean ret;
		if (stringBoolean.equals(BLOOD_FLASK_VAMPIRIC_TRUE)) { ret = true; } else { ret = false; }
		return ret;
	}
	
	public static double getBloodFlaskAmount(ItemStack item)
	{
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		String amountLoreString = lore.get(0);
		String amountString[] = amountLoreString.split(" ");
		String amountStr = amountString[0].substring(0, 3);
		double amount = Double.parseDouble(amountStr);
		
		return amount;
	}
	
	public static boolean isBloodFlask(ItemStack item)
	{
		if (item == null) return false;
		if ( ! item.hasItemMeta()) return false;
		ItemMeta meta = item.getItemMeta();
		if ( ! meta.hasDisplayName()) return false;
		String name = meta.getDisplayName();
		return BLOOD_FLASK_NAME.equals(name);
	}
	
	public static boolean playerHoldsGlassBottle(Player player)
	{
		ItemStack item = InventoryUtil.getWeapon(player);
		if (item == null) return false;
		if (item.getType() != Material.GLASS_BOTTLE) return false;
		return item.getDurability() == 0;
	}
	
	public static void playerConsumeGlassBottle(Player player)
	{
		ItemStack item = InventoryUtil.getWeapon(player);
		if (item == null) return;
		if (item.getType() != Material.GLASS_BOTTLE) return;
		int amt = item.getAmount();
		if (amt>1)
		{
			item.setAmount(amt - 1);
		}
		else
		{
			player.getInventory().setItemInMainHand(null);
		}
	}
	
}