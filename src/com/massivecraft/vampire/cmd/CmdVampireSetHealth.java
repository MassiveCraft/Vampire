package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
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
		targetMustBeOnline = true;
		argReader = ARInteger.get();
		
		// Aliases
		this.addAliases("h");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.SET_HEALTH.node));
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
