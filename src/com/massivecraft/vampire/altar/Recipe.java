package com.massivecraft.vampire.altar;

import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Recipe
{
	public Map<Material, Integer> materialQuantities;
	
	// GSON needs this noarg constructor;
	public Recipe()
	{
		this(new HashMap<Material, Integer>());
	}
	
	public Recipe(Map<Material, Integer> materialQuantities)
	{
		this.materialQuantities = materialQuantities;
	}
	
	@SuppressWarnings("deprecation")
	public void removeFromPlayer(Player player)
	{
		Inventory inventory = player.getInventory();
		for (Material material: this.materialQuantities.keySet()) {
			inventory.removeItem(new ItemStack(material, this.materialQuantities.get(material)));
		}
		player.updateInventory(); // It is ok to use this method though it is deprecated.
	}
	
	public boolean playerHasEnough(Player player)
	{
		Inventory inventory = player.getInventory();
		for (Material material: this.materialQuantities.keySet()) {
			if (getMaterialCountFromInventory(material, inventory) < this.materialQuantities.get(material))
			{
				return false;
			}
		}
		return true;
	}
	
	public static int getMaterialCountFromInventory(Material material, Inventory inventory)
	{
		int count = 0;
		for(ItemStack stack : inventory.all(material).values())
		{
			count += stack.getAmount();
		}
		return count;
	}
	
	public String getRecipeLine()
	{
		ArrayList<String> lines = new ArrayList<>();
		for (Entry<Material, Integer> item : MUtil.entriesSortedByValues(this.materialQuantities))
		{
			Material material = item.getKey();
			int count = item.getValue();
			lines.add(Txt.parse("<h>%d <p>%s", count, Txt.getMaterialName(material)));
					
		}
		return Txt.implode(lines, Txt.parse("<i>, "));
	}
	
}
