package com.massivecraft.vampire.util;

import com.massivecraft.vampire.entity.MConf;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

public class SunUtil
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static int MID_DAY_TICKS = 6000;
	public final static int DAY_TICKS = 24000;
	public final static int HALF_DAY_TICKS = DAY_TICKS / 2;
	public final static int DAYTIME_TICKS = 14000;
	public final static int HALF_DAYTIME_TICKS = DAYTIME_TICKS / 2;
	public final static double HALF_PI = Math.PI / 2;
	public final static double MDTICKS_TO_ANGLE_FACTIOR = HALF_PI / HALF_DAYTIME_TICKS;
	
	// -------------------------------------------- //
	// SOLAR RADIATION CALCULATION
	// -------------------------------------------- //
	
	/**
	 * This time of day relative to mid day.
	 * 0 means midday. -7000 mean start of sunrise. +7000 means end of sundown.
	 */
	public static int calcMidDeltaTicks(World world, Player player)
	{
		long rtime = world.getFullTime();
		if (MConf.get().isUseWorldGuardRegions() && Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
			RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
			if (rm != null) {
				int bestPriority = 0;
				Set<ProtectedRegion> prset = rm.getApplicableRegions(BlockVector3.at(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())).getRegions();
				for (ProtectedRegion pr : prset) {
					Map<Flag<?>, Object> flags = pr.getFlags();
					if (flags.containsKey(Flags.TIME_LOCK)) {
						try {
							long auxrtime = Long.parseLong((String) flags.get(Flags.TIME_LOCK));
							if (pr.getPriority() >= bestPriority) {
								rtime = auxrtime;
								bestPriority = pr.getPriority();
							}
						}
						catch(NumberFormatException e){	}
					}
				}
			}
		}

		int ret = (int) ((rtime - MID_DAY_TICKS) % DAY_TICKS);

		if (ret >= HALF_DAY_TICKS)
		{
			ret -= DAY_TICKS;
		}

		return ret;
	}
	
	/**
	 * The insolation angle in radians.
	 * 0 means directly from above. -Pi/2 means start of sunrise etc.
	 */
	public static double calcSunAngle(World world, Player player)
	{
		int mdticks = calcMidDeltaTicks(world, player);
		return MDTICKS_TO_ANGLE_FACTIOR * mdticks;
	}
	
	/**
	 * A value between 0 and 1. 0 means no sun at all. 1 means sun directly from above.
	 * http://en.wikipedia.org/wiki/Effect_of_sun_angle_on_climate
	 */
	public static double calcSolarRad(World world, Player player)
	{
		if (world.getEnvironment() != Environment.NORMAL) return 0d;
		boolean storming = world.hasStorm();
		if (MConf.get().isUseWorldGuardRegions() && Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
			RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
			if (rm != null) {
				int bestPriority = 0;
				Set<ProtectedRegion> prset = rm.getApplicableRegions(BlockVector3.at(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())).getRegions();
				for (ProtectedRegion pr : prset) {
					Map<Flag<?>, Object> flags = pr.getFlags();
					if (flags.containsKey(Flags.WEATHER_LOCK)) {
						try {
							boolean auxstorm = ((com.sk89q.worldedit.world.weather.WeatherType) flags.get(Flags.WEATHER_LOCK)).getName() == WeatherType.DOWNFALL.name();
							if (pr.getPriority() >= bestPriority) {
								storming = auxstorm;
								bestPriority = pr.getPriority();
							}
						}
						catch(NumberFormatException e){	}
					}
				}
			}
		}

		if (storming) return 0d;
		double angle = calcSunAngle(world, player);
		double absangle = Math.abs(angle);
		if (absangle >= HALF_PI) return 0;
		double a = HALF_PI - absangle;
		//P.p.log("calcSolarRadiation", Math.sin(a));
		return Math.sin(a);
	}
	
	// -------------------------------------------- //
	// TERRAIN OPACITY CALCULATION
	// -------------------------------------------- //
	
	/**
	 * The sum of the opacity above and including the block.
	 */
	@SuppressWarnings("deprecation")
	public static double calcTerrainOpacity(Block block)
	{
		double ret = 0;
		
		int x = block.getX();
		int z = block.getZ();
		World world = block.getWorld();
		int maxy = world.getMaxHeight() -1;
		
		for (int y = block.getY(); y <= maxy && ret < 1d; y++)
		{
			Material type = world.getBlockAt(x, y, z).getType();
			Double opacity = MConf.get().getTypeOpacity().get(type);
			if (opacity == null)
			{
				opacity = 1d; // Blocks not in that map have opacity 1;
			}
			ret += opacity;
		}
		
		if (ret > 1.0D) ret = 1d;
		
		//P.p.log("calcTerrainOpacity",ret);
		
		return ret;
	}
	
	// -------------------------------------------- //
	// ARMOR OPACITY CALCULATION
	// -------------------------------------------- //
	
	/**
	 * The armor opacity against solar radiation.
	 * http://en.wikipedia.org/wiki/Opacity_%28optics%29
	 */
	public static double calcArmorOpacity(Player player)
	{
		double ret = 0;
		for (ItemStack itemStack : player.getInventory().getArmorContents())
		{
			if (itemStack == null) continue;
			if (itemStack.getAmount() == 0) continue;
			if (itemStack.getType() == Material.AIR) continue;
			ret += MConf.get().getOpacityPerArmorPiece();
		}
		return ret;
	}

	// -------------------------------------------- //
	// PLAYER CALCULATIONS
	// -------------------------------------------- //
	
	/**
	 * The player irradiation is a value between 0 and 1.
	 * It is based on the irradiation from the sun but the 
	 * opacity of the terrain and player armor is taken into acocunt.
	 */
	public static double calcPlayerIrradiation(Player player)
	{
		// Player must exist.
		if ( ! player.isOnline()) return 0;
		if (player.isDead()) return 0;
		
		// Insolation
		World world = player.getWorld();
		double ret = calcSolarRad(world, player);
		if (ret == 0) return 0;
		
		// Terrain
		Block block = player.getLocation().getBlock().getRelative(0, 1, 0);
		double terrainOpacity = calcTerrainOpacity(block);
		ret *= (1-terrainOpacity);
		if (ret == 0) return 0;
		
		// Armor
		double armorOpacity = calcArmorOpacity(player);
		ret *= (1-armorOpacity);
		if (ret == 0) return 0;
		//P.p.log("calcPlayerIrradiation",ret);
		return ret;
	}
	
}
