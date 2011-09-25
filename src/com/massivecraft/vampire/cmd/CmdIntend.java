package com.massivecraft.vampire.cmd;

import com.massivecraft.vampire.*;
import com.massivecraft.vampire.config.Conf;
import com.massivecraft.vampire.config.Lang;

public class CmdIntend extends VCommand {
	
	public CmdIntend() {
		aliases.add("intend");
		aliases.add("intent");

		optionalArgs.put("bool", "flipp");
		
		helpShort = "Intend to infect others?";
		
		permission = Permission.COMMAND_INTEND.node;
		senderMustBePlayer = true;
		senderMustBeVampire = true;
	}
	
	@Override
	public void perform()
	{
		boolean indend = this.argAsBool(0, ! this.vme.isIntendingToInfect());
		vme.setIntendingToInfect(indend);
		
		this.msg(p.txt.parse(indend ? Lang.intentOnMessage : Lang.intentOffMessage));
		this.msg(p.txt.parse("<h>%.1f%% <p>risk to infect for each attack", vme.infectionGetRiskToInfectOther() * 100d));
		this.msg(p.txt.parse("<h>%.1f%% <p>damage dealt", vme.getDamageDealtFactor() * 100d));
		this.msg(p.txt.parse("<h>%.1f%% <p>damage received", vme.getDamageReceivedFactor() * 100d));
		this.msg(p.txt.parse("<h>%.1f%% <p>damage received from wood", Conf.damageReceivedWoodFactor * 100d));
	}
}
