package com.massivecraft.vampire;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.entity.Player;

import com.google.gson.reflect.TypeToken;
import com.massivecraft.vampire.zcore.persist.PlayerEntityCollection;

public class VPlayers extends PlayerEntityCollection<VPlayer>
{
	public static VPlayers i = new VPlayers();
	
	P p = P.p;
	
	private VPlayers()
	{
		super
		(
				VPlayer.class,
				new CopyOnWriteArrayList<VPlayer>(),
				new ConcurrentSkipListMap<String, VPlayer>(String.CASE_INSENSITIVE_ORDER),
				new File(P.p.getDataFolder(), "player.json"),
				P.p.gson
		);
		
		this.setCreative(true);
	}
	
	@Override
	public Type getMapType()
	{
		return new TypeToken<Map<String, VPlayer>>(){}.getType();
	}
	
	public VPlayer get(Player player)
	{
		return this.get(player.getName());
	}
	
	public Set<VPlayer> findAllOnlineInfected()
	{
		Set<VPlayer> vplayers = new HashSet<VPlayer>();
		for (VPlayer vplayer : this.getOnline())
		{
			if (vplayer.isInfected())
			{
				vplayers.add(vplayer);
			}
		}
		return vplayers;
	}
	
	public Set<VPlayer> findAllOnlineVampires()
	{
		Set<VPlayer> vplayers = new HashSet<VPlayer>();
		for (VPlayer vplayer : this.getOnline())
		{
			if (vplayer.isVampire())
			{
				vplayers.add(vplayer);
			}
		}
		return vplayers;
	}
}
