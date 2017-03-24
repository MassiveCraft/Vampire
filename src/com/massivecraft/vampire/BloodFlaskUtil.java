package com.massivecraft.vampire;

import com.massivecraft.massivecore.util.InventoryUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.vampire.entity.UPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class BloodFlaskUtil
{
	private final static String COLOR_RED = ChatColor.RED.toString();
	public final static String BLOOD_FLASK_NAME = ChatColor.GREEN.toString() + "Blood Flask";
	public final static String BLOOD_FLASK_AMOUNT_SUFFIX = COLOR_RED + " units of blood.";
	public final static String BLOOD_FLASK_VAMPIRIC_TRUE = COLOR_RED + "The blood is vampiric.";
	public final static String BLOOD_FLASK_VAMPIRIC_FALSE = COLOR_RED + "The blood is not vampiric.";
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
		PotionMeta meta = InventoryUtil.createMeta(ret);
		meta.setDisplayName(BLOOD_FLASK_NAME);
		meta.setLore(lore);
		meta.addCustomEffect(BLOOD_FLASK_CUSTOM_EFFECT, false);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
		ret.setItemMeta(meta);
		
		// ... finally, return the result.
		return ret;
	}
	
	public static boolean isBloodFlaskVampiric(ItemStack item)
	{
		List<String> lore = InventoryUtil.getLore(item);
		if (lore == null) return false;
		String stringBoolean = lore.get(1);
		return BLOOD_FLASK_VAMPIRIC_TRUE.equals(stringBoolean);
	}
	
	public static double getBloodFlaskAmount(ItemStack item)
	{
		if (item == null) return 0D;
		List<String> lore = InventoryUtil.getLore(item);
		if (lore == null || lore.isEmpty()) return 0D;
		
		String amountLoreString = lore.get(0);
		String amountString[] = amountLoreString.split(" ");
		String amountStr = amountString[0].substring(0, 3);
		double amount = Double.parseDouble(amountStr);
		
		return amount;
	}
	
	public static boolean isBloodFlask(ItemStack item)
	{
		return InventoryUtil.isDisplayName(item, BLOOD_FLASK_NAME);
	}
	
	public static void playerConsumeGlassBottle(Player player)
	{
		ItemStack item = InventoryUtil.getWeapon(player);
		if (item == null) return;
		if (item.getType() != Material.GLASS_BOTTLE) return;
		
		int amount = item.getAmount();
		if (amount > 1)
		{
			item.setAmount(amount - 1);
		}
		else
		{
			item = null;
		}
		player.getInventory().setItemInMainHand(item);
	}
	
	public static void fillBottle(double amount, UPlayer uplayer)
	{
		Player player = uplayer.getPlayer();
		BloodFlaskUtil.playerConsumeGlassBottle(player);
		PlayerInventory playerInventory = player.getInventory();
		playerInventory.addItem(BloodFlaskUtil.createBloodFlask(amount, uplayer.isVampire()));
	}
	
}
