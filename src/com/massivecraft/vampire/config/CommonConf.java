package com.massivecraft.vampire.config;

import org.bukkit.Material;

import com.massivecraft.vampire.Recipe;
import com.massivecraft.vampire.P;

public class CommonConf
{	
	public static double bloodDecreasePerSecond = 0.01; //You loose all your blood over the elapse of 2 real-life hours
	public static boolean allowSpreadingNecrosis = false;
	public static int radiusSpreadingNecrosis = 2;
	public static boolean burnInSunlight = true;
	public static Recipe altarCureRecipe = new Recipe();
	
	static
	{
		altarCureRecipe.materialQuantities.put(Material.WATER_BUCKET, 1);
		altarCureRecipe.materialQuantities.put(Material.DIAMOND, 1);
		altarCureRecipe.materialQuantities.put(Material.SUGAR, 20);
		altarCureRecipe.materialQuantities.put(Material.WHEAT, 20);
	}
	
	// -------------------------------------------- //
	// Persistance
	// -------------------------------------------- //
	private static transient CommonConf i = new CommonConf();
	public static void load()
	{
		P.p.persist.loadOrSaveDefault(i, CommonConf.class);
	}
	public static void save()
	{
		P.p.persist.save(i);
	}
}
