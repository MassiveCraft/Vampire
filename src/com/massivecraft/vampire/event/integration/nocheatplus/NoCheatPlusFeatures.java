package com.massivecraft.vampire.event.integration.nocheatplus;

import com.massivecraft.mcore4.integration.IntegrationFeaturesAbstract;
import com.massivecraft.vampire.P;

public class NoCheatPlusFeatures extends IntegrationFeaturesAbstract 
{
	@Override
	public void activate()
	{
		new NoCheatPlusHook(P.p);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private NoCheatPlusFeatures() {}
	private static NoCheatPlusFeatures instance = new NoCheatPlusFeatures();
	public static NoCheatPlusFeatures get() { return instance; }
}
