package com.massivecraft.vampire.accumulator;

import org.bukkit.entity.Player;

import com.massivecraft.mcore5.util.PlayerUtil;
import com.massivecraft.vampire.VPlayer;

public class VPlayerHealthAccumulator extends VPlayerAccumulator
{
	public VPlayerHealthAccumulator(VPlayer vplayer)
	{
		super(vplayer);
		this.setMin(0);
		this.setMax(20);
	}
	
	@Override
	protected int real()
	{
		Player player = this.vplayer.getPlayer();
		if (player == null) return 0;
		return player.getHealth();
	}
	@Override
	protected void real(int val)
	{
		Player player = this.vplayer.getPlayer();
		if (player == null) return;
		player.setHealth(val);
		PlayerUtil.sendHealthFoodUpdatePacket(player);
	}
}
