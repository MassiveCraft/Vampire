package com.massivecraft.vampire;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.AspectColl;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.vampire.cmd.CmdVampire;
import com.massivecraft.vampire.entity.MConfColl;
import com.massivecraft.vampire.entity.MLangColl;
import com.massivecraft.vampire.entity.UConfColls;
import com.massivecraft.vampire.entity.UPlayerColls;

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
	
	// Commands
	public CmdVampire cmdBase;

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void onEnable()
	{
		if ( ! preEnable()) return;
		
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
		
		// Database
		EngineIdUpdate.get().activate();
		MConfColl.get().init();
		MLangColl.get().init();
		UConfColls.get().init();
		UPlayerColls.get().init();
		
		// Commands
		this.cmdBase = new CmdVampire();
		this.cmdBase.register();
		
		// Tasks
		TheTask.get().activate(this);
	
		// Listeners
		ListenerMain.get().activate();
		
		postEnable();
	}
	
}