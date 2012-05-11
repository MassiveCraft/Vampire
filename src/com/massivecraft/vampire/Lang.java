package com.massivecraft.vampire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Lang
{
	public static String consolePlayerArgRequired = "<b>You must specify player from console.";
	
	public static String noSpoutWarnHuman = "";
	public static String noSpoutWarnVamp = "<b>Use SpoutCraft for improved vampire movement.\n<aqua>http://dev.bukkit.org/client-mods/spoutcraft/";
	public static String noSpoutWarnBloodlust = "<b>Use SpoutCraft! Bloodlust will then grant you faster movement.<aqua>http://dev.bukkit.org/client-mods/spoutcraft/";
	
	public static String on = "<lime>ON";
	public static String off = "<lime>OFF";
	public static String xIsOn = "<i>%s is <lime>ON<i>.";
	public static String xIsOff = "<i>%s is <rose>OFF<i>.";
	
	public static String infectionMessageHeal = "<g>You feel a little better. Bread helps you fight the sickness.";
	public static String infectionMessageCured = "<g>You are now completely cured from the sickness you had.";
	public static String vampiresCantEatThat = "<b>Vampires can't eat %s.";
	public static String messageTruceBroken = "<b>You temporarily broke your truce with the monsters.";
	public static String messageTruceRestored = "<g>Your truce with the monsters has been restored.";
	
	public static String intentOnMessage  = "<i>You now <h>do intend <i>to infect others in combat.";
	public static String intentOffMessage = "<i>You now <h>don't intend <i>to infect others in combat.";
	
	public static String messageWoodCombatWarning = "<b>Ouch!!! <h>%s <b>is made of <h>wood <b>and hurt vampires a lot!";
	
	public static String youWasTurned = "<b>Your heart stops. You don't breathe anymore. You are now a VAMPIRE!";
	public static String xWasTurned = "<h>%s <i>is now a vampire.";
	
	public static String youWasCured = "<g>You have been cured and is once again healthy and alive.<b> But will you miss the taste of blood on your lips? When you sleep, will you taste the salt and copper flowing over your tongue? Go, mortal. Bask in your precious sunlight.";
	//public static String xWasCured = "<h>%s <i>was cured and is no longer a vampire.";
	//public static String xIsHealthy = "<h>%s <i>is healthy. No need for a cure.";
	
	//public static String xNowHasFoodY = "<h>%s <i>now has food <h>%d<i>.";
	//public static String xNowHasYInfection = "<h>%s <i>now has <h>%.1f%%<i> infection.";
	
	public static String infectYouMustStandCloseToY = "<b>You must stand close to <h>%s <i>for this to work.";
	public static String infectXOffersToInfectYou = "<h>%s<i> offers to infect you with the vampire disease.";
	public static String infectYouOfferToInfectX = "<i>You offer to infect <h>%s<i> with the vampire disease.";
	public static String infectTypeXToAccept = "<i>Type %s<i> to accept.";
	public static String infectNoRecentOffer = "<b>No vampire offered to infect you recently.";
	//public static String infectYouDrinkSomeOfXBlood = "<i>You drink some of <h>%s's<i> blood.";
	public static String infectXDrinkSomeOfYourBlood = "<h>%s<i> drinks some of your blood.";
	
	public static List<String> infectionMessagesProgress = new ArrayList<String>();
	public static List<String> infectionBreadHintMessages = new ArrayList<String>();
	/*public static List<String> turnMessages = new ArrayList<String>(); // TODO
	public static List<String> turnTrueBloodMessages = new ArrayList<String>();
	public static List<String> cureMessages = new ArrayList<String>();*/
	
	public static List<String> helpMessages = new ArrayList<String>();
	//public static List<String> thirstMessages = new ArrayList<String>();
	//public static List<String> thirstStrongMessages = new ArrayList<String>();
	
	
	public static String altarIncomplete = "<i>This is an incomplete %s. This is missing:";
	public static String altarUseIngredientsSuccess = "<i>You use these items on the altar:";
	public static String altarUseIngredientsFail = "<i>To use it you need to collect these ingredients:";
	
	public static String altarEvilAlreadyInfected = "<i>You feel some of it's energy inside you...";
	public static String altarEvilAlreadyVampire = "<i>You understand this altar can turn people into vampires. But it's of no use to you as your'e already one.";
	public static String altarEvilUse = "<i>Tendrils of power reach into your very soul and forever change you, for better or worse?";

	public static String altarGoodInfected = "<i>You touch it and warm energy flows through you.";
	public static String altarGoodHealthy = "<i>It can probably cure curses, but you feel well right now.";
	public static String altarGoodUse = "<i>Energy rushes through you and you feel warm inside.";

	static
	{
		// Thank you for helping me with the messages:
		// * derbycar379
		// * karibu6
		// * cooldude8909
		
		// TODO ADD MORE!
		/*thirstMessages.add("You are a bit thirsty...");
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
		thirstStrongMessages.add("You fear for your half-life!");*/
		
		// As it take 1h to turn...
		// About 20-30 messages would be awesome. 
		infectionMessagesProgress.add("<b>You feel a bit tired.");
		infectionMessagesProgress.add("<b>You feel dizzy. Could you have contracted something?");
		infectionMessagesProgress.add("<b>You feel sick. It will probably get better in a while.");
		infectionMessagesProgress.add("<b>You feel ill. Your heart beats harder than normally."); 
		infectionMessagesProgress.add("<b>You feel cold in a peculiar way.");
		infectionMessagesProgress.add("<b>You wonder why the sun hurts your eyes so much.");
		infectionMessagesProgress.add("<b>Everything sounds to loud. You get a headache.");
		infectionMessagesProgress.add("<b>Something tells you this is not a normal sickness.");
		infectionMessagesProgress.add("<b>You are thirsty but water does not seem to help.");
		infectionMessagesProgress.add("<b>You find yourself thinking about blood.");
		infectionMessagesProgress.add("<b>You wonder what blood would taste like...");
		infectionMessagesProgress.add("<b>You wonder whats wrong. Why are you obsessed with blood?");
		infectionMessagesProgress.add("<b>Your teeth really hurt. It feels like they are growing.");
		infectionMessagesProgress.add("<b>Your teeth starts to bleed and you keep swallowing the blood.");
		infectionMessagesProgress.add("<b>You vomit and feel awful... but stronger than before.");
		infectionMessagesProgress.add("<b>When people walk by you really feel like biting them...");
		infectionMessagesProgress.add("<b>You get this impulse to kill one of your friends.");
		infectionMessagesProgress.add("<b>You cry and wonder if you should ask for help.");
		infectionMessagesProgress.add("<b>Your chest starts to hurt enormously.");
		infectionMessagesProgress.add("<b>It feels your blood is dark grease and needles.");
		infectionMessagesProgress.add("<b>The pain is unbearable. You must be dying.");
		infectionMessagesProgress.add("<b>It feels like your body is dying but your soul is trapped.");
		infectionMessagesProgress.add("<b>You almost can't breathe and you are freezing.");
		infectionMessagesProgress.add("<b>Your heart is barely beating...");
		
		// TODO ADD MORE
		// About 10-20 messages would be awesome.
		infectionBreadHintMessages.add("<i>Eating some bread might do you good.");
		infectionBreadHintMessages.add("<i>You see a faint image... its a loaf of bread...");
		infectionBreadHintMessages.add("<i>Bread would taste very good right now...");
		infectionBreadHintMessages.add("<i>You think of mother's fresh homeade bread...");
		infectionBreadHintMessages.add("<i>The thought of bread makes you feel warm inside...");
		infectionBreadHintMessages.add("<i>Maybe you should see the local farmer...");
		infectionBreadHintMessages.add("<i>Bread...Bread...Bread...");
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static <T> List<T> list(T... items)
	{
		return Arrays.asList(items);
	}
	
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> map(Class<K> classOfK, Class<V> classOfV, Object... objects)
	{
		Map<K, V> ret = new HashMap<K, V>();
		
		Iterator<Object> iter = Arrays.asList(objects).iterator();
		while (iter.hasNext())
		{
			K key = (K) iter.next();
			V value = (V) iter.next();
			ret.put(key, value);
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// Persistance
	// -------------------------------------------- //
	/*
	private static transient Lang i = new Lang();
	public static void load()
	{
		P.p.one.loadOrSaveDefault(i, Lang.class);
	}
	public static void save()
	{
		P.p.one.save(i, "lang");
	}*/
}
