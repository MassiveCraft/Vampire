package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore3.cmd.req.ReqHasPerm;
import com.massivecraft.mcore3.util.MUtil;
import com.massivecraft.vampire.*;

public class CmdSetFood extends CmdSetAbstract<Integer>
{
	public CmdSetFood()
	{
		targetMustBeOnline = true;
		classOfT = Integer.class;
		
		this.addAliases("f");
		this.addRequirements(ReqHasPerm.get(Permission.SET_FOOD.node));
	}

	@Override
	public Integer set(VPlayer vplayer, Player player, Integer val)
	{
		Integer res = MUtil.limitNumber(val, 0, 20);
		player.setFoodLevel(res);
		return res;
	}
}
