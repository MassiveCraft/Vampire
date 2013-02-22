package com.massivecraft.vampire.integration.spout;

import com.massivecraft.mcore.integration.IntegrationFeaturesAbstract;

public class SpoutFeatures extends IntegrationFeaturesAbstract 
{
	@Override
	public void activate()
	{
		// Register Key Bindings
		BloodlustToggle.get().register();
		Shriek.get().register();
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private SpoutFeatures() { super("Spout"); }
	private static SpoutFeatures i = new SpoutFeatures();
	public static SpoutFeatures get() { return i; }
}
