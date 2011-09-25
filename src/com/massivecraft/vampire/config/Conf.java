package com.massivecraft.vampire.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.CreatureType;

import com.massivecraft.vampire.AltarDefinition;
import com.massivecraft.vampire.Recipe;
import com.massivecraft.vampire.P;


public class Conf
{
	public final static transient int taskInterval = 40; // Defines often the task runs.
	
	public static Boolean nameColorize = false;
	public static ChatColor nameColor = ChatColor.RED;
	
	// TODO REMOVE!! Abstract...
	// Time is "hours passed since the beginning of the day" * 1000
	public final static transient int combustFromTime = 0;
	public final static transient int combustToTime = 12400;
	// After how many minecraft game ticks of no sunlight should the fire stop? 0 means right away.
	public static int combustFireExtinguishTicks = 10; 
	
	public static Double jumpDeltaSpeed = 3d;
	public static Integer jumpFoodCost = 2;
	public static Set<Material> jumpMaterials = new HashSet<Material>();;
	
	public static Double infectionRiskAtCloseCombatWithoutIntent = 1 / 400D;
	public static Double infectionRiskAtCloseCombatWithIntent = 1 / 50D;
	
	public static double infectionProgressPerTick = 100D / (20*60*60D) ; // It will take you 1h to turn
	public static double infectionBreadHealAmount = 20D;
	
	public static AltarDefinition altarInfect = new AltarDefinition();
	public static AltarDefinition altarCure = new AltarDefinition();
	public static Map<Material, Boolean> canEat = new HashMap<Material, Boolean>();
	
	
	// TODO: Rework to have different values for the intent modes!
	public static double combatDamageDealtFactor = 1.5;
	public static double combatDamageReceivedFactor = 0.7;
	public static double combatDamageReceivedWoodFactor = 3.5;
	
	public static Set<Material> woodMaterials = new HashSet<Material>();
	
	public static Map<Material,Double> materialOpacity = new HashMap<Material,Double>(); //We assume opacity 1 for all materials not in this map 
	
	public static double foodPerDamageFromPlayer = 0.4d;
	public static Map<CreatureType, Double> foodPerDamageFromCreature = new HashMap<CreatureType, Double>();
	
	public static long truceBreakTicks = 60 * 20; // One minute
	public static Set<CreatureType> creatureTypeTruceMonsters = new HashSet<CreatureType>();
	
	static
	{
		altarCure.material = Material.LAPIS_BLOCK;
		altarCure.materialSurround = Material.GLOWSTONE;
		altarCure.surroundCount = 20;
		altarCure.surroundRadius = 7D;
		altarCure.recipe = new Recipe();
		altarCure.recipe.materialQuantities.put(Material.WATER_BUCKET, 1);
		altarCure.recipe.materialQuantities.put(Material.DIAMOND, 1);
		altarCure.recipe.materialQuantities.put(Material.SUGAR, 20);
		altarCure.recipe.materialQuantities.put(Material.WHEAT, 20);
		
		altarInfect.material = Material.GOLD_BLOCK;
		altarInfect.materialSurround = Material.OBSIDIAN;
		altarInfect.surroundCount = 20;
		altarInfect.surroundRadius = 7D;
		altarInfect.recipe = new Recipe();
		altarInfect.recipe.materialQuantities.put(Material.MUSHROOM_SOUP, 1);
		altarInfect.recipe.materialQuantities.put(Material.BONE, 10);
		altarInfect.recipe.materialQuantities.put(Material.SULPHUR, 10);
		altarInfect.recipe.materialQuantities.put(Material.REDSTONE, 10);
		
		jumpMaterials.add(Material.ENDER_PEARL);
		
		// http://www.minecraftwiki.net/wiki/Food
		canEat.put(Material.RAW_BEEF, true);
		canEat.put(Material.RAW_CHICKEN, true);
		canEat.put(Material.PORK, true);
		canEat.put(Material.COOKED_BEEF, false);
		canEat.put(Material.BREAD, false);
		canEat.put(Material.CAKE, false);
		canEat.put(Material.COOKIE, false);
		canEat.put(Material.COOKED_CHICKEN, false);
		canEat.put(Material.RAW_FISH, false);
		canEat.put(Material.COOKED_FISH, false);
		canEat.put(Material.GRILLED_PORK, false);
		canEat.put(Material.APPLE, false);
		canEat.put(Material.GOLDEN_APPLE, false);
		canEat.put(Material.MELON, false);
		canEat.put(Material.MUSHROOM_SOUP, false);
		canEat.put(Material.ROTTEN_FLESH, false);
		
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
	}
	
	
	// -------------------------------------------- //
	// Persistance
	// -------------------------------------------- //
	private static transient Conf i = new Conf();
	public static void load()
	{
		P.p.persist.loadOrSaveDefault(i, Conf.class, "conf");
	}
	public static void save()
	{
		P.p.persist.save(i);
	}
}
