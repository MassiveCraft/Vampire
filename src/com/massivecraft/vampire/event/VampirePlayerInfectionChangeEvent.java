package com.massivecraft.vampire.event;

import org.bukkit.event.HandlerList;

import com.massivecraft.vampire.VPlayer;

public class VampirePlayerInfectionChangeEvent extends CancellableVampireEvent
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
	
	protected double infection;
	public double getInfection() { return this.infection; }
	public void setInfection(double infection) { this.infection = infection; }
	
	protected final VPlayer vplayer;
	public VPlayer getVPlayer() { return this.vplayer; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public VampirePlayerInfectionChangeEvent(double infection, VPlayer vplayer)
	{
		this.infection = infection;
		this.vplayer = vplayer;
	}
}
