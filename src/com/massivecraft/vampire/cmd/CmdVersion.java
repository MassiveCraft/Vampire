package com.massivecraft.vampire.cmd;

import com.massivecraft.vampire.P;
import com.massivecraft.vampire.Permission;
import com.massivecraft.vampire.zcore.CommandVisibility;

public class CmdVersion extends VCommand
{
	
	public CmdVersion()
	{
		aliases.add("version");
		
		helpShort = "display current version";
		
		this.visibility = CommandVisibility.SECRET;
		
		permission = Permission.COMMAND_VERSION.node;
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform()
	{
		this.msg(p.txt.parse("<i>You are running "+P.p.getDescription().getFullName()));
	}
}
