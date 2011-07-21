package com.massivecraft.vampire.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.CreatureType;

import com.massivecraft.vampire.Recipe;
import com.massivecraft.vampire.P;
import com.massivecraft.vampire.util.DiscUtil;


public class Conf {
	public static transient File file = new File(P.instance.getDataFolder(), "conf.json");
	
	public static ChatColor colorSystem = ChatColor.RED;
	public static ChatColor colorChrome = ChatColor.DARK_RED;
	public static ChatColor colorCommand = ChatColor.GOLD;
	public static ChatColor colorParameter = ChatColor.GRAY;
	public static ChatColor colorHighlight = ChatColor.WHITE;
	
	public static boolean allowNoSlashCommand = true;
	
	public static int timerInterval = 1000; // Defines the precision of the vampire timer. one second should be good. 
	
	public static double infectionCloseCombatRisk = 1d / 30d;
	public static double infectionCloseCombatAmount = 3D;
	public static double infectionProgressPerSecond = 100D / (1*60*60D) ; // It will take you 1h to turn
	//public static double infectionProgressPerSecond = 100D / (5*60D) ; // It will take you 1min to turn DEBUG SETTING
	
	public static boolean enableVampireNameColorInChat = false;
	public static ChatColor vampireChatNameColor = ChatColor.RED;
	public static ChatColor vampireChatMessageColor = ChatColor.WHITE;
	
	public static double infectionBreadHealAmount = 20D;
	
	public static double thirstUnderBlood = 50D;
	public static double thirstStrongUnderBlood = 20D;
	public static double thirstDamagePerSecond = 0.025; // Once every 40seconds
	public static double thirstStrongDamagePerSecond = 0.1; // Once every 10seconds
	
	public static Set<Material> jumpMaterials = new HashSet<Material>();
	public static double jumpDeltaSpeed = 3;
	public static double jumpBloodCost = 3;
	
	// Time is "hours passed since the beginning of the day" * 1000
	public static int combustFromTime = 0;
	public static int combustToTime = 12400;
	
	public static long regenStartDelayMilliseconds = 8000;
	public static double regenHealthPerSecond = 0.25;
	public static double regenBloodPerHealth = 2.5D;
	
	public static double combatDamageDealtFactor = 1.5;
	public static double combatDamageReceivedFactor = 0.7;
	public static double combatDamageReceivedWoodFactor = 3.5;
	
	public static Set<Material> woodMaterials = new HashSet<Material>();
	
	public static Set<Material> foodMaterials = new HashSet<Material>();
	
	public static int combustFireExtinguishTicks = 5; // After how many minecraft game ticks of no sunlight should the fire stop? 0 means right away.
	
	public static Map<Material,Double> materialOpacity = new HashMap<Material,Double>(); //We assume opacity 1 for all materials not in this map 
	
	public static double playerBloodQuality = 2D;
	public static Map<CreatureType, Double> creatureTypeBloodQuality = new HashMap<CreatureType, Double>();
	public static long truceBreakTime = 60 * 1000L; // One minute
	public static Set<CreatureType> creatureTypeTruceMonsters = new HashSet<CreatureType>();
	
	public static Material altarInfectMaterial = Material.GOLD_BLOCK;
	public static Material altarInfectMaterialSurround = Material.OBSIDIAN;
	public static int altarInfectMaterialSurroundCount = 20;
	public static double altarInfectMaterialSurroundRadious = 7D;
	public static Recipe altarInfectRecipe = new Recipe();
	
	public static Material altarCureMaterial = Material.LAPIS_BLOCK;
	public static Material altarCureMaterialSurround = Material.GLOWSTONE;
	public static int altarCureMaterialSurroundCount = 20;
	public static double altarCureMaterialSurroundRadious = 7D;	
	
	static
	{		
		// TODO Maybe cake could cure vampirism...
		// "You forget about blood this is way better :)"
		
		jumpMaterials.add(Material.RED_ROSE);
		
		foodMaterials.add(Material.APPLE);
		foodMaterials.add(Material.BREAD);
		foodMaterials.add(Material.COOKED_FISH);
		foodMaterials.add(Material.GRILLED_PORK);
		foodMaterials.add(Material.GOLDEN_APPLE);
		foodMaterials.add(Material.MUSHROOM_SOUP);
		foodMaterials.add(Material.RAW_FISH);
		
		woodMaterials.add(Material.STICK);
		woodMaterials.add(Material.WOOD_AXE);
		woodMaterials.add(Material.WOOD_HOE);
		woodMaterials.add(Material.WOOD_PICKAXE);
		woodMaterials.add(Material.WOOD_SPADE);
		woodMaterials.add(Material.WOOD_SWORD);
		
		materialOpacity.put(Material.AIR, 0D);
		materialOpacity.put(Material.SAPLING, 0.3D);
		materialOpacity.put(Material.LEAVES, 0.3D);
		materialOpacity.put(Material.GLASS, 0.5D); // Double glass means UV protection :)
		materialOpacity.put(Material.YELLOW_FLOWER, 0.1D);
		materialOpacity.put(Material.RED_ROSE, 0.1D);
		materialOpacity.put(Material.BROWN_MUSHROOM, 0.1D);
		materialOpacity.put(Material.RED_MUSHROOM, 0.1D);
		materialOpacity.put(Material.TORCH, 0.1D);
		materialOpacity.put(Material.FIRE, 0D);
		materialOpacity.put(Material.MOB_SPAWNER, 0.3D);
		materialOpacity.put(Material.REDSTONE_WIRE, 0D);
		materialOpacity.put(Material.CROPS, 0.2D);
		materialOpacity.put(Material.SIGN, 0.1D);
		materialOpacity.put(Material.SIGN_POST, 0.2D);
		materialOpacity.put(Material.LEVER, 0.1D);
		materialOpacity.put(Material.STONE_PLATE, 0D);
		materialOpacity.put(Material.WOOD_PLATE, 0D);
		materialOpacity.put(Material.REDSTONE_TORCH_OFF, 0.1D);
		materialOpacity.put(Material.REDSTONE_TORCH_ON, 0.1D);
		materialOpacity.put(Material.STONE_BUTTON, 0D);
		materialOpacity.put(Material.SUGAR_CANE_BLOCK, 0.3D);
		materialOpacity.put(Material.FENCE, 0.2D);
		materialOpacity.put(Material.DIODE_BLOCK_OFF, 0D);
		materialOpacity.put(Material.DIODE_BLOCK_ON, 0D);
		
		// For each damage to the creature; how much blood will the vampire obtain
		creatureTypeBloodQuality.put(CreatureType.CHICKEN, playerBloodQuality / 5D);
		creatureTypeBloodQuality.put(CreatureType.COW, playerBloodQuality / 5D);
		creatureTypeBloodQuality.put(CreatureType.PIG, playerBloodQuality / 5D);
		creatureTypeBloodQuality.put(CreatureType.SHEEP, playerBloodQuality / 5D);
		creatureTypeBloodQuality.put(CreatureType.SPIDER, playerBloodQuality / 10D);
		creatureTypeBloodQuality.put(CreatureType.SQUID, playerBloodQuality / 10D);
		
		// These are the creature types that won't target vampires
		creatureTypeTruceMonsters.add(CreatureType.CREEPER);
		creatureTypeTruceMonsters.add(CreatureType.GHAST);
		creatureTypeTruceMonsters.add(CreatureType.SKELETON);
		creatureTypeTruceMonsters.add(CreatureType.SPIDER);
		creatureTypeTruceMonsters.add(CreatureType.ZOMBIE);
		
		altarInfectRecipe.materialQuantities.put(Material.MUSHROOM_SOUP, 1);
		altarInfectRecipe.materialQuantities.put(Material.BONE, 10);
		altarInfectRecipe.materialQuantities.put(Material.SULPHUR, 10);
		altarInfectRecipe.materialQuantities.put(Material.REDSTONE, 10);
	}
	
	// -------------------------------------------- //
	// Persistance
	// -------------------------------------------- //
	
	public static boolean save() {
		P.log("Saving config to disk.");
		try {
			DiscUtil.write(file, P.gson.toJson(new Conf()));
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
			P.gson.fromJson(DiscUtil.read(file), Conf.class);
		} catch (IOException e) {
			e.printStackTrace();
			P.log("Failed to load the config from disk.");
			return false;
		}
		
		return true;
	}
}
