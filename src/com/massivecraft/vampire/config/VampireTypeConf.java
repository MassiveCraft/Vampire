package com.massivecraft.vampire.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.massivecraft.vampire.Recipe;

public class VampireTypeConf
{	
	public boolean burnInSunlight = true;
	
	public Material altarCureMaterial = Material.LAPIS_BLOCK;
	public Material altarCureMaterialSurround = Material.GLOWSTONE;
	public int altarCureMaterialSurroundCount = 20;
	public double altarCureMaterialSurroundRadious = 7D;
	public Recipe altarCureRecipe = new Recipe();
	
	public Map<Material, Boolean> canEat = new HashMap<Material, Boolean>();
	
	public boolean nameColorize = false;
	public ChatColor nameColor = ChatColor.RED;
	
	
	public int thirstUnderFood = 10; // 20 is max
	public int thirstStrongUnderFood = 5;
	
	public double thirstDamagePerSecond = 0.025; // Once every 40seconds
	public double thirstStrongDamagePerSecond = 0.1; // Once every 10seconds
	
	public Set<Material> jumpMaterials = new HashSet<Material>();
	public double jumpDeltaSpeed = 3;
	public int jumpFoodCost = 1;
	
	
	public VampireTypeConf()
	{
		this.altarCureRecipe.materialQuantities.put(Material.WATER_BUCKET, 1);
		this.altarCureRecipe.materialQuantities.put(Material.DIAMOND, 1);
		this.altarCureRecipe.materialQuantities.put(Material.SUGAR, 20);
		this.altarCureRecipe.materialQuantities.put(Material.WHEAT, 20);
		
		// http://www.minecraftwiki.net/wiki/Food
		this.canEat.put(Material.RAW_BEEF, true);
		this.canEat.put(Material.RAW_CHICKEN, true);
		this.canEat.put(Material.PORK, true);
		this.canEat.put(Material.COOKED_BEEF, false);
		this.canEat.put(Material.BREAD, false);
		this.canEat.put(Material.CAKE, false);
		this.canEat.put(Material.COOKIE, false);
		this.canEat.put(Material.COOKED_CHICKEN, false);
		this.canEat.put(Material.RAW_FISH, false);
		this.canEat.put(Material.COOKED_FISH, false);
		this.canEat.put(Material.GRILLED_PORK, false);
		this.canEat.put(Material.APPLE, false);
		this.canEat.put(Material.GOLDEN_APPLE, false);
		this.canEat.put(Material.MELON, false);
		this.canEat.put(Material.MUSHROOM_SOUP, false);
		this.canEat.put(Material.ROTTEN_FLESH, false);
		
		jumpMaterials.add(Material.ENDER_PEARL);
	}
}
