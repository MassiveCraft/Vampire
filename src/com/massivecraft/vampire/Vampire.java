package com.massivecraft.vampire;

import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.vampire.cmd.CmdVampire;
import com.massivecraft.vampire.entity.MConfColl;
import com.massivecraft.vampire.entity.MLangColl;
import com.massivecraft.vampire.entity.UPlayerColl;

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

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void onEnableInner()
	{
		// Activate
		this.activate(
			// Coll
			MConfColl.class,
			MLangColl.class,
			UPlayerColl.class,
		
			// Tasks
			TheTask.class,
			
			// Listeners
			ListenerMain.class,
			
			// Command
			CmdVampire.class
		);
	}
	
}