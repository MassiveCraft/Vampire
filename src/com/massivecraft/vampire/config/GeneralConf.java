package com.massivecraft.vampire.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.CreatureType;

import com.massivecraft.vampire.Recipe;
import com.massivecraft.vampire.P;


public class GeneralConf
{
	// public static boolean allowNoSlashCommand = true;
	
	public final static transient int taskInterval = 40; // Defines often the task runs. 
	
	public static double infectionCloseCombatRisk = 1d / 30d;
	public static double infectionCloseCombatAmount = 3D;
	public static double infectionProgressPerSecond = 100D / (1*60*60D) ; // It will take you 1h to turn
	//public static double infectionProgressPerSecond = 100D / (5*60D) ; // It will take you 1min to turn DEBUG SETTING
	
	public static double infectionBreadHealAmount = 20D;
	
	// Time is "hours passed since the beginning of the day" * 1000
	public static int combustFromTime = 0;
	public static int combustToTime = 12400;
	
	public static double combatDamageDealtFactor = 1.5;
	public static double combatDamageReceivedFactor = 0.7;
	public static double combatDamageReceivedWoodFactor = 3.5;
	
	public static Set<Material> woodMaterials = new HashSet<Material>();
	
	public static int combustFireExtinguishTicks = 5; // After how many minecraft game ticks of no sunlight should the fire stop? 0 means right away.
	
	public static Map<Material,Double> materialOpacity = new HashMap<Material,Double>(); //We assume opacity 1 for all materials not in this map 
	
	public static double foodPerDamageFromPlayer = 0.4d;
	public static Map<CreatureType, Double> foodPerDamageFromCreature = new HashMap<CreatureType, Double>();
	
	public static long truceBreakTime = 60 * 1000L; // One minute
	public static Set<CreatureType> creatureTypeTruceMonsters = new HashSet<CreatureType>();
	
	public static Material altarInfectMaterial = Material.GOLD_BLOCK;
	public static Material altarInfectMaterialSurround = Material.OBSIDIAN;
	public static int altarInfectMaterialSurroundCount = 20;
	public static double altarInfectMaterialSurroundRadious = 7D;
	public static Recipe altarInfectRecipe = new Recipe();
	
	static
	{		
		// TODO Maybe cake could cure vampirism...
		// "You forget about blood this is way better :)"
		
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
		foodPerDamageFromCreature.put(CreatureType.CHICKEN, foodPerDamageFromPlayer / 5D);
		foodPerDamageFromCreature.put(CreatureType.COW, foodPerDamageFromPlayer / 5D);
		foodPerDamageFromCreature.put(CreatureType.PIG, foodPerDamageFromPlayer / 5D);
		foodPerDamageFromCreature.put(CreatureType.SHEEP, foodPerDamageFromPlayer / 5D);
		foodPerDamageFromCreature.put(CreatureType.SPIDER, foodPerDamageFromPlayer / 10D);
		foodPerDamageFromCreature.put(CreatureType.CAVE_SPIDER, foodPerDamageFromPlayer / 10D);
		foodPerDamageFromCreature.put(CreatureType.SQUID, foodPerDamageFromPlayer / 10D);
		
		// These are the creature types that won't target vampires
		creatureTypeTruceMonsters.add(CreatureType.CREEPER);
		creatureTypeTruceMonsters.add(CreatureType.GHAST);
		creatureTypeTruceMonsters.add(CreatureType.SKELETON);
		creatureTypeTruceMonsters.add(CreatureType.SPIDER);
		creatureTypeTruceMonsters.add(CreatureType.CAVE_SPIDER);
		creatureTypeTruceMonsters.add(CreatureType.ZOMBIE);
		creatureTypeTruceMonsters.add(CreatureType.ENDERMAN);
		creatureTypeTruceMonsters.add(CreatureType.GIANT);
		
		altarInfectRecipe.materialQuantities.put(Material.MUSHROOM_SOUP, 1);
		altarInfectRecipe.materialQuantities.put(Material.BONE, 10);
		altarInfectRecipe.materialQuantities.put(Material.SULPHUR, 10);
		altarInfectRecipe.materialQuantities.put(Material.REDSTONE, 10);
	}
	
	
	// -------------------------------------------- //
	// Persistance
	// -------------------------------------------- //
	private static transient GeneralConf i = new GeneralConf();
	public static void load()
	{
		P.p.persist.loadOrSaveDefault(i, GeneralConf.class, "conf_general");
	}
	public static void save()
	{
		P.p.persist.save(i);
	}
}
