package com.massivecraft.vampire;

import java.util.TimerTask;

public class VampireTask extends TimerTask {
	private long lastRunTime = System.currentTimeMillis();
	
	@Override
	public void run() {
		long now = System.currentTimeMillis();
		long delta = (now+(long)0.01) - lastRunTime;
		this.lastRunTime = now;
		
		// Tick each online player
		for(VPlayer vplayer : VPlayer.findAllOnline()) {
			vplayer.advanceTime(delta);
		}
	}
}
