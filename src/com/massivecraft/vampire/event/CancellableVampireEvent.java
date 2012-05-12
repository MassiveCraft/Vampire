package com.massivecraft.vampire.event;

import org.bukkit.event.Cancellable;

public abstract class CancellableVampireEvent extends VampireEvent implements Cancellable
{
	// FIELD: cancelled
	private boolean cancelled = false;
	@Override public boolean isCancelled() { return this.cancelled; }
	@Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
}
