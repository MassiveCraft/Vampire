package com.massivecraft.vampire.integration.spout;

import com.massivecraft.mcore5.integration.IntegrationFeaturesAbstract;

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
	
	private SpoutFeatures() {}
	private static SpoutFeatures instance = new SpoutFeatures();
	public static SpoutFeatures get() { return instance; }
}
