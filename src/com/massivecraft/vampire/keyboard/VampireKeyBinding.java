package com.massivecraft.vampire.keyboard;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;
import org.getspout.spoutapi.keyboard.Keyboard;

import com.massivecraft.vampire.P;

public class VampireKeyBinding implements BindingExecutionDelegate 
{
	// -------------------------------------------- //
	// INTERFACE IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override public void keyPressed(KeyBindingEvent event){}
	@Override public void keyReleased(KeyBindingEvent event){}
	
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
	
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
	public void register()
	{
		if (registered) return;
		SpoutManager.getKeyBindingManager().registerBinding(this.id(), this.defaultKey(), this.description(), this, P.p);
	}
	 
}
