package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore2.cmd.req.ReqHasPerm;
import com.massivecraft.mcore2.util.MUtil;
import com.massivecraft.vampire.*;

public class CmdSetInfection extends CmdSetAbstract<Double>
{
	public CmdSetInfection()
	{
		targetMustBeOnline = false;
		classOfT = Double.class;
		this.addAliases("i");
		this.addRequirements(ReqHasPerm.get(Permission.SET_INFECTION.node));
	}

	@Override
	public Double set(VPlayer vplayer, Player player, Double val)
	{
		Double res = MUtil.limitNumber(val, 0D, 100D);
		if (vplayer.vampire())
		{
			msg("<b>%s is already a vampire.", vplayer.getId());
			return null;
		}
		
		vplayer.reason(InfectionReason.OPERATOR);
		vplayer.maker(null);
		vplayer.infection(res);
		vplayer.infectionAdd(0, InfectionReason.OPERATOR, null);
		return res;
	}
}
