package com.massivecraft.vampire.cmdarg;

import com.massivecraft.mcore4.cmd.arg.AHPlayerWrapper;
import com.massivecraft.vampire.VPlayer;

public class AHVPlayer extends AHPlayerWrapper<VPlayer>
{
	@Override public Class<VPlayer> getClazz() { return VPlayer.class; }
	
	private AHVPlayer() {}
	private static AHVPlayer instance = new AHVPlayer();
	public static AHVPlayer getInstance() { return instance; } 
}
