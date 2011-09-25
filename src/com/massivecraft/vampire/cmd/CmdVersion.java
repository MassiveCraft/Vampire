package com.massivecraft.vampire.cmd;

import com.massivecraft.vampire.P;

public class CmdVersion extends VCommand {
	
	public CmdVersion() {
		aliases.add("version");
		
		helpShort = "display current version";
		
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform()
	{
		this.msg(p.txt.parse("<i>You are running "+P.p.getDescription().getFullName()));
	}
}
