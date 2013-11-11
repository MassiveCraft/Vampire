package com.massivecraft.vampire.accumulator;

import org.bukkit.entity.Player;

import com.massivecraft.mcore.util.PlayerUtil;
import com.massivecraft.vampire.entity.UPlayer;

public class UPlayerFoodAccumulator extends UPlayerAccumulator
{
	public UPlayerFoodAccumulator(UPlayer uplayer)
	{
		super(uplayer);
		this.setMin(0);
		this.setMax(20);
	}
	
	@Override
	protected int real()
	{
		Player player = this.uplayer.getPlayer();
		if (player == null) return 0;
		return player.getFoodLevel();
	}
	@Override
	protected void real(int val)
	{
		Player player = this.uplayer.getPlayer();
		if (player == null) return;
		player.setFoodLevel(val);
		PlayerUtil.sendHealthFoodUpdatePacket(player);
	}
	
}
