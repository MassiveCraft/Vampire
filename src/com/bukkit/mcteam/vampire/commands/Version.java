package com.bukkit.mcteam.vampire.commands;

import java.util.ArrayList;
import com.bukkit.mcteam.vampire.*;

public class Version extends CommandBase {
	public Version() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBeOp = true;
		senderMustBePlayer = false;
	}
	
	@Override
	public void perform() {
		this.sendMessage("You are running "+Vampire.instance.getDescription().getFullName());
	}
}
