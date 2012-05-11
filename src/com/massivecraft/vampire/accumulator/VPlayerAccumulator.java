package com.massivecraft.vampire.accumulator;

import com.massivecraft.vampire.VPlayer;

public abstract class VPlayerAccumulator extends Accumulator
{
	protected VPlayer vplayer;
	public VPlayerAccumulator(VPlayer vplayer) { this.vplayer = vplayer; }
}
