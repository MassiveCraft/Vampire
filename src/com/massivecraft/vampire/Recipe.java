package com.massivecraft.vampire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.vampire.util.SortUtil;
import com.massivecraft.vampire.util.TextUtil;


public class Recipe {
	public Map<Material, Integer> materialQuantities;
	
	// GSON needs this noarg constructor;
	public Recipe() {
		materialQuantities = new HashMap<Material, Integer>();
	}
	
	@SuppressWarnings("deprecation")
	public void removeFromPlayer(Player player) {
		Inventory inventory = player.getInventory();
		for (Material material: this.materialQuantities.keySet()) {
			inventory.removeItem(new ItemStack(material.getId(), this.materialQuantities.get(material)));
		}
		player.updateInventory(); // It is ok to use this method though it is deprecated.
	}
	
	public boolean playerHasEnough(Player player) {
		Inventory inventory = player.getInventory();
		for (Material material: this.materialQuantities.keySet()) {
			if (getMaterialCountFromInventory(material, inventory) < this.materialQuantities.get(material)) {
				return false;
			}
		}
		return true;
	}
	
	public static int getMaterialCountFromInventory(Material material, Inventory inventory) {
		int count = 0;
		for(ItemStack stack : inventory.all(material).values()) {
			count += stack.getAmount();
		}
		return count;
	}
	
	public String getRecipeLine() {
		ArrayList<String> lines = new ArrayList<String>();
		for (Entry<Material, Integer> item : SortUtil.entriesSortedByValues(this.materialQuantities)) {
			lines.add(""+item.getValue()+" "+TextUtil.getMaterialName(item.getKey()));
		}
		return TextUtil.implode(lines, ", ");
	}
}
