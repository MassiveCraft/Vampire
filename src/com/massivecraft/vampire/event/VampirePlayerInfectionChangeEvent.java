package com.massivecraft.vampire.event;

import lombok.Getter;
import lombok.Setter;

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
	
	@Getter @Setter protected double infection;
	@Getter protected final VPlayer vplayer;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public VampirePlayerInfectionChangeEvent(double infection, VPlayer vplayer)
	{
		this.infection = infection;
		this.vplayer = vplayer;
	}
}
