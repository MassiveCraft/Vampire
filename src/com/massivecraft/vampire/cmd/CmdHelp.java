package com.massivecraft.vampire.cmd;

import java.util.ArrayList;

import com.massivecraft.vampire.P;
import com.massivecraft.vampire.zcore.MCommand;

public class CmdHelp extends MCommand<P>
{
	public CmdHelp()
	{
		super(P.p);
		this.aliases.add("h");
		this.aliases.add("help");
		
		this.helpShort = "";
		
		this.optionalArgs.put("page","1");
	}
	
	@Override
	public void perform()
	{
		if (this.commandChain.size() == 0) return;
		MCommand<?> pcmd = this.commandChain.get(this.commandChain.size()-1);
		
		ArrayList<String> lines = new ArrayList<String>();
		
		lines.addAll(pcmd.helpLong);
		
		for(MCommand<?> scmd : pcmd.subCommands)
		{
			if (scmd.validSenderPermissions(sender, false))
			{
				lines.add(scmd.getUseageTemplate(this.commandChain, true));
			}
		}
		
		msg(p.txt.getPage(lines, this.argAsInt(0, 1), "Help for command \""+pcmd.aliases.get(0)+"\""));
	}
}
