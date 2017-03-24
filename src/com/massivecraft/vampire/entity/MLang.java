package com.massivecraft.vampire.entity;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

@EditorName("lang")
public class MLang extends Entity<MLang>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MLang i;
	public static MLang get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public String consolePlayerArgRequired = "<b>You must specify player from console.";
	
	public String xIsAlreadyVamp = "<b>%s <b>is already a vampire.";
	public String onlyVampsCanX = "<b>Only vampires can %s.";
	
	public String xIsY = "<k>%s: <v>%s";
	public String on = "<lime>ON  ";
	public String off = "<rose>OFF";
	public String boolIsY(String boolName, boolean val) { return String.format(xIsY, boolName, val ? on : off); }
	public String quotaIsPercent(String quotaName, double val) { return String.format(xIsY, quotaName, percent(val)); }
	public String percent(double quota) { return String.format("%.1f%%", quota*100); }
	
	public String vampireTrue = "<b><strong>You are now a vampire!";
	public String vampireFalse = "<g><strong>You were cured from vampirism!";
	
	public String shriekWait = "<i>You must wait <h>%ds <i>before you can shriek again.";
	
	public String tradeSelf = "<i>You drink some of your own blood. Forever alone :'/";
	public String tradeNotClose = "<b>You must stand close to <h>%s <i>for this to work.";
	public String tradeOfferOut = "<i>You offer <h>%.1f<i> of your blood to <h>%s<i>.";
	public String tradeOfferIn = "<h>%s<i> offers you <h>%.1f <i>of their blood.";
	public String tradeAcceptHelp = "<i>Type %s<i> to accept.";
	public String tradeAcceptNone = "<b>Noone offered you blood recently.";
	public String tradeLackingOut = "<b>You don't have enough blood to complete the trade.";
	public String tradeLackingIn = "<b><h>%s<b> don't have enough blood to complete the trade.";
	public String tradeTransferOut = "<h>%s<i> drinks <h>%.1f <i>of your blood.";
	public String tradeTransferIn = "<i>You drink <h>%.1f <i> of <h>%s's<i> blood.";
	public String tradeSeen = "<h>%s <i>drinks blood from <h>%s <i><strong>:O<reset> <i>!";
	
	public String flaskInsufficient = "<b>You do not have the specified amount of blood.";
	public String flaskBloodlusting = "<b>You cannot drink a blood flask while bloodlusting.";
	public String flaskSuccess = "<i>You have created a blood flask.";

	public String truceBroken = "<b>You temporarily broke your truce with the monsters.";
	public String truceRestored = "<g>Your truce with the monsters has been restored.";
	
	public String combatWoodWarning = "<b>Ouch!!! <h>%s <b>is made of <h>wood <b>and hurt vampires a lot!";
	
	public String infectionCured = "<g>You are now completely cured from the sickness you had.";
	
	// As it take 1h to turn...
	// About 20-30 messages would be awesome.
	public List<String> infectionFeeling = MUtil.list(
		"<b>You feel a bit tired.",
		"<b>You feel dizzy. Could you have contracted something?",
		"<b>You feel sick. It will probably get better in a while.",
		"<b>You feel ill. Your heart beats harder than normal.",
		"<b>You feel cold in a peculiar way.",
		"<b>You wonder why the sun hurts your eyes so much.",
		"<b>Everything sounds too loud. You get a headache.",
		"<b>Something tells you this is not a normal sickness.",
		"<b>You are thirsty but water does not seem to help.",
		"<b>You find yourself thinking about blood.",
		"<b>You wonder what blood would taste like...",
		"<b>You wonder what's wrong. Why are you obsessed with blood?",
		"<b>Your teeth really hurt. It feels like they are growing.",
		"<b>Your teeth start to bleed and you keep swallowing the blood.",
		"<b>You vomit and feel awful... but stronger than before.",
		"<b>When people walk nearby, you feel like biting them...",
		"<b>You get an impulse to kill one of your friends.",
		"<b>You cry and wonder if you should ask for help.",
		"<b>Your chest starts to hurt enormously.",
		"<b>It feels your blood is dark grease and needles.",
		"<b>The pain is unbearable. You must be dying.",
		"<b>It feels like your body is dying but your soul is trapped.",
		"<b>You almost can't breathe and you are freezing.",
		"<b>Your heart is barely beating..."
	);
	
	public List<String> infectionHint = MUtil.list(
		"<i>You may want use an altar of light.",
		"<i>Perhaps you should visit a temple of light?",
		"<i>Only bright light may purge you of this darkness",
		"<i>You want to touch a lapis lazuli block"
	);
	
	public String foodCantEat = "<b>Vampires can't eat %s.";
	
	public String holyWaterCommon = "<h>%s <i>throws holy water at you.";
	public String holyWaterVampire = "<b>The sparkles of light burn you.";
	public String holyWaterInfected = "<i>The sparkles of light purge you of all darkness.";
	public String holyWaterHealthy = "<i>You feel cleansed by the sparkles of light.";

	public String altarIncomplete = "<i>An incomplete %s. Missing blocks:";
	public String altarResourceSuccess = "<i>You use these items on the altar:";
	public String altarResourceFail = "<i>To use it you need to collect these ingredients:";
	
	public String altarDarkName = "altar of darkness";
	public String altarDarkDesc = "<i>This altar looks dark and evil.";
	public String altarDarkCommon = "<i>You meditate and let darkness surround you...";
	public String altarDarkVampire = "<i>...you feel as one with the darkness.";
	public String altarDarkInfected = "<i>...some of the darkness already dwells within you.";
	public String altarDarkHealthy = "<i>...an intense darkness purge you of light.";
	
	public String altarLightName = "altar of light";
	public String altarLightDesc = "<i>This altar looks bright and good.";
	
	public String altarLightWaterResourceSuccess = "<i>You mix these items on the altar:";
	public String altarLightWaterResourceFail = "<i>You need these items to create holy water:";
	public String altarLightWaterResult = "<i>...the water in the bottle starts to sparkle.";
	public String altarLightCommon = "<i>You meditate and feel at peace...";
	public String altarLightHealthy = "<i>...you feel as one with the light.";
	public String altarLightInfected = "<i>...a warm energy flows through you.";
	public String altarLightVampire = "<i>...a bright light purge you of all darkness.";
	

}
