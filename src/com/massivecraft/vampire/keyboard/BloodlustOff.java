package com.massivecraft.vampire.keyboard;

import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.keyboard.Keyboard;
import com.massivecraft.vampire.VPlayers;

public class BloodlustOff extends VampireKeyBinding 
{
	@Override
	public void keyPressed(KeyBindingEvent event)
	{
		VPlayers.i.get(event.getPlayer()).bloodlust(false);
	}

	// The Single Instance
	private BloodlustOff()
	{
		this.defaultKey = Keyboard.KEY_Z;
	}
	private static BloodlustOff instance = new BloodlustOff();
	public static BloodlustOff get() { return instance; } 
}
