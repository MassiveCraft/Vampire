package com.massivecraft.vampire.cmd;

import com.massivecraft.vampire.*;
import com.massivecraft.vampire.config.Conf;
import com.massivecraft.vampire.config.Lang;

public class CmdIntend extends VCommand
{
	
	public CmdIntend()
	{
		aliases.add("intend");
		aliases.add("intent");

		optionalArgs.put("bool", "flipp");
		
		this.setHelpShort("intend to infect others?");
		
		permission = Permission.COMMAND_INTEND.node;
		senderMustBePlayer = true;
		senderMustBeVampire = true;
	}
	
	@Override
	public void perform()
	{
		boolean indend = this.argAsBool(0, ! this.vme.isIntendingToInfect());
		vme.setIntendingToInfect(indend);
		
		this.msg(indend ? Lang.intentOnMessage : Lang.intentOffMessage);
		this.msg("<h>%.1f%% <p>risk to infect for each attack", vme.infectionGetRiskToInfectOther() * 100d);
		this.msg("<h>%.1f%% <p>damage dealt", vme.getDamageDealtFactor() * 100d);
		this.msg("<h>%.1f%% <p>damage received", vme.getDamageReceivedFactor() * 100d);
		this.msg("<h>%.1f <p>hearts of damage received from wood", Conf.damageReceivedWood * 0.5d);
	}
}
