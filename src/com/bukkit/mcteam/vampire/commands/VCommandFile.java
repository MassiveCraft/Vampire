package com.bukkit.mcteam.vampire.commands;

import java.util.ArrayList;

import com.bukkit.mcteam.vampire.*;

public class VCommandFile extends VCommand {
	public String msgLoadSuccess = "Loaded %s.";
	public String msgLoadFail = "FAILED to load %s.";
	public String msgSaveSuccess = "Saved %s.";
	public String msgSaveFail = "FAILED to save %s.";
	
	public VCommandFile() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		
		requiredParameters.add("what");
	}
	
	public boolean meansConfiguration (String str) {
		return "configurations".startsWith(str.toLowerCase());
	}
	public boolean meansPlayers (String str) {
		return "players".startsWith(str.toLowerCase()) || "vampires".startsWith(str.toLowerCase());
	}
	public boolean meansAll (String str) {
		return "all".startsWith(str.toLowerCase()) || "both".startsWith(str.toLowerCase());
	}
	
	public String saveConfiguration() {
		if (Conf.save()) {
			return String.format(msgSaveSuccess, Conf.file);
		} else {
			return String.format(msgSaveFail, Conf.file);
		}
	}
	public String savePlayers() {
		if (VPlayer.save()) {
			return String.format(msgSaveSuccess, VPlayer.file);
		} else {
			return String.format(msgSaveFail, VPlayer.file);
		}
	}
	public String loadConfiguration() {
		if (Conf.load()) {
			return String.format(msgLoadSuccess, Conf.file);
		} else {
			return String.format(msgLoadFail, Conf.file);
		}
	}
	public String loadPlayers() {
		if (VPlayer.load()) {
			return String.format(msgLoadSuccess, VPlayer.file);
		} else {
			return String.format(msgLoadFail, VPlayer.file);
		}
	}
}
