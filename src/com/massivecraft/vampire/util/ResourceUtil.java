package com.massivecraft.vampire.util;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore3.util.Txt;

public class ResourceUtil
{
	public static boolean playerHas(Player player, ItemStack stack)
	{
		int requiredTypeId = stack.getTypeId();
		short requiredDamage = stack.getDurability();
		int requiredAmount = stack.getAmount();
		
		int actualAmount = 0;
		for (ItemStack pstack : player.getInventory().getContents())
		{
			if (pstack == null) continue;
			if (pstack.getTypeId() != requiredTypeId) continue;
			if (pstack.getDurability() != requiredDamage) continue;
			actualAmount += pstack.getAmount();
		}
		
		return actualAmount >= requiredAmount;
	}
	
	public static boolean playerHas(Player player, Collection<? extends ItemStack> stacks)
	{
		for (ItemStack stack : stacks)
		{
			if ( ! playerHas(player, stack)) return false;
		}
		return true;
	}
	
	public static void playerRemove(Player player, Collection<? extends ItemStack> stacks)
	{
		playerRemove(player, stacks.toArray(new ItemStack[0]));
	}
	
	@SuppressWarnings("deprecation")
	public static void playerRemove(Player player, ItemStack... stacks)
	{
		player.getInventory().removeItem(stacks);
		player.updateInventory();
	}
	
	@SuppressWarnings("deprecation")
	public static void playerAdd(Player player, Collection<? extends ItemStack> stacks)
	{
		Inventory inventory = player.getInventory();
		inventory.addItem(stacks.toArray(new ItemStack[0]));
		player.updateInventory();
	}
	
	@SuppressWarnings("deprecation")
	public static void playerAdd(Player player, ItemStack stack)
	{
		Inventory inventory = player.getInventory();
		inventory.addItem(stack);
		player.updateInventory();
	}
	
	public static String describe(Collection<? extends ItemStack> stacks)
	{
		ArrayList<String> lines = new ArrayList<String>();
		for (ItemStack stack : stacks)
		{
			String desc = describe(stack.getType(), stack.getDurability());
			lines.add(Txt.parse("<h>%d <p>%s", stack.getAmount(), desc));
					
		}
		return Txt.implode(lines, Txt.parse("<i>, "));
	}
	
	public static String describe(Material type, short damage)
	{
		if (type == Material.POTION && damage == 0) return "Water Bottle";
		if (type == Material.INK_SACK && damage == 4 ) return "Lapis Lazuli Dye";
		
		return Txt.getMaterialName(type);
	}
	
	public static boolean playerRemoveAttempt(Player player, Collection<? extends ItemStack> stacks, String success, String fail)
	{
		if ( ! playerHas(player, stacks))
		{
			player.sendMessage(Txt.parse(fail));
			player.sendMessage(describe(stacks));
			return false;
		}

		playerRemove(player, stacks);
		
		player.sendMessage(Txt.parse(success));
		player.sendMessage(describe(stacks));
		
		return true;
	}
}
