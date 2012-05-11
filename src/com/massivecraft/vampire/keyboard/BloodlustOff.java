package com.massivecraft.vampire.keyboard;

import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.massivecraft.vampire.VPlayer;

public class BloodlustOff extends VampireKeyBinding 
{
	@Override
	public void pressed(KeyBindingEvent event, SpoutPlayer splayer, VPlayer vplayer)
	{
		vplayer.bloodlust(false);
	}

	// The Single Instance
	private BloodlustOff()
	{
		this.defaultKey = Keyboard.KEY_Z;
	}
	private static BloodlustOff instance = new BloodlustOff();
	public static BloodlustOff get() { return instance; } 
}
