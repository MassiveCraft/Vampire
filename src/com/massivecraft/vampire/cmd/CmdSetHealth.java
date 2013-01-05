package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore5.cmd.arg.ARInteger;
import com.massivecraft.mcore5.cmd.req.ReqHasPerm;
import com.massivecraft.mcore5.util.MUtil;
import com.massivecraft.vampire.*;

public class CmdSetHealth extends CmdSetAbstract<Integer>
{
	public CmdSetHealth()
	{
		targetMustBeOnline = true;
		argReader = ARInteger.get();
		this.addAliases("h");
		this.addRequirements(ReqHasPerm.get(VPerm.SET_HEALTH.node));
	}

	@Override
	public Integer set(VPlayer vplayer, Player player, Integer val)
	{
		Integer res = MUtil.limitNumber(val, 0, 20);
		player.setHealth(res);
		return res;
	}
}
