package com.massivecraft.vampire.accumulator;

import lombok.Getter;
import lombok.Setter;

import com.massivecraft.mcore.util.MUtil;

public abstract class Accumulator
{
	protected abstract int real();
	protected abstract void real(int val);
	
	@Getter @Setter protected Integer min = null;
	
	@Getter @Setter protected Integer max = null;
	
	protected double diff = 0;
	protected void update()
	{
		int delta = (int)Math.floor(this.diff);
		if (delta == 0) return;
		this.diff -= delta;
		int target = MUtil.limitNumber(this.real() + delta, min, max);
		this.real(target);
	}
	
	public double get()
	{
		return this.real() + this.diff;
	}
	
	public void set(double val)
	{
		this.diff = val % 1D;
		int target = MUtil.limitNumber((int) (val - this.diff), min, max);
		this.real(target);
	}
	
	public double add(double val)
	{
		double before = this.get();
		this.diff += val;
		this.update();
		double after = this.get();
		return after - before;
	}
}
