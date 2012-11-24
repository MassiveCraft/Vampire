package com.massivecraft.vampire.event;

import lombok.Getter;

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
	
	@Getter protected final boolean vampire;
	@Getter protected final VPlayer vplayer;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public VampirePlayerVampireChangeEvent(boolean vampire, VPlayer vplayer)
	{
		this.vampire = vampire;
		this.vplayer = vplayer;
	}
}
