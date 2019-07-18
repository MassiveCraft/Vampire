package com.massivecraft.vampire;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.vampire.cmd.CmdVampire;
import com.massivecraft.vampire.entity.MConf;
import com.massivecraft.vampire.entity.MConfColl;
import com.massivecraft.vampire.entity.MLangColl;
import com.massivecraft.vampire.entity.UPlayerColl;
import com.massivecraft.vampire.json.MConfDeserializer;
import com.massivecraft.vampire.json.MConfSerializer;
import org.bukkit.Bukkit;

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

		// Version Synchronization
		this.checkVersionSynchronization();

		// Create Gson
		if (result) {
			GsonBuilder gsonb = new GsonBuilder();
			gsonb.registerTypeAdapter(MConf.class, new MConfSerializer());
			gsonb.registerTypeAdapter(MConf.class, new MConfDeserializer());
			this.setGson(gsonb.create());
		}

		// Listener
		Bukkit.getPluginManager().registerEvents(this, this);

		return true;
	}
}