package com.massivecraft.vampire.cmd;

import com.massivecraft.vampire.zcore.CommandVisibility;

public class CmdAccept extends VCommand
{
	
	public CmdAccept()
	{
		aliases.add("accept");
		
		this.setHelpShort("accept infection from someone");
		
		this.visibility = CommandVisibility.INVISIBLE;
		
		senderMustBePlayer = true;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform()
	{
		vme.acceptInfection();
	}
}
