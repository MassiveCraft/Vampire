package com.massivecraft.vampire.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.massivecraft.vampire.P;
import com.massivecraft.vampire.util.DiscUtil;

public class Lang
{
	public static transient File file = new File(P.instance.getDataFolder(), "lang.json");
	
	public static String infectionMessageHeal = "You feel a little better. Bread helps you fight the sickness.";
	public static String infectionMessageCured = "You are now completely cured from the sickness you had.";
	public static String vampiresCantEatFoodMessage = "Vampires can't eat food. You must drink blood instead.";
	public static String messageTruceBroken = "You temporarily broke your truce with the monsters.";
	public static String messageTruceRestored = "Your truce with the monsters has been restored.";
	public static String messageBloodMeter = "%1$.1f blood";
	public static String messageTrueBloodVampire = "You are a True Blood vampire.";
	public static String messageBasicVampire = "You are a common vampire.";
	public static String messageBloodMeterWithDiffAndReason = "%1$.1f blood %2$+.1f %3$s";
	public static String messageBloodDrinkReason = "from %s";
	public static String messageDeadBecomeVampire = "You feel the dust in your heart. You are maybe infected.";
	
	public static String thirstDeathMessage = "You thirsted to death. Drink more blood to avoid this.";

	public static String jumpMessageNotEnoughBlood = "Not enough blood to jump.";

	public static String regenStartMessage = "Your wounds start to close. This drains blood over time.";
	
	public static String messageWoodCombatWarning = "Wood weapons hurt vampires a lot! Watch out!";
	
	public static String combustMessage = "Vampires burn in sunlight! Take cover!";
	
	public static List<String> infectionMessagesProgress = new ArrayList<String>();
	public static List<String> infectionBreadHintMessages = new ArrayList<String>();
	public static List<String> turnMessages = new ArrayList<String>();
	public static List<String> turnTrueBloodMessages = new ArrayList<String>();
	public static List<String> cureMessages = new ArrayList<String>();
	public static List<String> helpMessages = new ArrayList<String>();
	public static List<String> thirstMessages = new ArrayList<String>();
	public static List<String> thirstStrongMessages = new ArrayList<String>();
	
	public static String altarUseIngredientsSuccess = "You use these items on the altar:";
	public static String altarUseIngredientsFail = "To use it you need to collect these ingredients:";
	
	public static String altarInfectExamineMsg = "This altar looks really evil.";
	public static String altarInfectExamineWhileInfected = "You feel some of it's energy inside you...";
	public static String altarInfectExamineMsgNoUse = "You understand this altar can turn people into vampires. But it's of no use to you as you already is one.";
	public static String altarInfectToSmall = "Something happens... The gold draws energy from the obsidian... But there don't seem to be enough obsidian nearby.";
	public static String altarInfectUse = "Something happens... The gold draws energy from the obsidian... Then the energy rushes through you and you feel a bit cold...";

	public static String altarCureExamineMsg = "This altar looks bright and nice.";
	public static String altarCureExamineWhileInfected = "You touch it and warm energy flows through you.";
	public static String altarCureExamineMsgNoUse = "It can probably cure curses, but you feel well right now.";

	public static String altarCureToSmall = "Something happens... The lapiz draws energy from the glowstone... But there don't seem to be enough glowstone nearby.";
	public static String altarCureUse = "Something happens... The lapiz draws energy from the glowstone... Then the energy rushes through you and you feel warm inside.";

	static
	{
		// Thank you for helping me with the messages:
		// * derbycar379
		// * karibu6
		// * cooldude8909
		
		// TODO ADD MORE!
		thirstMessages.add("You are a bit thirsty...");
		thirstMessages.add("Some blood would taste good...");
		thirstMessages.add("You daydream about whose blood you shall drink today...");
		thirstMessages.add("Your teeth are restless...");
		thirstMessages.add("Maybe I should stab someone and lick my hands clean...");
		thirstMessages.add("You miss the smell of blood...");
		
		// TODO ADD MORE!
		thirstStrongMessages.add("You crave for blood!");
		thirstStrongMessages.add("You need to taste blood now!");
		thirstStrongMessages.add("The thirst is unbearable!");
		thirstStrongMessages.add("Your skin is turning grey!");
		thirstStrongMessages.add("You feel what's left of your half-life faint away!");
		thirstStrongMessages.add("You feel weak!");
		thirstStrongMessages.add("You start to keel over!");
		thirstStrongMessages.add("No one can help you now!");
		thirstStrongMessages.add("Your vison turns red and blurry!");
		thirstStrongMessages.add("You fear for your half-life!");
		
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
		infectionMessagesProgress.add("Your teeth really hurt. It feels like they are growing.");
		infectionMessagesProgress.add("Your teeth starts to bleed and you keep swallowing the blood.");
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
		infectionBreadHintMessages.add("Eating some bread might do you good.");
		infectionBreadHintMessages.add("You see a faint image... its a loaf of bread...");
		infectionBreadHintMessages.add("Bread would taste very good right now...");
		infectionBreadHintMessages.add("You think of mother's fresh homeade bread...");
		infectionBreadHintMessages.add("The thought of bread makes you feel warm inside...");
		infectionBreadHintMessages.add("Maybe you should see the local farmer...");
		infectionBreadHintMessages.add("Bread...Bread...Bread...");
		
		turnMessages.add("Your heart stops. You don't breathe anymore.");
		turnMessages.add("You are now a vampire. To see your blood supply:");
		turnMessages.add("Type \"/v\" or simply \"v\" in the chat.");
		
		turnTrueBloodMessages.add("Your heart stops. You don't breathe anymore.");
		turnTrueBloodMessages.add("You are now a True Blood vampire, able to infect.");
		turnTrueBloodMessages.add("To see your blood supply:");
		turnTrueBloodMessages.add("Type \"/v\" or simply \"v\" in the chat.");
		
		cureMessages.add("You have been cured from the vampirism.");
		cureMessages.add("You are once again healthy and alive.");
	}
	
	// -------------------------------------------- //
	// Persistance
	// -------------------------------------------- //
	
	public static boolean save() {
		P.log("Saving config to disk.");
		try {
			DiscUtil.write(file, P.gson.toJson(new Lang()));
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
			P.gson.fromJson(DiscUtil.read(file), Lang.class);
		} catch (IOException e) {
			e.printStackTrace();
			P.log("Failed to load the config from disk.");
			return false;
		}
		
		return true;
	}
}
