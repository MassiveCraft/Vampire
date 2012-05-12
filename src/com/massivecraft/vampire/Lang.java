package com.massivecraft.vampire;

import java.util.List;

import com.massivecraft.mcore3.util.MUtil;

public class Lang
{
	//public static String intentOnMessage  = "<i>You now <h>do intend <i>to infect others in combat.";
	//public static String intentOffMessage = "<i>You now <h>don't intend <i>to infect others in combat.";
	
	public static String consolePlayerArgRequired = "<b>You must specify player from console.";
	public static String noSpoutWarnHuman = "";
	public static String noSpoutWarnVamp = "<b>Use SpoutCraft for improved vampire movement.\n<aqua>http://dev.bukkit.org/client-mods/spoutcraft/";
	public static String noSpoutWarnBloodlust = "<b>Use SpoutCraft! Bloodlust will then grant you faster movement.<aqua>http://dev.bukkit.org/client-mods/spoutcraft/";
	
	public static String xIsAlreadyVamp = "<b>%s is already a vampire.";
	public static String onlyVampsCanX = "<b>Only vampires can %s.";
	
	public static String xIsY = "<k>%s: <v>%s";
	public static String on = "<lime>ON";
	public static String off = "<lime>OFF";
	public static String boolIsY(String boolName, boolean val) { return String.format(xIsY, boolName, val ? on : off); }

	public static String vampireTrue = "<b>Your heart stops. You don't breathe anymore. You are now a VAMPIRE!";
	public static String vampireFalse = "<g>You have been cured and is once again healthy and alive.<b> But will you miss the taste of blood on your lips? When you sleep, will you taste the salt and copper flowing over your tongue? Go, mortal. Bask in your precious sunlight.";
	
	public static String tradeSelf = "<i>You drink some of your own blood. Forever alone :'/";
	public static String tradeNotClose = "<b>You must stand close to <h>%s <i>for this to work.";
	public static String tradeOfferOut = "<i>You offer <h>%.1f<i> of your blood to <h>%s<i>.";
	public static String tradeOfferIn = "<h>%s<i> offers you <h>%.1f <i>of their blood.";
	public static String tradeAcceptHelp = "<i>Type %s<i> to accept.";
	public static String tradeAcceptNone = "<b>Noone offered you blood recently.";
	public static String tradeLackingOut = "<b>You don't have enough blood to complete the trade.";
	public static String tradeLackingIn = "<b><h>%s<b> don't have enough blood to complete the trade.";
	public static String tradeTransferOut = "<h>%s<i> drinks <h>%.1f <i>of your blood.";
	public static String tradeTransferIn = "<i>You drink <h>%.1f <i> of <h>%s's<i> blood.";
	public static String tradeSeen = "<h>%s <i>drinks blood from <h>%s <i><strong>:O<reset> <i>!";

	public static String truceBroken = "<b>You temporarily broke your truce with the monsters.";
	public static String truceRestored = "<g>Your truce with the monsters has been restored.";
	
	public static String combatWoodWarning = "<b>Ouch!!! <h>%s <b>is made of <h>wood <b>and hurt vampires a lot!";
	
	public static String infectionCured = "<g>You are now completely cured from the sickness you had.";
	
	// As it take 1h to turn...
	// About 20-30 messages would be awesome.
	public static List<String> infectionFeeling = MUtil.list(
		"<b>You feel a bit tired.",
		"<b>You feel dizzy. Could you have contracted something?",
		"<b>You feel sick. It will probably get better in a while.",
		"<b>You feel ill. Your heart beats harder than normally.",
		"<b>You feel cold in a peculiar way.",
		"<b>You wonder why the sun hurts your eyes so much.",
		"<b>Everything sounds to loud. You get a headache.",
		"<b>Something tells you this is not a normal sickness.",
		"<b>You are thirsty but water does not seem to help.",
		"<b>You find yourself thinking about blood.",
		"<b>You wonder what blood would taste like...",
		"<b>You wonder whats wrong. Why are you obsessed with blood?",
		"<b>Your teeth really hurt. It feels like they are growing.",
		"<b>Your teeth starts to bleed and you keep swallowing the blood.",
		"<b>You vomit and feel awful... but stronger than before.",
		"<b>When people walk by you really feel like biting them...",
		"<b>You get this impulse to kill one of your friends.",
		"<b>You cry and wonder if you should ask for help.",
		"<b>Your chest starts to hurt enormously.",
		"<b>It feels your blood is dark grease and needles.",
		"<b>The pain is unbearable. You must be dying.",
		"<b>It feels like your body is dying but your soul is trapped.",
		"<b>You almost can't breathe and you are freezing.",
		"<b>Your heart is barely beating..."
	);
	
	public static List<String> infectionHint = MUtil.list(
		"<i>Altar Hint 1",
		"<i>Altar Hint 2",
		"<i>Altar Hint 3"
	);
	
	public static String foodCantEat = "<b>Vampires can't eat %s.";

	public static String altarIncomplete = "<i>This is an incomplete %s. This is missing:";
	public static String altarUseIngredientsSuccess = "<i>You use these items on the altar:";
	public static String altarUseIngredientsFail = "<i>To use it you need to collect these ingredients:";
	
	public static String altarEvilName = "evil altar";
	public static String altarEvilDesc = "<i>The very sight of this altar makes you feel cold and corrupted down to your bones.";
	public static String altarEvilUse = "<i>Tendrils of power reach into your very soul and forever change you, for better or worse?";
	public static String altarEvilAlreadyInfected = "<i>You feel some of it's energy inside you...";
	public static String altarEvilAlreadyVampire = "<i>You understand this altar can turn people into vampires. But it's of no use to you as your'e already one.";
	
	public static String altarGoodName = "good altar";
	public static String altarGoodDesc = "<i>This altar looks bright and nice.";
	public static String altarGoodUse = "<i>Energy rushes through you and you feel warm inside.";
	public static String altarGoodInfected = "<i>You touch it and warm energy flows through you.";
	public static String altarGoodHealthy = "<i>It can probably cure curses, but you feel well right now.";
	
	// -------------------------------------------- //
	// Persistance
	// -------------------------------------------- //
	
	private static transient Lang i = new Lang();
	public static void load()
	{
		P.p.one.loadOrSaveDefault(i, Lang.class);
	}
	public static void save()
	{
		P.p.one.save(i, "lang");
	}
}
