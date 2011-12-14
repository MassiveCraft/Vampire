package com.massivecraft.vampire.cmd;

import com.massivecraft.mcore1.cmd.req.ReqHasPerm;
import com.massivecraft.vampire.*;
import com.massivecraft.vampire.cmd.req.ReqIsVampire;
import com.massivecraft.vampire.config.Conf;
import com.massivecraft.vampire.config.Lang;

public class CmdIntend extends VCommand
{
	
	public CmdIntend()
	{
		this.addAliases("intend", "intent");

		optionalArgs.put("bool", "flip");
		
		this.setDesc("intend to infect others?");
		
		this.setDescPermission(Permission.COMMAND_INTEND.node);
		this.addRequirements(new ReqHasPerm(Permission.COMMAND_INTEND.node));
		this.addRequirements(ReqIsVampire.getInstance());
	}
	
	@Override
	public void perform()
	{
		Boolean intend = this.argAs(0, Boolean.class, ! this.vme().isIntendingToInfect());
		if (intend == null) return;
			
		this.vme().setIntendingToInfect(intend);
		
		this.msg(intend ? Lang.intentOnMessage : Lang.intentOffMessage);
		this.msg("<h>%.1f%% <p>risk to infect for each attack", this.vme().infectionGetRiskToInfectOther() * 100d);
		this.msg("<h>%.1f%% <p>damage dealt", this.vme().getDamageDealtFactor() * 100d);
		this.msg("<h>%.1f%% <p>damage received", this.vme().getDamageReceivedFactor() * 100d);
		this.msg("<h>%.1f <p>hearts of damage received from wood", Conf.damageReceivedWood * 0.5d);
	}
}
