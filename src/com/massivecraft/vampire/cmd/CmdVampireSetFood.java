package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.entity.MConf;
import com.massivecraft.vampire.entity.UPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdVampireSetFood extends CmdVampireSetAbstract<Integer>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireSetFood()
	{
		super(true, TypeInteger.get());
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.SET_FOOD));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().getAliasesVampireSetFood();
	}
	
	@Override
	public Integer set(UPlayer uplayer, Player player, Integer val)
	{
		Integer res = MUtil.limitNumber(val, 0, 20);
		player.setFoodLevel(res);
		return res;
	}
	
}
