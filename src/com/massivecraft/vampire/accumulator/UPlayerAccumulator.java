package com.massivecraft.vampire.accumulator;

import com.massivecraft.vampire.entity.UPlayer;

public abstract class UPlayerAccumulator extends Accumulator
{
	protected UPlayer uplayer;
	public UPlayerAccumulator(UPlayer uplayer) { this.uplayer = uplayer; }
}
