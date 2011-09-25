package com.massivecraft.vampire.cmd;

import com.massivecraft.vampire.*;
import com.massivecraft.vampire.config.Lang;

public class CmdIntent extends VCommand {
	
	public CmdIntent() {
		aliases.add("intend");
		aliases.add("intent");

		optionalArgs.put("bool", "flipp");
		
		helpShort = "Intend to infect others?";
		
		permission = Permission.COMMAND_INTEND.node;
		senderMustBePlayer = true;
		senderMustBeVampire = true;
	}
	
	@Override
	public void perform() {
		boolean indend = this.argAsBool(0, ! this.vme.isIntendingToInfect());
		vme.setIntendingToInfect(indend);
		
		this.msg(p.txt.parse(indend ? Lang.intentOnMessage : Lang.intentOffMessage, this.vme.infectionGetRiskToInfectOther() * 100d));
	}
}
