package com.massivecraft.vampire;

import java.util.List;

import com.massivecraft.mcore4.SimpleConfig;
import com.massivecraft.mcore4.util.MUtil;

public class Lang extends SimpleConfig
{	
	public static String consolePlayerArgRequired = "<b>You must specify player from console.";
	
	public static String xIsAlreadyVamp = "<b>%s is already a vampire.";
	public static String onlyVampsCanX = "<b>Only vampires can %s.";
	
	public static String xIsY = "<k>%s: <v>%s";
	public static String on = "<lime>ON  ";
	public static String off = "<rose>OFF";
	public static String boolIsY(String boolName, boolean val) { return String.format(xIsY, boolName, val ? on : off); }
	public static String quotaIsPercent(String quotaName, double val) { return String.format(xIsY, quotaName, percent(val)); }
	public static String percent(double quota) { return String.format("%.1f%%", quota*100); }
	
	public static String vampireTrue = "<b><strong>You are now a vampire!";
	public static String vampireFalse = "<g><strong>You were cured from vampirism!";
	
	public static String shriekWait = "<i>You must wait <h>%ds <i>before you can shriek again.";
	
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
		"<i>You may want use an altar of light.",
		"<i>Perhaps you should visit a temple of light?",
		"<i>Only bright light may purge you of this darkness",
		"<i>You want to touch a lapis lazuli block"
	);
	
	public static String foodCantEat = "<b>Vampires can't eat %s.";
	
	public static String holyWaterCommon = "<h>%s <i>throws holy water at you.";
	public static String holyWaterVampire = "<b>The sparkles of light burn you.";
	public static String holyWaterInfected = "<i>The sparkles of light purge you of all darkness.";
	public static String holyWaterHealthy = "<i>You feel cleansed by the sparkles of light.";

	public static String altarIncomplete = "<i>An incomplete %s. Missing blocks:";
	public static String altarResourceSuccess = "<i>You use these items on the altar:";
	public static String altarResourceFail = "<i>To use it you need to collect these ingredients:";
	
	public static String altarDarkName = "altar of darkness";
	public static String altarDarkDesc = "<i>This altar looks dark and evil.";
	public static String altarDarkCommon = "<i>You meditate and let darkness surround you...";
	public static String altarDarkVampire = "<i>...you feel as one with the darkness.";
	public static String altarDarkInfected = "<i>...some of the darkness already dwells within you.";
	public static String altarDarkHealthy = "<i>...an intense darkness purge you of light.";
	
	public static String altarLightName = "altar of light";
	public static String altarLightDesc = "<i>This altar looks bright and good.";
	
	public static String altarLightWaterResourceSuccess = "<i>You mix these items on the altar:";
	public static String altarLightWaterResourceFail = "<i>You need these items to create holy water:";
	public static String altarLightWaterResult = "<i>...the water in the bottle starts to sparkle.";
	public static String altarLightCommon = "<i>You meditate and feel at peace...";
	public static String altarLightHealthy = "<i>...you feel as one with the light.";
	public static String altarLightInfected = "<i>...a warm energy flows through you.";
	public static String altarLightVampire = "<i>...a bright light purge you of all darkness.";
	
	// -------------------------------------------- //
	// Persistance
	// -------------------------------------------- //
	public static transient Lang i = new Lang();
	private Lang()
	{
		super(P.p, "lang");
	}
}
