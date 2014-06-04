package com.massivecraft.vampire.event;

import org.bukkit.event.HandlerList;

import com.massivecraft.massivecore.event.EventMassiveCore;
import com.massivecraft.vampire.entity.UPlayer;

public class EventVampirePlayerInfectionChange extends EventMassiveCore
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
	
	protected final UPlayer uplayer;
	public UPlayer getUplayer() { return this.uplayer; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventVampirePlayerInfectionChange(double infection, UPlayer uplayer)
	{
		this.infection = infection;
		this.uplayer = uplayer;
	}
	
}
