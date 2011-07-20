package com.massivecraft.vampire.commands;

import java.util.ArrayList;

import com.massivecraft.vampire.util.TextUtil;

public class VCommandHelp extends VCommand
{
	public VCommandHelp() {
		aliases.add("help");
		aliases.add("h");
		aliases.add("?");
		
		optionalParameters.add("page");		
		
		helpDescription = "Display a help page";
		
		senderMustBePlayer = false;
		senderMustBeVampire = false;
	}
	
	@Override
	public void perform()
	{
		int page = 1;
		if (parameters.size() > 0) {
			try {
				page = Integer.parseInt(parameters.get(0));
			} catch (NumberFormatException e) {
				// wasn't an integer
			}
		}
		sendMessage(TextUtil.titleize("Vampire Help ("+page+"/"+helpPages.size()+")"));
		page -= 1;
		if (page < 0 || page >= helpPages.size()) {
			sendMessage("This page does not exist");
			return;
		}
		sendMessage(helpPages.get(page));
	}
	
	//----------------------------------------------//
	// Build the help pages
	//----------------------------------------------//
	
	public static final ArrayList<ArrayList<String>> helpPages;
	
	static {
		helpPages = new ArrayList<ArrayList<String>>();
		ArrayList<String> pageLines;
		
		pageLines = new ArrayList<String>();
		pageLines.add( new VCommandHelp().getUseageTemplate() );
		pageLines.add( new VCommandBlood().getUseageTemplate() );
		pageLines.add( new VCommandList().getUseageTemplate() );
		pageLines.add( new VCommandInfect().getUseageTemplate() );
		pageLines.add( new VCommandTurn().getUseageTemplate() );
		pageLines.add( new VCommandCure().getUseageTemplate() );
		pageLines.add( new VCommandFeed().getUseageTemplate() );
		pageLines.add( new VCommandTime().getUseageTemplate() );
		pageLines.add( new VCommandLoad().getUseageTemplate() );
		pageLines.add( new VCommandSave().getUseageTemplate() );
		pageLines.add( new VCommandVersion().getUseageTemplate() );
		helpPages.add(pageLines);
	}
}
