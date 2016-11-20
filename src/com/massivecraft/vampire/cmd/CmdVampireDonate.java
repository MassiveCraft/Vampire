package com.massivecraft.vampire.cmd;

import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeDouble;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.vampire.BloodFlaskUtil;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.entity.MLang;

public class CmdVampireDonate extends VCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireDonate()
	{
		// Aliases
		this.addAliases("donate");
		
		// Parameters
		this.addParameter(TypeDouble.get(), "amount", "4.0");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.DONATE));
		this.addRequirements(RequirementIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		double unlimitedAmount = this.readArg(4D);
		
		double amount = MUtil.limitNumber(unlimitedAmount, 0D, 20D);
		if (amount != unlimitedAmount)
		{
			msg("<b>Amount must be between 0.0 and 20.0");
			return;
		}
		
		// If the player is holding a glass bottle ...
		if (BloodFlaskUtil.playerHoldsGlassBottle(vme.getPlayer()))
		{
			// ... and has the required amount ...
			if ((vme.isVampire() && amount>vme.getFood().get()) || ( ! vme.isVampire() && amount>vme.getPlayer().getHealth()))
			{
				vme.msg(MLang.get().donateInsufficient);
				return;
			}
			
			// ... create a blood flask!
			if (vme.isVampire())
			{
				vme.getFood().add(-amount);
			}
			else
			{
				vme.getPlayer().setHealth(vme.getPlayer().getHealth()-amount);
			}
			BloodFlaskUtil.playerConsumeGlassBottle(vme.getPlayer());
			PlayerInventory pInv = vme.getPlayer().getInventory();
			pInv.addItem(BloodFlaskUtil.createBloodFlask(amount, vme.isVampire()));
			vme.msg(MLang.get().donateSuccess);
		}
		else
		{
			vme.msg(MLang.get().donateNoBottle);
		}
	}
	
}