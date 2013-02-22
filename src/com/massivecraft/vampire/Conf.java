package com.massivecraft.vampire;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore.store.Entity;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.vampire.altar.AltarDark;
import com.massivecraft.vampire.altar.AltarLight;

public class Conf extends Entity<Conf, String>
{
	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static Conf get(Object worldNameExtractable)
	{
		return ConfColls.i.get2(worldNameExtractable);
	}
	
	// -------------------------------------------- //
	// FX
	// -------------------------------------------- //
	
	public double fxSmokePerTick = 0.40D;
	public double fxEnderPerTick = 0.10D;
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
		Material.WEB,
		Material.GLOWSTONE,
		Material.BOOKSHELF,
		Material.DEAD_BUSH
	);
	
	// -------------------------------------------- //
	// BLOODLUST
	// -------------------------------------------- //
	
	public double bloodlustMinFood = 2.5D;
	public double bloodlustFoodPerTick = -0.015D;
	public double bloodlustSmokes = 1.5D;
	
	public Map<Integer, Integer> potionEffectStrengthBloodlust = MUtil.map(
		8, 4,
		1, 4
	);
	
	public Map<Integer, Integer> potionEffectStrengthVamp = MUtil.map(
		8, 1,
		1, 0
	);
	
	public Map<Integer, Integer> potionEffectStrengthHuman = MUtil.map(
		8, 0,
		1, 0
	);
	
	// -------------------------------------------- //
	// NIGHTVISION
	// -------------------------------------------- //
	
	public boolean nightvisionCanBeUsed = true;
	public int nightvisionPulseTicks = 420;
	
	// -------------------------------------------- //
	// REGEN
	// -------------------------------------------- //
	
	public double regenMinFood = 2.5D;
	public int regenDelayMillis = 10*1000;
	public double regenFoodPerTick = 0.025D;
	public double regenHealthPerFood = 2D;
	
	// -------------------------------------------- //
	// TRUCE
	// -------------------------------------------- //
	
	// One minute
	public long truceBreakTicks = 60 * 20; 
	
	// These are the creature types that won't target vampires
	public Set<EntityType> truceEntityTypes = MUtil.set(
		EntityType.BLAZE,
		EntityType.CAVE_SPIDER,
		EntityType.CREEPER,
		EntityType.ENDERMAN,
		EntityType.GHAST,
		EntityType.GIANT,
		EntityType.MAGMA_CUBE,
		EntityType.PIG_ZOMBIE,
		EntityType.SKELETON,
		EntityType.SPIDER,
		EntityType.ZOMBIE
	);

	
	// -------------------------------------------- //
	// COMBAT
	// -------------------------------------------- //
	
	public double combatDamageFactorWithoutBloodlust = 1.0;
	public double combatDamageFactorWithBloodlust = 1.2;
	
	private final static transient int damageDiamondSword = 7;
	public int combatWoodDamage = 3*damageDiamondSword;
	
	public Set<Material> combatWoodMaterials = MUtil.set(
		Material.WOOD_AXE,
		Material.WOOD_HOE,
		Material.WOOD_PICKAXE,
		Material.WOOD_SPADE,
		Material.WOOD_SWORD,
		Material.STICK,
		Material.TORCH,
		Material.REDSTONE_TORCH_OFF,
		Material.REDSTONE_TORCH_ON,
		Material.SIGN,
		Material.SIGN_POST,
		Material.FENCE,
		Material.FENCE_GATE
	);
	
	// -------------------------------------------- //
	// INFECTION
	// -------------------------------------------- //
	
	// It will take you 1h to turn
	public double infectionPerTick = 1D / (20*60*60D);
	
	public int infectionProgressNauseaTicks = 12*20;
	public int infectionProgressDamage = 1;
	
	public Double infectionRiskAtCloseCombatWithoutIntent = 0.003;
	public Double infectionRiskAtCloseCombatWithIntent = 0.05;
	
	// -------------------------------------------- //
	// TRADE
	// -------------------------------------------- //
	
	public double tradeOfferMaxDistance = 2d;
	public long tradeOfferToleranceMillis = 20 * 1000;
	public double tradeVisualDistance = 7D;

	// -------------------------------------------- //
	// FOOD
	// -------------------------------------------- //
	
	public boolean foodCakeAllowed = true;
	
	public Map<EntityType, Double> entityTypeFullFoodQuotient = MUtil.map(
		EntityType.CREEPER,        0/20D,
		EntityType.SKELETON,       0/20D,
		EntityType.SPIDER,         3/20D,
		EntityType.GIANT,         50/20D,
		EntityType.ZOMBIE,         0/20D,
		EntityType.SLIME,          0/20D,
		EntityType.GHAST,          0/20D,
		EntityType.PIG_ZOMBIE,     0/20D,
		EntityType.ENDERMAN,       0/20D,
		EntityType.CAVE_SPIDER,    3/20D,
		EntityType.SILVERFISH,     1/20D,
		EntityType.BLAZE,          0/20D,
		EntityType.MAGMA_CUBE,     0/20D,
		EntityType.ENDER_DRAGON, 140/20D,
		EntityType.PIG,            5/20D,
		EntityType.SHEEP,          5/20D,
		EntityType.COW,            7/20D,
		EntityType.CHICKEN,        2/20D,
		EntityType.SQUID,          4/20D,
		EntityType.WOLF,           5/20D,
		EntityType.MUSHROOM_COW,  20/20D,
		EntityType.SNOWMAN,        0/20D,
		EntityType.OCELOT,         5/20D,
		EntityType.IRON_GOLEM,     0/20D,
		EntityType.VILLAGER,      10/20D,
		EntityType.PLAYER,        10/20D
	);

	// -------------------------------------------- //
	// HOLY WATER
	// -------------------------------------------- //
	
	public double holyWaterSplashRadius = 6D;
	public double holyWaterTemp = 0.7D;
	
	public List<ItemStack> holyWaterResources = MUtil.list(
		new ItemStack(Material.POTION, 1, (short)0),
		new ItemStack(Material.INK_SACK, 1, (short)4)
	);
	
	// -------------------------------------------- //
	// SUN
	// -------------------------------------------- //
	
	public double opacityPerArmorPiece = 0.125d;
	public double baseRad = -0.2d;
	public double tempPerRadAndTick = 1d / (10d * 20d); // it should take 10 seconds to reach max temp in maximum sunlight.
	
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
	
	public double sunSmokesPerTempAndTick = 0.60d;
	public double sunFlamesPerTempAndTick = 0.02d;
	
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
	
	public Map<Integer,Double> typeIdOpacity = MUtil.map(
		0, AIR, //AIR
		//1, XD, //STONE
		//2, XD, //GRASS
		//3, XD, //DIRT
		//4, XD, //COBBLESTONE
		//5, XD, //WOOD
		6, VEGETATION, //SAPLING
		//7, XD, //BEDROCK
		8, WATER, //WATER
		9, WATER, //STATIONARY_WATER
		//10, XD, //LAVA
		//11, XD, //STATIONARY_LAVA
		//12, XD, //SAND
		//13, XD, //GRAVEL
		//14, XD, //GOLD_ORE
		//15, XD, //IRON_ORE
		//16, XD, //COAL_ORE
		//17, XD, //LOG
		18, VEGETATION, //LEAVES
		//19, XD, //SPONGE
		20, GLASS, //GLASS
		//21, XD, //LAPIS_ORE
		//22, XD, //LAPIS_BLOCK
		//23, XD, //DISPENSER
		//24, XD, //SANDSTONE
		//25, XD, //NOTE_BLOCK
		//26, XD, //BED_BLOCK
		27, GROUND, //POWERED_RAIL
		28, GROUND, //DETECTOR_RAIL
		//29, XD, //PISTON_STICKY_BASE
		30, THIN, //WEB
		31, VEGETATION, //LONG_GRASS
		32, VEGETATION, //DEAD_BUSH
		//33, XD, //PISTON_BASE
		34, STICK, //PISTON_EXTENSION
		//35, XD, //WOOL
		//36, XD, //PISTON_MOVING_PIECE
		37, VEGETATION, //YELLOW_FLOWER
		38, VEGETATION, //RED_ROSE
		39, VEGETATION, //BROWN_MUSHROOM
		40, VEGETATION, //RED_MUSHROOM
		//41, XD, //GOLD_BLOCK
		//42, XD, //IRON_BLOCK
		//43, XD, //DOUBLE_STEP
		44, SLABS, //STEP
		//45, XD, //BRICK
		//46, XD, //TNT
		//47, XD, //BOOKSHELF
		//48, XD, //MOSSY_COBBLESTONE
		//49, XD, //OBSIDIAN
		50, STICK, //TORCH
		51, THIN, //FIRE
		//52, XD, //MOB_SPAWNER
		53, STAIRS, //WOOD_STAIRS
		//54, XD, //CHEST
		55, GROUND, //REDSTONE_WIRE
		//56, XD, //DIAMOND_ORE
		//57, XD, //DIAMOND_BLOCK
		//58, XD, //WORKBENCH
		59, VEGETATION, //CROPS
		//60, XD, //SOIL
		//61, XD, //FURNACE
		//62, XD, //BURNING_FURNACE
		63, STICK, //SIGN_POST
		64, DOOR, //WOODEN_DOOR
		65, THIN, //LADDER
		66, GROUND, //RAILS
		67, STAIRS, //COBBLESTONE_STAIRS
		68, THIN, //WALL_SIGN
		69, STICK, //LEVER
		70, GROUND, //STONE_PLATE
		71, DOOR, //IRON_DOOR_BLOCK
		72, GROUND, //WOOD_PLATE
		//73, XD, //REDSTONE_ORE
		//74, XD, //GLOWING_REDSTONE_ORE
		75, STICK, //REDSTONE_TORCH_OFF
		76, STICK, //REDSTONE_TORCH_ON
		77, THIN, //STONE_BUTTON
		78, GROUND, //SNOW
		//79, XD, //ICE
		//80, XD, //SNOW_BLOCK
		//81, XD, //CACTUS
		//82, XD, //CLAY
		83, VEGETATION, //SUGAR_CANE_BLOCK
		//84, XD, //JUKEBOX
		85, FENCE, //FENCE
		//86, XD, //PUMPKIN
		//87, XD, //NETHERRACK
		//88, XD, //SOUL_SAND
		//89, XD, //GLOWSTONE
		//90, XD, //PORTAL
		//91, XD, //JACK_O_LANTERN
		//92, XD, //CAKE_BLOCK
		93, GROUND, //DIODE_BLOCK_OFF
		94, GROUND, //DIODE_BLOCK_ON
		//95, XD, //LOCKED_CHEST
		96, DOOR, //TRAP_DOOR
		//97, XD, //MONSTER_EGGS
		//98, XD, //SMOOTH_BRICK
		//99, XD, //HUGE_MUSHROOM_1
		//100, XD, //HUGE_MUSHROOM_2
		101, FENCE, //IRON_FENCE
		102, THIN, //THIN_GLASS
		//103, XD, //MELON_BLOCK
		104, VEGETATION, //PUMPKIN_STEM
		105, VEGETATION, //MELON_STEM
		106, VEGETATION, //VINE
		107, DOOR, //FENCE_GATE
		108, STAIRS, //BRICK_STAIRS
		109, STAIRS, //SMOOTH_STAIRS
		//110, XD, //MYCEL
		111, VEGETATION, //WATER_LILY
		//112, XD, //NETHER_BRICK
		113, FENCE, //NETHER_FENCE
		114, STAIRS, //NETHER_BRICK_STAIRS
		115, VEGETATION //NETHER_WARTS
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
