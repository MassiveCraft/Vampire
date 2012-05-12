package com.massivecraft.vampire.event;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.massivecraft.vampire.P;

public class SpoutCraftAuthenticationEvent extends VampireEvent
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	private static final HandlerList handlers = new HandlerList();
	public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// FIELD: splayer - The SpoutPlayer in question
	protected final SpoutPlayer splayer;
	public SpoutPlayer splayer() { return this.splayer; }
	
	// FIELD: success - Was the authentication successful?
	protected Boolean successful;
	public Boolean successful() { return this.successful; }
	
	public SpoutCraftAuthenticationEvent(SpoutPlayer splayer)
	{
		this.splayer = splayer;
		this.successful = null;
		this.startPolling();
	}
	
	// -------------------------------------------- //
	// THE POLLING LOGIC
	// -------------------------------------------- //
	
	protected final static int pollEachXTicks = 3;
	protected int pollsLeft = 5 * 20 / pollEachXTicks;
	protected int taskId;
	protected void startPolling()
	{
		// This gives ME JavaScript feelings :P
		final SpoutCraftAuthenticationEvent ME = this;
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(P.p, new Runnable()
		{
			@Override
			public void run()
			{
				pollsLeft--;
				if (ME.splayer.isSpoutCraftEnabled())
				{
					ME.successful = true;
				}
				else if (ME.pollsLeft == 0)
				{
					ME.successful = false;
				}
				
				if (ME.successful != null)
				{
					Bukkit.getScheduler().cancelTask(ME.taskId);
					ME.run();
					return;
				}
			}
		}, pollEachXTicks, pollEachXTicks);
	}
}
