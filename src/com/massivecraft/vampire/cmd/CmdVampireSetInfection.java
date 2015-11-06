package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeDouble;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.vampire.InfectionReason;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.entity.MLang;
import com.massivecraft.vampire.entity.UPlayer;

public class CmdVampireSetInfection extends CmdVampireSetAbstract<Double>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireSetInfection()
	{
		super(false, TypeDouble.get());
		
		// Aliases
		this.addAliases("infection");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.SET_INFECTION.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Double set(UPlayer uplayer, Player player, Double val)
	{
		Double res = MUtil.limitNumber(val, 0D, 100D);
		if (uplayer.isVampire())
		{
			msg(MLang.get().xIsAlreadyVamp, uplayer.getDisplayName(sender));
			return null;
		}
		
		uplayer.setReason(InfectionReason.OPERATOR);
		uplayer.setMaker(null);
		uplayer.setInfection(res);
		uplayer.addInfection(0, InfectionReason.OPERATOR, null);
		return res;
	}
	
}
