package com.massivecraft.vampire.keyboard;

import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.keyboard.Keyboard;

import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.VPlayers;

public class BloodlustToggle extends VampireKeyBinding 
{
	@Override
	public void keyPressed(KeyBindingEvent event)
	{
		VPlayer vplayer = VPlayers.i.get(event.getPlayer());
		vplayer.bloodlust( ! vplayer.bloodlust());
	}
	
	// The Single Instance
	private BloodlustToggle()
	{
		this.defaultKey = Keyboard.KEY_V;
	}
	private static BloodlustToggle instance = new BloodlustToggle();
	public static BloodlustToggle get() { return instance; } 
}
