package com.massivecraft.vampire.integration.spout;

import java.util.Set;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.massivecraft.mcore4.util.MUtil;
import com.massivecraft.vampire.P;
import com.massivecraft.vampire.VPlayer;

public abstract class VampireKeyBinding implements BindingExecutionDelegate 
{
	// -------------------------------------------- //
	// INTERFACE IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override public void keyPressed(KeyBindingEvent event)
	{
		if ( ! this.allowed(event)) return;
		SpoutPlayer splayer = event.getPlayer();
		VPlayer vplayer = VPlayer.get(splayer);
		this.pressed(event, splayer, vplayer);
	}
	
	@Override public void keyReleased(KeyBindingEvent event)
	{
		if ( ! this.allowed(event)) return;
		SpoutPlayer splayer = event.getPlayer();
		VPlayer vplayer = VPlayer.get(splayer);
		this.released(event, splayer, vplayer);
	}
	
	// -------------------------------------------- //
	// METHODS TO OVERRIDE
	// -------------------------------------------- //
	
	public void pressed(KeyBindingEvent event, SpoutPlayer splayer, VPlayer vplayer){};
	public void released(KeyBindingEvent event, SpoutPlayer splayer, VPlayer vplayer){};
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// FIELD: id - unique id that represents your binding. IT MUST BE UNIQUE!
	protected String id = null;
	public String id() { return this.id != null ? this.id : "Vampire"+this.getClass().getSimpleName(); }
	
	// FIELD: defaultKey - for this binding.
	protected Keyboard defaultKey = null;
	public Keyboard defaultKey() { return this.defaultKey; }
	
	// FIELD: description - of this key binding, that players will see.
	protected String description = null;
	public String description() { return this.description != null ? this.description : this.getClass().getSimpleName(); }
	
	// FIELD: registered - is this key binding registered already?
	protected boolean registered = false;
	public boolean registered() { return this.registered; }
	
	// FIELD: screenWhitelist - Only these are allowed if non-null.
	protected Set<ScreenType> screenWhitelist = MUtil.set(ScreenType.GAME_SCREEN);
	public Set<ScreenType> screenWhitelist() { return this.screenWhitelist; }
	
	// FIELD: screenBlacklist - These are forbidden if non-null.
	protected Set<ScreenType> screenBlacklist = null;
	public Set<ScreenType> screenBlacklist() { return this.screenBlacklist; }
	
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
	public void register()
	{
		if (registered) return;
		SpoutManager.getKeyBindingManager().registerBinding(this.id(), this.defaultKey(), this.description(), this, P.p);
	}
	
	// -------------------------------------------- //
	// ALLOWED DUE TO SCREEN? 
	// -------------------------------------------- //
	
	public boolean allowed (KeyBindingEvent event)
	{
		SpoutPlayer splayer = event.getPlayer();
		ScreenType screen = splayer.getActiveScreen();
		
		if (this.screenWhitelist != null && ! this.screenWhitelist.contains(screen)) return false;
		if (this.screenBlacklist != null && this.screenBlacklist.contains(screen)) return false;
		return true;
	}
	 
}
