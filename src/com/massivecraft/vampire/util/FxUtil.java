package com.massivecraft.vampire.util;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.mcore.MCore;

public class FxUtil
{
	// -------------------------------------------- //
	// PLAYER BUFFS
	// -------------------------------------------- //
	
	public static void ensureBurn(Player player, int ticks)
	{
		boolean now = player.getFireTicks() > 0; 
		if (now) return;
		player.setFireTicks(ticks);
	}
	
	public static void ensure(PotionEffectType type, Player player, int ticks)
	{
		boolean now = player.hasPotionEffect(type);
		if (now) return;
		player.addPotionEffect(new PotionEffect(type, ticks, 1), true);
	}
	
	// -------------------------------------------- //
	// SMOKE
	// -------------------------------------------- //
	
	public static void smoke(Location location, int direction)
	{
		if (location == null) return;
		location.getWorld().playEffect(location, Effect.SMOKE, direction);
	}
	
	public static void smoke(Location location)
	{
		smoke(location, MCore.random.nextInt(9));
	}
	
	public static void smoke(Player player)
	{
		smoke(getRandomPlayerLocation(player));
	}
	
	// -------------------------------------------- //
	// FLAME
	// -------------------------------------------- //
	
	public static void flame(Location location)
	{
		if (location == null) return;
		location.getWorld().playEffect(location, Effect.MOBSPAWNER_FLAMES, 0);
	}
	
	public static void flame(Player player)
	{
		flame(getRandomPlayerLocation(player));
	}
	
	// -------------------------------------------- //
	// FLAME
	// -------------------------------------------- //
	
	public static void ender(Location location)
	{
		if (location == null) return;
		location.getWorld().playEffect(location, Effect.ENDER_SIGNAL, 0);
	}
	
	public static void ender(Player player, int randomMaxLen)
	{
		ender(getRandomPlayerLocation(player, randomMaxLen));
	}
	
	// -------------------------------------------- //
	// SHARED
	// -------------------------------------------- //

	public static Location getRandomPlayerLocation(Player player)
	{
		if (MCore.random.nextBoolean())
		{
			return player.getLocation();
		}
		else
		{
			return player.getEyeLocation();
		}
	}
	
	public static Location getRandomPlayerLocation(Player player, int randomMaxLen)
	{
		Location ret = getRandomPlayerLocation(player);
		
		int dx = getRandomDelta(randomMaxLen);
		int dy = getRandomDelta(randomMaxLen);
		int dz = getRandomDelta(randomMaxLen);
		
		ret.add(dx, dy, dz);
		
		return ret;
	}
	
	public static int getRandomDelta(int randomMaxLen)
	{
		return MCore.random.nextInt(randomMaxLen*2+1) - randomMaxLen;
	}
}
