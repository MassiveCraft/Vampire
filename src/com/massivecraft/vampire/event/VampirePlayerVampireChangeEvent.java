package com.massivecraft.vampire.event;

import org.bukkit.event.HandlerList;

import com.massivecraft.massivecore.event.EventMassiveCore;
import com.massivecraft.vampire.entity.UPlayer;

public class VampirePlayerVampireChangeEvent extends EventMassiveCore
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
	
	protected final UPlayer uplayer;
	public UPlayer getUplayer() { return this.uplayer; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public VampirePlayerVampireChangeEvent(boolean vampire, UPlayer uplayer)
	{
		this.vampire = vampire;
		this.uplayer = uplayer;
	}
	
}
