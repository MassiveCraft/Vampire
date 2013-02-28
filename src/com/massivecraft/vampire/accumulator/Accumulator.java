package com.massivecraft.vampire.accumulator;

import com.massivecraft.mcore.util.MUtil;

public abstract class Accumulator
{
	protected abstract int real();
	protected abstract void real(int val);
	
	protected Integer min = null;
	public Integer getMin() { return this.min; }
	public void setMin(Integer min) { this.min = min; }
	
	protected Integer max = null;
	public Integer getMax() { return this.max; }
	public void setMax(Integer max) { this.max = max; }
	
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
