package com.massivecraft.vampire.entity;

import com.massivecraft.massivecore.collections.BackstringSet;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.vampire.PotionEffectConf;
import com.massivecraft.vampire.altar.AltarDark;
import com.massivecraft.vampire.altar.AltarLight;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UConf extends Entity<UConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static UConf get(Object oid)
	{
		return UConfColls.get().get2(oid);
	}
	
	// -------------------------------------------- //
	// FX
	// -------------------------------------------- //
	
	public double fxSmokePerMilli = 8D / 1000D; // 8 smokes per second
	public double fxEnderPerMilli = 2D / 1000D; // 2 enders per second
	public int fxEnderRandomMaxLen = 1;
	public double fxSmokeBurstCount = 30D;
	public double fxFlameBurstCount = 5D;
	public double fxEnderBurstCount = 3D;
	
	// -------------------------------------------- //
	// SHRIEK
	// -------------------------------------------- //
	
	public long shriekWaitMessageCooldownMillis = 500;
	public long shriekCooldownMillis = 30 * 1000;
	
	// -------------------------------------------- //
	// MISC
	// -------------------------------------------- //
	
	public Set<DamageCause> blockDamageFrom = MUtil.set(
		DamageCause.DROWNING,
		DamageCause.FALL,
		DamageCause.STARVATION
	);
	
	public Set<RegainReason> blockHealthFrom = MUtil.set(
		RegainReason.SATIATED,
		RegainReason.REGEN	
	);
	
	// -------------------------------------------- //
	// UPDATE
	// -------------------------------------------- //
	
	public Map<String,Boolean> updatePermsVampire = MUtil.map(
		"vampire.is.vampire", true,
		"vampire.is.human", false
	);
	
	public Map<String,Boolean> updatePermsHuman = MUtil.map(
		"vampire.is.vampire", false,
		"vampire.is.human", true
	);

	public int updateRespawnFood = 20;
	public int updateRespawnHealth = 20;
	
	public Boolean updateNameColor = false;
	public ChatColor updateNameColorTo = ChatColor.RED;
	
	// -------------------------------------------- //
	// DROP SELF
	// -------------------------------------------- //
	
	public Set<Material> dropSelfMaterials = MUtil.set(
		Material.COBWEB,
		Material.GLOWSTONE,
		Material.BOOKSHELF,
		Material.DEAD_BUSH
	);
	
	// -------------------------------------------- //
	// BLOODLUST
	// -------------------------------------------- //
	
	public double bloodlustMinFood = 2.5D;
	public double bloodlustFoodPerMilli = -20D / (60D * 1000D); // You can bloodlust for 60 seconds
	public double bloodlustSmokes = 1.5D;
	
	// -------------------------------------------- //
	// NIGHTVISION
	// -------------------------------------------- //
	
	public boolean nightvisionCanBeUsed = true;
	
	// -------------------------------------------- //
	// UNDEAD HORSES
	// -------------------------------------------- //
	
	public boolean canInfectHorses = true;
 
	// -------------------------------------------- //
	// POTION EFFECTS
	// -------------------------------------------- //
	
	public PotionEffectConf effectConfBloodlust = new PotionEffectConf(EventPriority.HIGHEST, true, 0x1f1f23, MUtil.map(
		8, 1,
		1, 1
	));

	public PotionEffectConf effectConfNightvision = new PotionEffectConf(EventPriority.HIGH, true, 0, MUtil.map(
		16, 1
	));

	public PotionEffectConf effectConfVampire = new PotionEffectConf(EventPriority.NORMAL, true, 0, MUtil.map(
		8, 0,
		1, 1
	));
	
	public PotionEffectConf effectConfInfected = new PotionEffectConf(EventPriority.NORMAL, true, 0x587653, new HashMap<Integer, Integer>());
	
	public PotionEffectConf effectConfHuman = new PotionEffectConf(EventPriority.NORMAL, false, 0, new HashMap<Integer, Integer>());

	
	// -------------------------------------------- //
	// REGEN
	// -------------------------------------------- //
	
	public double regenMinFood = 2.5D;
	public int regenDelayMillis = 10*1000;
	public double regenFoodPerMilli = 0.5D / 1000D; // Regen 0.5 food per second
	public double regenHealthPerFood = 2D;
	
	// -------------------------------------------- //
	// TRUCE
	// -------------------------------------------- //
	
	// One minute
	public long truceBreakMillis = 60L * 1000L; 
	
	// These are the creature types that won't target vampires
	public BackstringSet<EntityType> truceEntityTypes = new BackstringSet<>(EntityType.class,
		"BLAZE",
		"CAVE_SPIDER",
		"CREEPER",
		"ENDERMAN",
		"GHAST",
		"GIANT",
		"MAGMA_CUBE",
		"PIG_ZOMBIE",
		"SKELETON",
		"SPIDER",
		"ZOMBIE",
		"WITCH",
		"GUARDIAN",
		"SILVERFISH",
		"ENDERMITE"
	);
	
	// -------------------------------------------- //
	// COMBAT
	// -------------------------------------------- //
	
	public double combatDamageFactorWithoutBloodlust = 1.0;
	public double combatDamageFactorWithBloodlust = 1.15;
	
	private final static transient int damageDiamondSword = 7;
	public int combatWoodDamage = 3*damageDiamondSword;
	
	public Set<Material> combatWoodMaterials = MUtil.set(
		Material.WOODEN_AXE,
		Material.WOODEN_HOE,
		Material.WOODEN_PICKAXE,
		Material.WOODEN_SHOVEL,
		Material.WOODEN_SWORD,
		Material.STICK,
		Material.TORCH,
		Material.REDSTONE_TORCH,
		Material.SPRUCE_SIGN,
		Material.ACACIA_SIGN,
		Material.BIRCH_SIGN,
		Material.DARK_OAK_SIGN,
		Material.JUNGLE_SIGN,
		Material.OAK_SIGN,
		Material.ACACIA_FENCE,
		Material.ACACIA_FENCE_GATE,
		Material.BIRCH_FENCE,
		Material.BIRCH_FENCE_GATE,
		Material.DARK_OAK_FENCE,
		Material.DARK_OAK_FENCE_GATE,
		Material.JUNGLE_FENCE,
		Material.JUNGLE_FENCE_GATE,
		Material.OAK_FENCE,
		Material.OAK_FENCE_GATE,
		Material.SPRUCE_FENCE,
		Material.SPRUCE_FENCE_GATE
	);
	
	// -------------------------------------------- //
	// INFECTION
	// -------------------------------------------- //
	
	// It will take you 0.25h to turn
	public double infectionPerMilli = 0.25D / (1000D * 60D * 60D);
	
	public int infectionProgressNauseaTicks = 12*20;
	public int infectionProgressDamage = 1;
	
	// We have disabled this feature per default.
	// public Double infectionRiskAtCloseCombatWithoutIntent = 0.003;
	// public Double infectionRiskAtCloseCombatWithIntent = 0.05;
	public Double infectionRiskAtCloseCombatWithoutIntent = 0D;
	public Double infectionRiskAtCloseCombatWithIntent = 0D;
	
	// -------------------------------------------- //
	// TRADE
	// -------------------------------------------- //
	
	public double tradeOfferMaxDistance = 2d;
	public long tradeOfferToleranceMillis = 20 * 1000;
	public double tradeVisualDistance = 7D;
	public double tradePercentage = 1d;

	// -------------------------------------------- //
	// FOOD
	// -------------------------------------------- //
	
	public boolean foodCakeAllowed = true;
	
	public Map<EntityType, Double> entityTypeFullFoodQuotient = MUtil.map(
		EntityType.ENDER_DRAGON, 140/20D,
		EntityType.MUSHROOM_COW, 20/20D,
		EntityType.GIANT, 50/20D,
		EntityType.CREEPER, 10/20D,
		EntityType.SKELETON, 10/20D,
		EntityType.SPIDER, 10/20D,
		EntityType.ZOMBIE, 10/20D,
		EntityType.SLIME, 10/20D,
		EntityType.GHAST, 10/20D,
		EntityType.PIG_ZOMBIE, 10/20D,
		EntityType.ENDERMAN, 10/20D,
		EntityType.CAVE_SPIDER, 10/20D,
		EntityType.SILVERFISH, 10/20D,
		EntityType.BLAZE, 10/20D,
		EntityType.MAGMA_CUBE, 10/20D,
		EntityType.PIG, 10/20D,
		EntityType.SHEEP, 10/20D,
		EntityType.COW, 10/20D,
		EntityType.HORSE, 10/20D,
		EntityType.CHICKEN, 10/20D,
		EntityType.SQUID, 10/20D,
		EntityType.OCELOT, 10/20D,
		EntityType.IRON_GOLEM, 10/20D,
		EntityType.VILLAGER, 10/20D,
		EntityType.PLAYER, 10/20D,
		EntityType.WOLF, 0/20D,
		EntityType.SNOWMAN, 0/20D
	);

	// -------------------------------------------- //
	// HOLY WATER
	// -------------------------------------------- //
	
	public double holyWaterSplashRadius = 6D;
	public double holyWaterTemp = 0.7D;
	
	public List<ItemStack> holyWaterResources = MUtil.list(
		new ItemStack(Material.POTION, 1, (short)0),
		new ItemStack(Material.LAPIS_LAZULI, 1, (short)0)
	);
	
	// -------------------------------------------- //
	// SUN
	// -------------------------------------------- //
	
	public double opacityPerArmorPiece = 0.125d;
	public double baseRad = -0.2d;
	public double tempPerRadAndMilli = 1d / (10d * 1000d); // it should take 10 seconds to reach max temp in maximum sunlight.
	
	public double sunNauseaTemp = 0.20d;
	public double sunWeaknessTemp = 0.30d;
	public double sunSlowTemp = 0.50d;
	public double sunBlindnessTemp = 0.80d;
	public double sunBurnTemp = 0.90d;
	
	public int sunNauseaTicks = 10*20;
	public int sunWeaknessTicks = 10*20;
	public int sunSlowTicks = 10*20;
	public int sunBlindnessTicks = 10*20;
	public int sunBurnTicks = 3*20;	
	
	public double sunSmokesPerTempAndMilli = 12D / 1000D; // 12 smokes per second in full sunlight
	public double sunFlamesPerTempAndMilli = 0.4D / 1000D; // 0.4 flames every second in full sunlight
	
	//We assume opacity 1 for all materials not in this map
	private final static transient Double AIR = 0D;
	private final static transient Double GROUND = 0D;
	private final static transient Double STAIRS = 1D;
	private final static transient Double SLABS = 1D;
	private final static transient Double DOOR = 0D;
	private final static transient Double THIN = 0D;
	private final static transient Double STICK = 0.1D;
	private final static transient Double WATER = 0.2D;
	private final static transient Double VEGETATION = 0.2D;
	private final static transient Double FENCE = 0.4D;
	private final static transient Double GLASS = 0.5D;
	
	public Map<Material,Double> typeOpacity = MUtil.map(
		Material.AIR, AIR, //AIR
		Material.CAVE_AIR, AIR, //AIR
		Material.VOID_AIR, AIR, //AIR
		//1, XD, //STONE
		//2, XD, //GRASS
		//3, XD, //DIRT
		//4, XD, //COBBLESTONE
		//5, XD, //WOOD
		Material.ACACIA_SAPLING, VEGETATION, //SAPLING
		Material.BIRCH_SAPLING, VEGETATION, //SAPLING
		Material.DARK_OAK_SAPLING, VEGETATION, //SAPLING
		Material.JUNGLE_SAPLING, VEGETATION, //SAPLING
		Material.OAK_SAPLING, VEGETATION, //SAPLING
		Material.SPRUCE_SAPLING, VEGETATION, //SAPLING
		//7, XD, //BEDROCK
		Material.WATER, WATER, //WATER
		//9, WATER, //STATIONARY_WATER
		//10, XD, //LAVA
		//11, XD, //STATIONARY_LAVA
		//12, XD, //SAND
		//13, XD, //GRAVEL
		//14, XD, //GOLD_ORE
		//15, XD, //IRON_ORE
		//16, XD, //COAL_ORE
		//17, XD, //LOG
		Material.ACACIA_LEAVES, VEGETATION, //LEAVES
		Material.BIRCH_LEAVES, VEGETATION, //LEAVES
		Material.DARK_OAK_LEAVES, VEGETATION, //LEAVES
		Material.JUNGLE_LEAVES, VEGETATION, //LEAVES
		Material.OAK_LEAVES, VEGETATION, //LEAVES
		Material.SPRUCE_LEAVES, VEGETATION, //LEAVES
		//19, XD, //SPONGE
		Material.GLASS, GLASS, //GLASS
		Material.BLACK_STAINED_GLASS, THIN, //GLASS
		Material.BLUE_STAINED_GLASS, THIN, //GLASS
		Material.BROWN_STAINED_GLASS, THIN, //GLASS
		Material.CYAN_STAINED_GLASS, THIN, //GLASS
		Material.GRAY_STAINED_GLASS, THIN, //GLASS
		Material.GREEN_STAINED_GLASS, THIN, //GLASS
		Material.LIGHT_BLUE_STAINED_GLASS, THIN, //GLASS
		Material.LIGHT_GRAY_STAINED_GLASS, THIN, //GLASS
		Material.LIME_STAINED_GLASS, THIN, //GLASS
		Material.MAGENTA_STAINED_GLASS, THIN, //GLASS
		Material.ORANGE_STAINED_GLASS, THIN, //GLASS
		Material.PINK_STAINED_GLASS, THIN, //GLASS
		Material.PURPLE_STAINED_GLASS, THIN, //GLASS
		Material.RED_STAINED_GLASS, THIN, //GLASS
		Material.WHITE_STAINED_GLASS, THIN, //GLASS
		//21, XD, //LAPIS_ORE
		//22, XD, //LAPIS_BLOCK
		//23, XD, //DISPENSER
		//24, XD, //SANDSTONE
		//25, XD, //NOTE_BLOCK
		//26, XD, //BED_BLOCK
		Material.POWERED_RAIL, GROUND, //POWERED_RAIL
		Material.DETECTOR_RAIL, GROUND, //DETECTOR_RAIL
		//29, XD, //PISTON_STICKY_BASE
		Material.COBWEB, THIN, //WEB
		Material.TALL_GRASS, VEGETATION, //LONG_GRASS
		Material.DEAD_BUSH, VEGETATION, //DEAD_BUSH
		//33, XD, //PISTON_BASE
		Material.PISTON_HEAD, STICK, //PISTON_EXTENSION
		//35, XD, //WOOL
		//36, XD, //PISTON_MOVING_PIECE
		Material.DANDELION, VEGETATION, //FLOWER
		Material.POPPY, VEGETATION, //FLOWER
		Material.BLUE_ORCHID, VEGETATION, //FLOWER
		Material.ALLIUM, VEGETATION, //FLOWER
		Material.AZURE_BLUET, VEGETATION, //FLOWER
		Material.ORANGE_TULIP, VEGETATION, //FLOWER
		Material.PINK_TULIP, VEGETATION, //FLOWER
		Material.RED_TULIP, VEGETATION, //FLOWER
		Material.WHITE_TULIP, VEGETATION, //FLOWER
		Material.OXEYE_DAISY, VEGETATION, //FLOWER
		Material.SUNFLOWER, VEGETATION, //FLOWER
		Material.LILAC, VEGETATION, //FLOWER
		Material.ROSE_BUSH, VEGETATION, //FLOWER
		Material.PEONY, VEGETATION, //FLOWER
		Material.BROWN_MUSHROOM, VEGETATION, //BROWN_MUSHROOM
		Material.RED_MUSHROOM, VEGETATION, //RED_MUSHROOM
		//41, XD, //GOLD_BLOCK
		//42, XD, //IRON_BLOCK
		//43, XD, //DOUBLE_STEP
		Material.SANDSTONE_SLAB, SLABS, //STEP
		Material.RED_SANDSTONE_SLAB, SLABS, //STEP
		Material.STONE_BRICK_SLAB, SLABS, //STEP
		Material.STONE_SLAB, SLABS, //STEP
		Material.BRICK_SLAB, SLABS, //STEP
		Material.COBBLESTONE_SLAB, SLABS, //STEP
		Material.DARK_PRISMARINE_SLAB, SLABS, //STEP
		Material.PRISMARINE_SLAB, SLABS, //STEP
		Material.PRISMARINE_BRICK_SLAB, SLABS, //STEP
		Material.NETHER_BRICK_SLAB, SLABS, //STEP
		Material.QUARTZ_SLAB, SLABS, //STEP
		Material.PURPUR_SLAB, SLABS, //STEP
		Material.PETRIFIED_OAK_SLAB, SLABS, //STEP
		Material.ACACIA_SLAB, SLABS, //STEP
		Material.BIRCH_SLAB, SLABS, //STEP
		Material.DARK_OAK_SLAB, SLABS, //STEP
		Material.JUNGLE_SLAB, SLABS, //STEP
		Material.OAK_SLAB, SLABS, //STEP
		Material.SPRUCE_SLAB, SLABS, //STEP
		//45, XD, //BRICK
		//46, XD, //TNT
		//47, XD, //BOOKSHELF
		//48, XD, //MOSSY_COBBLESTONE
		//49, XD, //OBSIDIAN
		Material.TORCH, STICK, //TORCH
		Material.FIRE, THIN, //FIRE
		//52, XD, //MOB_SPAWNER
		//53, STAIRS, //WOOD_STAIRS
		Material.SANDSTONE_STAIRS, STAIRS, //STEP
		Material.RED_SANDSTONE_STAIRS, STAIRS, //STEP
		Material.STONE_BRICK_STAIRS, STAIRS, //STEP
		Material.BRICK_STAIRS, STAIRS, //STEP
		Material.COBBLESTONE_STAIRS, STAIRS, //STEP
		Material.DARK_PRISMARINE_STAIRS, STAIRS, //STEP
		Material.PRISMARINE_STAIRS, STAIRS, //STEP
		Material.PRISMARINE_BRICK_STAIRS, STAIRS, //STEP
		Material.NETHER_BRICK_STAIRS, STAIRS, //STEP
		Material.QUARTZ_STAIRS, STAIRS, //STEP
		Material.PURPUR_STAIRS, STAIRS, //STEP
		Material.ACACIA_STAIRS, STAIRS, //STEP
		Material.BIRCH_STAIRS, STAIRS, //STEP
		Material.DARK_OAK_STAIRS, STAIRS, //STEP
		Material.JUNGLE_STAIRS, STAIRS, //STEP
		Material.OAK_STAIRS, STAIRS, //STEP
		Material.SPRUCE_STAIRS, STAIRS, //STEP
		//54, XD, //CHEST
		Material.REDSTONE_WIRE, GROUND, //REDSTONE_WIRE
		//56, XD, //DIAMOND_ORE
		//57, XD, //DIAMOND_BLOCK
		//58, XD, //WORKBENCH
		Material.WHEAT, VEGETATION, //CROPS
		Material.POTATOES, VEGETATION, //CROPS
		Material.BEETROOTS, VEGETATION, //CROPS
		Material.CARROTS, VEGETATION, //CROPS
		Material.PUMPKIN_STEM, VEGETATION, //CROPS
		Material.MELON_STEM, VEGETATION, //CROPS
		Material.NETHER_WART, VEGETATION, //CROPS
		//60, XD, //SOIL
		//61, XD, //FURNACE
		//62, XD, //BURNING_FURNACE
		Material.SPRUCE_SIGN, STICK, //SIGN_POST
		Material.SPRUCE_WALL_SIGN, STICK, //SIGN_POST
		Material.ACACIA_SIGN, STICK, //SIGN_POST
		Material.ACACIA_WALL_SIGN, STICK, //SIGN_POST
		Material.BIRCH_SIGN, STICK, //SIGN_POST
		Material.BIRCH_WALL_SIGN, STICK, //SIGN_POST
		Material.DARK_OAK_SIGN, STICK, //SIGN_POST
		Material.DARK_OAK_WALL_SIGN, STICK, //SIGN_POST
		Material.JUNGLE_SIGN, STICK, //SIGN_POST
		Material.JUNGLE_WALL_SIGN, STICK, //SIGN_POST
		Material.OAK_SIGN, STICK, //SIGN_POST
		Material.OAK_WALL_SIGN, STICK, //SIGN_POST
		//64, DOOR, //WOODEN_DOOR
		Material.ACACIA_DOOR, DOOR, //WOODEN_DOOR
		Material.BIRCH_DOOR, DOOR, //WOODEN_DOOR
		Material.DARK_OAK_DOOR, DOOR, //WOODEN_DOOR
		Material.JUNGLE_DOOR, DOOR, //WOODEN_DOOR
		Material.OAK_DOOR, DOOR, //WOODEN_DOOR
		Material.SPRUCE_DOOR, DOOR, //WOODEN_DOOR
		Material.LADDER, THIN, //LADDER
		Material.RAIL, GROUND, //RAILS
		//67, STAIRS, //COBBLESTONE_STAIRS
		//68, THIN, //WALL_SIGN
		Material.LEVER, STICK, //LEVER
		Material.HEAVY_WEIGHTED_PRESSURE_PLATE, GROUND, //PRESSURE_PLATE
		Material.LIGHT_WEIGHTED_PRESSURE_PLATE, GROUND, //PRESSURE_PLATE
		Material.STONE_PRESSURE_PLATE, GROUND, //PRESSURE_PLATE
		Material.IRON_DOOR, DOOR, //IRON_DOOR
		Material.ACACIA_PRESSURE_PLATE, GROUND, //PRESSURE_PLATE
		Material.BIRCH_PRESSURE_PLATE, GROUND, //PRESSURE_PLATE
		Material.DARK_OAK_PRESSURE_PLATE, GROUND, //PRESSURE_PLATE
		Material.JUNGLE_PRESSURE_PLATE, GROUND, //PRESSURE_PLATE
		Material.OAK_PRESSURE_PLATE, GROUND, //PRESSURE_PLATE
		Material.SPRUCE_PRESSURE_PLATE, GROUND, //PRESSURE_PLATE
		//73, XD, //REDSTONE_ORE
		//74, XD, //GLOWING_REDSTONE_ORE
		Material.REDSTONE_TORCH, STICK, //REDSTONE_TORCH
		Material.REDSTONE_WALL_TORCH, STICK, //REDSTONE_WALL_TORCH
		Material.STONE_BUTTON, THIN, //STONE_BUTTON
		Material.ACACIA_BUTTON, THIN, //WOODEN_BUTTON
		Material.BIRCH_BUTTON, THIN, //WOODEN_BUTTON
		Material.DARK_OAK_BUTTON, THIN, //WOODEN_BUTTON
		Material.JUNGLE_BUTTON, THIN, //WOODEN_BUTTON
		Material.OAK_BUTTON, THIN, //WOODEN_BUTTON
		Material.SPRUCE_BUTTON, THIN, //WOODEN_BUTTON
		Material.SNOW, GROUND, //SNOW
		//79, XD, //ICE
		//80, XD, //SNOW_BLOCK
		//81, XD, //CACTUS
		//82, XD, //CLAY
		Material.SUGAR_CANE, VEGETATION, //SUGAR_CANE_BLOCK
		//84, XD, //JUKEBOX
		//85, FENCE, //FENCE
		Material.NETHER_BRICK_FENCE, FENCE, //FENCE
		Material.ACACIA_FENCE, FENCE, //FENCE
		Material.BIRCH_FENCE, FENCE, //FENCE
		Material.DARK_OAK_FENCE, FENCE, //FENCE
		Material.JUNGLE_FENCE, FENCE, //FENCE
		Material.OAK_FENCE, FENCE, //FENCE
		Material.SPRUCE_FENCE, FENCE, //FENCE
		Material.COBBLESTONE_WALL, FENCE, //FENCE
		Material.MOSSY_COBBLESTONE_WALL, FENCE, //FENCE
		//86, XD, //PUMPKIN
		//87, XD, //NETHERRACK
		//88, XD, //SOUL_SAND
		//89, XD, //GLOWSTONE
		//90, XD, //PORTAL
		//91, XD, //JACK_O_LANTERN
		//92, XD, //CAKE_BLOCK
		Material.REPEATER, GROUND, //DIODE_BLOCK
		//94, GROUND, //DIODE_BLOCK_ON
		//95, XD, //LOCKED_CHEST
		//96, DOOR, //TRAP_DOOR
		Material.IRON_TRAPDOOR, DOOR, //TRAP_DOOR
		Material.ACACIA_TRAPDOOR, DOOR, //TRAP_DOOR
		Material.BIRCH_TRAPDOOR, DOOR, //TRAP_DOOR
		Material.DARK_OAK_TRAPDOOR, DOOR, //TRAP_DOOR
		Material.JUNGLE_TRAPDOOR, DOOR, //TRAP_DOOR
		Material.OAK_TRAPDOOR, DOOR, //TRAP_DOOR
		Material.SPRUCE_TRAPDOOR, DOOR, //TRAP_DOOR
		//97, XD, //MONSTER_EGGS
		//98, XD, //SMOOTH_BRICK
		//99, XD, //HUGE_MUSHROOM_1
		//100, XD, //HUGE_MUSHROOM_2
		Material.IRON_BARS, FENCE, //IRON_FENCE
		Material.GLASS_PANE, THIN, //THIN_GLASS
		Material.BLACK_STAINED_GLASS_PANE, THIN, //THIN_GLASS
		Material.BLUE_STAINED_GLASS_PANE, THIN, //THIN_GLASS
		Material.BROWN_STAINED_GLASS_PANE, THIN, //THIN_GLASS
		Material.CYAN_STAINED_GLASS_PANE, THIN, //THIN_GLASS
		Material.GRAY_STAINED_GLASS_PANE, THIN, //THIN_GLASS
		Material.GREEN_STAINED_GLASS_PANE, THIN, //THIN_GLASS
		Material.LIGHT_BLUE_STAINED_GLASS_PANE, THIN, //THIN_GLASS
		Material.LIGHT_GRAY_STAINED_GLASS_PANE, THIN, //THIN_GLASS
		Material.LIME_STAINED_GLASS_PANE, THIN, //THIN_GLASS
		Material.MAGENTA_STAINED_GLASS_PANE, THIN, //THIN_GLASS
		Material.ORANGE_STAINED_GLASS_PANE, THIN, //THIN_GLASS
		Material.PINK_STAINED_GLASS_PANE, THIN, //THIN_GLASS
		Material.PURPLE_STAINED_GLASS_PANE, THIN, //THIN_GLASS
		Material.RED_STAINED_GLASS_PANE, THIN, //THIN_GLASS
		Material.WHITE_STAINED_GLASS_PANE, THIN, //THIN_GLASS
		//103, XD, //MELON_BLOCK
		//104, VEGETATION, //PUMPKIN_STEM
		//105, VEGETATION, //MELON_STEM
		Material.VINE, VEGETATION, //VINE
		//107, DOOR, //FENCE_GATE
		Material.ACACIA_FENCE_GATE, DOOR, //FENCE_GATE
		Material.BIRCH_FENCE_GATE, DOOR, //FENCE_GATE
		Material.DARK_OAK_FENCE_GATE, DOOR, //FENCE_GATE
		Material.JUNGLE_FENCE_GATE, DOOR, //FENCE_GATE
		Material.OAK_FENCE_GATE, DOOR, //FENCE_GATE
		Material.SPRUCE_FENCE_GATE, DOOR, //FENCE_GATE
		//108, STAIRS, //BRICK_STAIRS
		//109, STAIRS, //SMOOTH_STAIRS
		//110, XD, //MYCEL
		Material.LILY_PAD, VEGETATION //WATER_LILY
		//112, XD, //NETHER_BRICK
		//113, FENCE, //NETHER_FENCE
		//114, STAIRS, //NETHER_BRICK_STAIRS
		//115, VEGETATION //NETHER_WARTS
		//116, XD, //ENCHANTMENT_TABLE
		//117, XD, //BREWING_STAND
		//118, XD, //CAULDRON
		//119, XD, //ENDER_PORTAL
		//120, XD, //ENDER_PORTAL_FRAME
		//121, XD, //ENDER_STONE
		//122, XD, //DRAGON_EGG
		//123, XD, //REDSTONE_LAMP_OFF
		//124, XD, //REDSTONE_LAMP_ON
	);
	
	// -------------------------------------------- //
	// ALTARS
	// -------------------------------------------- //
	
	public int altarSearchRadius = 10;
	public double altarMinRatioForInfo = 0;
	
	public AltarDark altarDark = new AltarDark();
	public AltarLight altarLight = new AltarLight();	
	
}
