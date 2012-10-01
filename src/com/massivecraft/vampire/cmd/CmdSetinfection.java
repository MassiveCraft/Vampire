package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore4.cmd.arg.ARDouble;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;
import com.massivecraft.mcore4.util.MUtil;
import com.massivecraft.vampire.*;

public class CmdSetInfection extends CmdSetAbstract<Double>
{
	public CmdSetInfection()
	{
		targetMustBeOnline = false;
		argReader = ARDouble.get();
		this.addAliases("i");
		this.addRequirements(ReqHasPerm.get(Permission.SET_INFECTION.node));
	}

	@Override
	public Double set(VPlayer vplayer, Player player, Double val)
	{
		Double res = MUtil.limitNumber(val, 0D, 100D);
		if (vplayer.isVampire())
		{
			msg(Lang.xIsAlreadyVamp, vplayer.getId());
			return null;
		}
		
		vplayer.setReason(InfectionReason.OPERATOR);
		vplayer.setMaker(null);
		vplayer.setInfection(res);
		vplayer.addInfection(0, InfectionReason.OPERATOR, null);
		return res;
	}
}
