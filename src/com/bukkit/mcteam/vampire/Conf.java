package com.bukkit.mcteam.vampire;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.bukkit.*;
import org.bukkit.entity.CreatureType;

import com.bukkit.mcteam.util.DiscUtil;

public class Conf {
	public static transient File file = new File(Vampire.instance.getDataFolder(), "conf.json");
	
	public static ChatColor colorSystem = ChatColor.RED;
	
	public static int timerInterval = 1000; // Defines the precision of the vampire timer. one second should be good. 
	
	public static double infectionCloseCombatRisk = 1d / 30d;
	public static double infectionCloseCombatAmount = 3D;
	public static double infectionProgressPerSecond = 100D / (1*60*60D) ; // It will take you 1h to turn
	//public static double infectionProgressPerSecond = 100D / (5*60D) ; // It will take you 1min to turn DEBUG SETTING
	
	public static double infectionBreadHealAmount = 20D;
	public static String infectionMessageHeal = "You feel a little better. Bread helps you fight the sickness.";
	public static String infectionMessageCured = "You are now completely cured from the sickness you had.";
	public static String vampiresCantEatFoodMessage = "Vampires can't eat food. You must drink blood instead.";
	public static String messageCommandBaseNoVampire = "You don't know that much about vampires.";
	public static String messageTruceBroken = "You temporarily broke your truce with the monsters.";
	public static String messageTruceRestored = "Your truce with the monsters has been restored.";
	public static String messageBloodMeter = "%1$.1f blood";
	public static String messageBloodMeterWithDiffAndReason = "%1$.1f blood %2$+.1f %3$s";
	public static String messageBloodDrinkReason = "from %s";
	
	public static double thirstUnderBlood = 50D;
	public static double thirstStrongUnderBlood = 20D;
	public static double thirstDamagePerSecond = 0.025; // Once every 40seconds
	public static double thirstStrongDamagePerSecond = 0.1; // Once every 10seconds
	public static String thirstDeathMessage = "You thirsted to death. Drink more blood to avoid this.";
	public static List<String> thirstMessages = new ArrayList<String>();
	public static List<String> thirstStrongMessages = new ArrayList<String>();
	
	
	public static Set<Material> dashMaterials = new HashSet<Material>();
	public static int dashMaxLength = 15;
	public static double dashBloodPerBlock = 1.7;
	public static String dashMessageToLong = "To long to dash.";
	public static String dashMessageTargetToSmall = "That space is to small to dash into.";
	public static String dashMessageNotEnoughBlood = "Not enough blood to dash.";
	
	public static long regenStartDelayMilliseconds = 8000;
	public static double regenHealthPerSecond = 0.25;
	public static double regenBloodPerHealth = 2.5D;
	public static String regenStartMessage = "Your wounds start to close. This drains blood over time.";
	
	public static double combatDamageDealtFactor = 1.5;
	public static double combatDamageReceivedFactor = 0.7;
	public static double combatDamageReceivedWoodFactor = 3.5;
	public static String messageWoodCombatWarning = "Wood weapons hurt vampires a lot! Watch out!";
	
	public static Set<Material> woodMaterials = new HashSet<Material>();
	
	public static Set<Material> foodMaterials = new HashSet<Material>();
	
	// Time is "hours passed since the beginning of the day" * 1000
	public static int combustFromTime = 0;
	public static int combustToTime = 12400;
	public static int combustFireExtinguishTicks = 0; // After how many minecraft game ticks of no sunlight should the fire stop? 0 means right away.
	public static String combustMessage = "Vampires burn in sunlight! Take cover!";
	
	public static Map<Material,Double> materialOpacity = new HashMap<Material,Double>(); //We assume opacity 1 for all materials not in this map 
	
	public static double playerBloodQuality = 2D;
	public static Map<CreatureType, Double> creatureTypeBloodQuality = new HashMap<CreatureType, Double>();
	public static long truceBreakTime = 60 * 1000L; // One minute
	public static Set<CreatureType> creatureTypeTruceMonsters = new HashSet<CreatureType>();
	public static long bloodDecreasePerMinute = 100/120L; //You loose all your blood over the elapse of 2 real-life hours
	public static List<String> infectionMessagesProgress = new ArrayList<String>();
	public static List<String> infectionBreadHintMessages = new ArrayList<String>();
	public static List<String> turnMessages = new ArrayList<String>();
	public static List<String> cureMessages = new ArrayList<String>();
	
	static {
		// TODO ADD MORE!
		thirstMessages.add("You are a bit thirsty...");
		thirstMessages.add("Some blood would taste good...");
		thirstMessages.add("You daydream about whose blood shall drink today...");
		thirstMessages.add("Your teeth are restless...");
		thirstMessages.add("Maybe I should stab someone and lick my hands clean...");
		
		// TODO ADD MORE!
		thirstStrongMessages.add("You crave for blood!");
		thirstStrongMessages.add("You need to taste blood now!");
		thirstStrongMessages.add("The thirst is unbearable!");
		thirstStrongMessages.add("Your skin is turning grey!");
		thirstStrongMessages.add("You stumble forward through red fog!");
		
		dashMaterials.add(Material.FEATHER);
		
		foodMaterials.add(Material.APPLE);
		foodMaterials.add(Material.BREAD);
		foodMaterials.add(Material.COOKED_FISH);
		foodMaterials.add(Material.GRILLED_PORK);
		foodMaterials.add(Material.GOLDEN_APPLE);
		foodMaterials.add(Material.MUSHROOM_SOUP);
		foodMaterials.add(Material.PORK);
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
		
		// As it take 1h to turn...
		// About 20-30 messages would be awesome. 
		infectionMessagesProgress.add("You feel a bit tired.");
		infectionMessagesProgress.add("You feel dizzy. Could you have contracted something?");
		infectionMessagesProgress.add("You feel sick. It will probably get better in a while.");
		infectionMessagesProgress.add("You feel ill. Your heart beats harder than normally."); 
		infectionMessagesProgress.add("You feel cold in a peculiar way.");
		infectionMessagesProgress.add("You wonder why the sun hurts your eyes so much.");
		infectionMessagesProgress.add("Everything sounds to loud. You get a headache.");
		infectionMessagesProgress.add("Something tells you this is not a normal sickness.");
		infectionMessagesProgress.add("You are thirsty but water does not seem to help.");
		infectionMessagesProgress.add("You find yourself thinking about blood.");
		infectionMessagesProgress.add("You wonder what blood would taste like...");
		infectionMessagesProgress.add("You wonder whats wrong. Why are you obsessed with blood?");
		infectionMessagesProgress.add("You teeth really hurt. It feels like they are growing.");
		infectionMessagesProgress.add("You teeth starts to bleed and you keep swallowing the blood.");
		infectionMessagesProgress.add("You vomit and feel awful... but stronger than before.");
		infectionMessagesProgress.add("When people walk by you really feel like biting them...");
		infectionMessagesProgress.add("You get this impulse to kill one of your friends.");
		infectionMessagesProgress.add("You cry and wonder if you should ask for help.");
		infectionMessagesProgress.add("Your chest starts to hurt enormously.");
		infectionMessagesProgress.add("It feels your blood is dark grease and needles.");
		infectionMessagesProgress.add("The pain is unbearable. You must be dying.");
		infectionMessagesProgress.add("It feels like your body is dying but your soul is trapped.");
		infectionMessagesProgress.add("You almost can't breathe and you are freezing.");
		infectionMessagesProgress.add("Your heart is barely beating...");
		
		// TODO ADD MORE
		// About 10-20 messages would be awesome.
		infectionBreadHintMessages.add("Bread might cure your.");
		infectionBreadHintMessages.add("Eating some bread might do you good.");
		infectionBreadHintMessages.add("Freshly baked bread might cure you.");
		
		turnMessages.add("Your heart stops. You don't breathe anymore.");
		turnMessages.add("You are now a vampire.");
		turnMessages.add("Type /vampire help for more info.");
		
		cureMessages.add("You have been cured from the vampirism.");
		cureMessages.add("You are once again healthy and alive.");
	}
	
	// -------------------------------------------- //
	// Persistance
	// -------------------------------------------- //
	
	public static boolean save() {
		Vampire.log("Saving config to disk.");
		try {
			DiscUtil.write(file, Vampire.gson.toJson(new Conf()));
		} catch (IOException e) {
			e.printStackTrace();
			Vampire.log("Failed to save the config to disk.");
			return false;
		}
		return true;
	}
	
	public static boolean load() {
		if ( ! file.exists()) {
			Vampire.log("No conf to load from disk. Creating new file.");
			save();
			return true;
		}
		
		try {
			Vampire.gson.fromJson(DiscUtil.read(file), Conf.class);
		} catch (IOException e) {
			e.printStackTrace();
			Vampire.log("Failed to load the config from disk.");
			return false;
		}
		
		return true;
	}
}
