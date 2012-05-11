package com.massivecraft.vampire.keyboard;

import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.keyboard.Keyboard;
import com.massivecraft.vampire.VPlayers;

public class BloodlustOn extends VampireKeyBinding 
{
	@Override
	public void keyPressed(KeyBindingEvent event)
	{
		VPlayers.i.get(event.getPlayer()).bloodlust(true);
	}

	// The Single Instance
	private BloodlustOn()
	{
		this.defaultKey = Keyboard.KEY_X;
	}
	private static BloodlustOn instance = new BloodlustOn();
	public static BloodlustOn get() { return instance; } 
}
