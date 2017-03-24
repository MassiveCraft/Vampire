package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasItemInHand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.vampire.BloodFlaskUtil;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.entity.MConf;
import com.massivecraft.vampire.entity.MLang;
import com.massivecraft.vampire.type.TypeLimitedDouble;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdVampireFlask extends VCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireFlask()
	{
		// Parameters
		this.addParameter(TypeLimitedDouble.get(0D, 20D), "amount", "4.0").setDefaultValue(4D);
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.FLASK));
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(RequirementHasItemInHand.get(Material.GLASS_BOTTLE));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().aliasesVampireFlask;
	}
	
	@Override
	public void perform() throws MassiveException
	{
		// Parameters
		double amount = this.readArg();
		Player player = vme.getPlayer();
		
		// Does the player have the required amount?
		if ((vme.isVampire() && amount > vme.getFood().get()) || ( ! vme.isVampire() && amount > player.getHealth()))
		{
			vme.msg(MLang.get().flaskInsufficient);
			return;
		}
		
		// ... create a blood flask!
		if (vme.isVampire())
		{
			vme.getFood().add( - amount);
		}
		else
		{
			player.setHealth(player.getHealth() - amount);
		}
		BloodFlaskUtil.fillBottle(amount, vme);
		
		// Inform
		vme.msg(MLang.get().flaskSuccess);
	}
	
}
