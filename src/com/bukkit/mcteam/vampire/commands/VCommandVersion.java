package com.bukkit.mcteam.vampire.commands;

import java.util.ArrayList;

import com.bukkit.mcteam.vampire.*;

public class VCommandVersion extends VCommand {
	public VCommandVersion() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBeOp = true;
		senderMustBePlayer = false;
	}
	
	@Override
	public void perform() {
		this.sendMessage("You are running "+Vampire.instance.getDescription().getFullName());
		
		/*
		Testcode... trying to generate smoke and stuff... not working :/
		if ( ! (this.sender instanceof Player) ) {
			return;
		}
		Player player = (Player)this.sender;
		
		
		Vampire.log("globbbb player.getLocation()" + player.getLocation());
		
		Location playerLocation = player.getLocation();
		double x = playerLocation.getX() + Math.random() * 2D - 1D;
		double y = playerLocation.getY() + Math.random() * 2D - 1D;
		double z = playerLocation.getZ() + Math.random() * 2D - 1D;
		double w = 0.0D;
		CraftWorld cw = (CraftWorld)player.getWorld();
		cw.getHandle().a("smoke", x+Math.random(), y+Math.random(), z+Math.random(), w, w, w);
		cw.getHandle().a("largesmoke", x+Math.random(), y+Math.random(), z+Math.random(), w, w, w);
		cw.getHandle().a("bubble", x+Math.random(), y+Math.random(), z+Math.random(), w, w, w);
		cw.getHandle().a("splash", x+Math.random(), y+Math.random(), z+Math.random(), w, w, w);
		cw.getHandle().a("snowballpoof", x+Math.random(), y+Math.random(), z+Math.random(), w, w, w);
		cw.getHandle().a("explode", x+Math.random(), y+Math.random(), z+Math.random(), w, w, w);
		cw.getHandle().a("flame", x+Math.random(), y+Math.random(), z+Math.random(), w, w, w);
		*/
	}
}
