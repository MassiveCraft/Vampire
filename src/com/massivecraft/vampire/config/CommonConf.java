package com.massivecraft.vampire.config;

import java.io.File;
import java.io.IOException;
import org.bukkit.Material;

import com.massivecraft.vampire.Recipe;
import com.massivecraft.vampire.P;
import com.massivecraft.vampire.util.DiscUtil;

public class CommonConf
{
	public static transient File file = new File(P.instance.getDataFolder(), "Common.json");
	
	public static double bloodDecreasePerSecond = 0.01; //You loose all your blood over the elapse of 2 real-life hours
	public static boolean allowSpreadingNecrosis = true;
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
	
	public static boolean save() {
		P.log("Saving config to disk.");
		try {
			DiscUtil.write(file, P.instance.gson.toJson(new CommonConf()));
		} catch (IOException e) {
			e.printStackTrace();
			P.log("Failed to save the config to disk.");
			return false;
		}
		return true;
	}
	
	public static boolean load() {
		if ( ! file.exists()) {
			P.log("No conf to load from disk. Creating new file.");
			save();
			return true;
		}
		
		try {
			P.instance.gson.fromJson(DiscUtil.read(file), CommonConf.class);
		} catch (IOException e) {
			e.printStackTrace();
			P.log("Failed to load the config from disk.");
			return false;
		}
		
		return true;
	}
}
