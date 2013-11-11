package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore.cmd.arg.ARBoolean;
import com.massivecraft.vampire.*;
import com.massivecraft.vampire.entity.UPlayer;

public class CmdVampireSetVampire extends CmdVampireSetAbstract<Boolean>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireSetVampire()
	{
		targetMustBeOnline = false;
		argReader = ARBoolean.get();
		
		// Aliases
		this.addAliases("v");
		
		// Requirements
		this.setDesc("set vampire (yes or no)");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Boolean set(UPlayer uplayer, Player player, Boolean val)
	{
		Perm perm = val ? Perm.SET_VAMPIRE_TRUE : Perm.SET_VAMPIRE_FALSE;
		
		if ( ! perm.has(sender, true)) return null;
		
		if (uplayer.isVampire() == val) return val;
		
		uplayer.setReason(InfectionReason.OPERATOR);
		uplayer.setMaker(null);
		uplayer.setVampire(val);
		
		return val;
	}
	
}
