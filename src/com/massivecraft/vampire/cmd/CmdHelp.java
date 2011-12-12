package com.massivecraft.vampire.cmd;

import java.util.ArrayList;

import com.massivecraft.mcore1.cmd.MCommand;

public class CmdHelp extends VCommand
{
	public CmdHelp()
	{
		super();
		this.addAliases("?", "h", "help");
		this.setDesc("");
		this.addOptionalArg("page","1");
	}
	
	@Override
	public void perform()
	{
		if (this.commandChain.size() == 0) return;
		MCommand pcmd = this.commandChain.get(this.commandChain.size()-1);
		
		ArrayList<String> lines = new ArrayList<String>();
		
		//lines.addAll(pcmd.helpLong);
		
		for(MCommand scmd : pcmd.getSubCommands())
		{
			if (scmd.visibleTo(sender))
			{
				lines.add(scmd.getUseageTemplate(this.commandChain, true));
			}
		}
		
		sendMessage(p.txt.getPage(lines, this.argAs(0, Integer.class, 1), "Help for command \""+pcmd.getAliases().get(0)+"\""));
	}
}
