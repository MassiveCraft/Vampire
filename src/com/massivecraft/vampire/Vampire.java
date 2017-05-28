package com.massivecraft.vampire;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.AspectColl;
import com.massivecraft.massivecore.MassivePlugin;

public class Vampire extends MassivePlugin 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static Vampire i;
	public static Vampire get() { return i; }
	public Vampire() { Vampire.i = this; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Aspects
	public Aspect playerAspect;
	public Aspect configAspect;

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void onEnableInner()
	{
		// Aspects
		this.playerAspect = AspectColl.get().get(Const.ASPECT_PLAYER, true);
		this.playerAspect.register();
		this.playerAspect.setDesc(
			"<i>Everything player related:", 
			"<i>Is the player a vampire or not?",
			"<i>What was the infection reason?",
			"<i>Check <h>"+Const.ASPECT_CONF+" <i>for rules and balancing."
		);
		
		this.configAspect = AspectColl.get().get(Const.ASPECT_CONF, true);
		this.configAspect.register();
		this.configAspect.setDesc(
			"<i>Config options for balancing:", 
			"<i>What is the splash potion radius for holy water?",
			"<i>What items are considered wooden stakes?",
			"<i>Check <h>"+Const.ASPECT_PLAYER+" <i>for player state."
		);
		
		// Activate
		this.activateAuto();
	}
	
}