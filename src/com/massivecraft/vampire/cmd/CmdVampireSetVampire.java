package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.vampire.InfectionReason;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.entity.MConf;
import com.massivecraft.vampire.entity.UPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdVampireSetVampire extends CmdVampireSetAbstract<Boolean>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireSetVampire()
	{
		super(false, TypeBooleanYes.get());
		
		// Requirements
		this.setDesc("set vampire (yes or no)");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().getAliasesVampireSetVampire();
	}
	
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
