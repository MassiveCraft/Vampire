package com.massivecraft.vampire.keyboard;

import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.massivecraft.vampire.VPlayer;

public class BloodlustOn extends VampireKeyBinding 
{
	@Override
	public void pressed(KeyBindingEvent event, SpoutPlayer splayer, VPlayer vplayer)
	{
		vplayer.bloodlust(true);
	}

	// The Single Instance
	private BloodlustOn()
	{
		this.defaultKey = Keyboard.KEY_X;
	}
	private static BloodlustOn instance = new BloodlustOn();
	public static BloodlustOn get() { return instance; } 
}
