package com.massivecraft.vampire;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import com.massivecraft.mcore3.util.MUtil;
import com.massivecraft.vampire.altar.AltarEvil;
import com.massivecraft.vampire.altar.AltarGood;


public class Conf
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	public final static transient int taskInterval = 10; // Defines often the task runs.
	
	public static List<String> baseCommandAliases = MUtil.list("v");
	
	// -------------------------------------------- //
	// NAME COLORIZE
	// -------------------------------------------- //
	
	public static Boolean nameColorize = false;
	public static ChatColor nameColor = ChatColor.RED;
	
	// -------------------------------------------- //
	// DROP SELF
	// -------------------------------------------- //
	
	// TODO: Improve this system
	public static Set<Material> dropSelfMaterials = MUtil.set(
		Material.WEB,
		Material.GLOWSTONE,
		Material.BOOKSHELF,
		Material.DEAD_BUSH
	);
	
	// -------------------------------------------- //
	// PERMISSIONS GRANT
	// -------------------------------------------- //
	
	public static Map<String,Boolean> permissionsGrantVampire = MUtil.map(
		"vampire.is.vampire", true,
		"vampire.is.human", false
	);
	
	public static Map<String,Boolean> permissionsGrantHuman = MUtil.map(
		"vampire.is.vampire", false,
		"vampire.is.human", true
	);
	
	// -------------------------------------------- //
	// INFECTION
	// -------------------------------------------- //
	
	public final static transient double fxSmokePerTick = 0.40d;
	public final static transient double fxEnderPerTick = 0.70d;
	public final static transient int fxEnderRandomMaxLen = 2;
	
	public final static transient double infectOfferMaxDistance = 2d;
	public final static transient long infectOfferToleranceTicks = 20 * 20;
	
	// It will take you 1h to turn
	public static double infectionPerTick = 1D / (20*60*60D);
	public static double infectionPerBread = -0.2D;
	
	public static int infectionProgressNauseaTicks = 40;
	public static int infectionProgressDamage = 1;
	
	public static Double infectionRiskAtCloseCombatWithoutIntent = 0.003;
	public static Double infectionRiskAtCloseCombatWithIntent = 0.05;
	
	// -------------------------------------------- //
	// BLOCK EVENTS
	// -------------------------------------------- //
	// TODO: Split into HEALTH (with regen speed and such features), FOOD for level change stuff and so on?
	// TODO: Conf.foodCanEat is very related to the block health from.
	
	public static Set<DamageCause> blockDamageFrom = MUtil.set(
		DamageCause.DROWNING,
		DamageCause.FALL,
		DamageCause.STARVATION
	);
	
	public static Set<RegainReason> blockHealthFrom = MUtil.set(
		RegainReason.SATIATED,
		RegainReason.REGEN	
	);
	
	// -------------------------------------------- //
	// RESPAWN
	// -------------------------------------------- //
	
	public static int respawnFood = 2;
	public static int respawnHealth = 20;
	
	// -------------------------------------------- //
	// REGEN
	// -------------------------------------------- //
	
	public static double regenMinFood = 2.5D;
	public static int regenDelayMillis = 3*1000;
	public static double regenFoodPerTick = 0.025D;
	public static double regenHealthPerFood = 2D;
	
	// -------------------------------------------- //
	// BLOODLUST
	// -------------------------------------------- //
	
	public static double bloodlustMinFood = 2.5D;
	public static double bloodlustFoodPerTick = -0.005D;
	public static double bloodlustSmokes = 1.5D;
	
	public static Double multGravityBloodlust = null;
	public static Double multSwimmingBloodlust = 3D;
	public static Double multWalkingBloodlust = 3D;
	public static Double multJumpingBloodlust = 3D;
	public static Double multAirSpeedBloodlust = 4D;
	
	public static Double multGravityVamp = null;
	public static Double multSwimmingVamp = 1D;
	public static Double multWalkingVamp = 1D;
	public static Double multJumpingVamp = 1.5D;
	public static Double multAirSpeedVamp = 1.5D;
	
	public static Double multGravityHuman = null;
	public static Double multSwimmingHuman = 1D;
	public static Double multWalkingHuman = 1D;
	public static Double multJumpingHuman = 1D;
	public static Double multAirSpeedHuman = 1D;
	
	
	// -------------------------------------------- //
	// FOOD
	// -------------------------------------------- //
	
	public static boolean foodCakeAllowed = true;
	
	// Food from blood drinking (damaging)
	private final static transient Double foodPerPlayerDamage = 0.2D;
	public static Map<EntityType, Double> foodPerDamageFromCreature = MUtil.map(
		EntityType.PLAYER,       foodPerPlayerDamage,
		EntityType.CHICKEN,      foodPerPlayerDamage / 2D,
		EntityType.COW,          foodPerPlayerDamage / 2D,
		EntityType.PIG,          foodPerPlayerDamage / 2D,
		EntityType.SHEEP,        foodPerPlayerDamage / 2D,
		EntityType.SPIDER,       foodPerPlayerDamage / 5D,
		EntityType.CAVE_SPIDER,  foodPerPlayerDamage / 5D,
		EntityType.SQUID,        foodPerPlayerDamage / 5D
	);	
	
	// -------------------------------------------- //
	// COMBAT
	// -------------------------------------------- //
	
	public static double damageDealtFactorWithoutIntent = 1.2;
	public static double damageDealtFactorWithIntent = 0.65;
	public static double damageReceivedFactorWithoutIntent = 0.80;
	public static double damageReceivedFactorWithIntent = 1.0;
	
	public static int damageReceivedWood = 14;
	
	public final static transient Set<Material> woodMaterials = MUtil.set(
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
	// TRUCE
	// -------------------------------------------- //
	
	// One minute
	public static long truceBreakTicks = 60 * 20; 
	
	// These are the creature types that won't target vampires
	public static Set<EntityType> truceEntityTypes = MUtil.set(
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
	// SUN
	// -------------------------------------------- //
	
	public static double opacityPerArmorPice = 0.125d;
	public static double baseRad = -0.2d;
	public static double tempPerRadAndTick = 1d / (5d * 20d); // it should take 5 seconds to reach max temp in maximum sunlight.
	
	public static double sunNauseaTemp = 0.20d;
	public static double sunSlowTemp = 0.30d;
	public static double sunBlindnessTemp = 0.40d;
	public static double sunBurnTemp = 0.50d;
	
	public static int sunNauseaTicks = 10*20;
	public static int sunSlowTicks = 10*20;
	public static int sunBlindnessTicks = 10*20;
	public static int sunBurnTicks = 3*20;	
	
	public static double sunSmokesPerTempAndTick = 1.00d;
	public static double sunFlamesPerTempAndTick = 0.05d;
	
	//We assume opacity 1 for all materials not in this map
	public static Map<Integer,Double> typeIdOpacity = MUtil.map(
		Material.AIR.getId(), 0D,
		Material.SAPLING.getId(), 0.3D,
		Material.LEAVES.getId(), 0.3D,
		Material.GLASS.getId(), 0.5D, // Double glass means UV protection :)
		Material.YELLOW_FLOWER.getId(), 0.1D,
		Material.RED_ROSE.getId(), 0.1D,
		Material.BROWN_MUSHROOM.getId(), 0.1D,
		Material.RED_MUSHROOM.getId(), 0.1D,
		Material.TORCH.getId(), 0.1D,
		Material.FIRE.getId(), 0D,
		Material.MOB_SPAWNER.getId(), 0.3D,
		Material.REDSTONE_WIRE.getId(), 0D,
		Material.CROPS.getId(), 0.2D,
		Material.SIGN.getId(), 0.1D,
		Material.SIGN_POST.getId(), 0.2D,
		Material.LEVER.getId(), 0.1D,
		Material.STONE_PLATE.getId(), 0D,
		Material.WOOD_PLATE.getId(), 0D,
		Material.REDSTONE_TORCH_OFF.getId(), 0.1D,
		Material.REDSTONE_TORCH_ON.getId(), 0.1D,
		Material.STONE_BUTTON.getId(), 0D,
		Material.SUGAR_CANE_BLOCK.getId(), 0.3D,
		Material.FENCE.getId(), 0.2D,
		Material.DIODE_BLOCK_OFF.getId(), 0D,
		Material.DIODE_BLOCK_ON.getId(), 0D,
		Material.SNOW.getId(), 0.1D
	);

	// -------------------------------------------- //
	// ALTARS
	// -------------------------------------------- //
	
	public static int altarSearchRadius = 10;
	
	public static AltarEvil altarEvil = new AltarEvil();
	public static AltarGood altarGood = new AltarGood();	
	
	// -------------------------------------------- //
	// Persistance
	// -------------------------------------------- //
	private static transient Conf i = new Conf();
	public static void load()
	{
		P.p.one.loadOrSaveDefault(i, Conf.class);
	}
	public static void save()
	{
		P.p.one.save(i);
	}
}
