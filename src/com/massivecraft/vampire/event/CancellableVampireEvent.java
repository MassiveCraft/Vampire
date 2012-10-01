package com.massivecraft.vampire.event;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.event.Cancellable;

public abstract class CancellableVampireEvent extends VampireEvent implements Cancellable
{
	// FIELD: cancelled
	@Getter @Setter private boolean cancelled = false;
}
