package com.massivecraft.vampire.event;

import org.bukkit.event.Cancellable;

public abstract class CancellableVampireEvent extends VampireEvent implements Cancellable
{
	// FIELD: cancelled
	private boolean cancelled = false;
	public boolean isCancelled() { return this.cancelled; }
	public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}
