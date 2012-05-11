package com.massivecraft.vampire.cmd;

import com.massivecraft.vampire.cmdreq.ReqIsVampire;

public abstract class CmdModeAbstract extends VCommand
{	
	public CmdModeAbstract()
	{
		this.addOptionalArg("bool", "flip");
		
		this.addRequirements(ReqIsVampire.get());
	}
	
	@Override
	public void perform()
	{
		Boolean val = this.argAs(0, Boolean.class, ! this.get());
		if (val == null) return;
			
		this.set(val);
		
		/*this.msg(intend ? Lang.intentOnMessage : Lang.intentOffMessage);
		this.msg("<h>%.1f%% <p>risk to infect for each attack", vme.infectionGetRiskToInfectOther() * 100d);
		this.msg("<h>%.1f%% <p>damage dealt", vme.getDamageDealtFactor() * 100d);
		this.msg("<h>%.1f%% <p>damage received", vme.getDamageReceivedFactor() * 100d);
		this.msg("<h>%.1f <p>hearts of damage received from wood", Conf.damageReceivedWood * 0.5d);*/
	}
	
	protected abstract void set(boolean val);
	protected abstract boolean get();
}
