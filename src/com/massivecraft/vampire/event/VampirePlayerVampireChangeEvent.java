package com.massivecraft.vampire.event;

import org.bukkit.event.HandlerList;

import com.massivecraft.vampire.VPlayer;

public class VampirePlayerVampireChangeEvent extends CancellableVampireEvent
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	protected final boolean vampire;
	public boolean isVampire() { return this.vampire; }
	
	protected final VPlayer vplayer;
	public VPlayer getVplayer() { return this.vplayer; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public VampirePlayerVampireChangeEvent(boolean vampire, VPlayer vplayer)
	{
		this.vampire = vampire;
		this.vplayer = vplayer;
	}
}
