package com.massivecraft.vampire.keyboard;

import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.massivecraft.vampire.Permission;
import com.massivecraft.vampire.VPlayer;

public class Shriek extends VampireKeyBinding 
{
	@Override
	public void pressed(KeyBindingEvent event, SpoutPlayer splayer, VPlayer vplayer)
	{
		if ( ! Permission.SHRIEK.has(splayer, true)) return;
		vplayer.fxShriek();
	}

	// The Single Instance
	private Shriek()
	{
		this.defaultKey = Keyboard.KEY_X;
	}
	private static Shriek instance = new Shriek();
	public static Shriek get() { return instance; } 
}
