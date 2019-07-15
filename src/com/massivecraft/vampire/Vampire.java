package com.massivecraft.vampire;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.vampire.cmd.CmdVampire;
import com.massivecraft.vampire.entity.MConfColl;
import com.massivecraft.vampire.entity.MLangColl;
import com.massivecraft.vampire.entity.UPlayerColl;
import com.massivecraft.vampire.util.ItemMetaInstanceCreator;
import org.bukkit.inventory.meta.ItemMeta;

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
			MLangColl.class,
			MConfColl.class,
			UPlayerColl.class,
		
			// Tasks
			TheTask.class,
			
			// Listeners
			ListenerMain.class,
			
			// Command
			CmdVampire.class
		);
	}

	// Needed for PotionMeta. I hate it.
	@Override
	public boolean onEnablePre()
	{
		boolean result = super.onEnablePre();

		if (result) {
			Gson gson = new GsonBuilder().registerTypeAdapter(ItemMeta.class, new ItemMetaInstanceCreator()).create();
			this.setGson(gson);
		}

		return true;
	}
}