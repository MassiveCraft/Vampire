package com.massivecraft.vampire.integration.nocheatplus;

import com.massivecraft.mcore5.integration.IntegrationFeaturesAbstract;
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
	
	private NoCheatPlusFeatures() { super("NoCheatPlus"); }
	private static NoCheatPlusFeatures i = new NoCheatPlusFeatures();
	public static NoCheatPlusFeatures get() { return i; }
}
