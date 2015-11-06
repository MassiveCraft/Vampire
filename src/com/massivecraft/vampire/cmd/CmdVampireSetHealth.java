package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.vampire.*;
import com.massivecraft.vampire.entity.UPlayer;

public class CmdVampireSetHealth extends CmdVampireSetAbstract<Integer>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireSetHealth()
	{
		super(true, TypeInteger.get());
		
		// Aliases
		this.addAliases("health");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.SET_HEALTH.node));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Integer set(UPlayer uplayer, Player player, Integer val)
	{
		Integer res = MUtil.limitNumber(val, 0, 20);
		player.setHealth(res);
		return res;
	}
	
}
