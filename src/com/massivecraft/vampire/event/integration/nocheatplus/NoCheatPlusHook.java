package com.massivecraft.vampire.event.integration.nocheatplus;

import org.bukkit.entity.Player;

import com.massivecraft.vampire.P;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.AbstractNCPHook;
import fr.neatmonster.nocheatplus.hooks.NCPHookManager;

public class NoCheatPlusHook extends AbstractNCPHook
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public P p;
	public NoCheatPlusHook(P p)
	{
		this.p = p;
		NCPHookManager.addHook(CheckType.MOVING_SURVIVALFLY, this);
	}
	
	// -------------------------------------------- //
	// OVERRIDES
	// -------------------------------------------- //

	@Override
	public String getHookName()
	{
		return "Vampire";
	}

	@Override
	public String getHookVersion()
	{
		return p.getDescription().getFullName();
	}

	@Override
	public boolean onCheckFailure(CheckType checkType, Player player)
	{
		return p.noCheatExemptedPlayerNames.contains(player.getName());
	}
}
