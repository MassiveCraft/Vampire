package com.massivecraft.vampire;

import com.massivecraft.vampire.keyboard.BloodlustToggle;
import com.massivecraft.vampire.keyboard.Shriek;

public class SpoutFeatures
{
	public static void setup()
	{
		// Register Key Bindings
		BloodlustToggle.get().register();
		Shriek.get().register();
	}
}
