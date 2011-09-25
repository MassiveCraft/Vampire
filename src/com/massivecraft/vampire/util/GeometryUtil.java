package com.massivecraft.vampire.util;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class GeometryUtil {
	
	public static ArrayList<Block> getBallBlocks(Block centerBlock, double radius) {
		return getRadiusBlocks(centerBlock, radius, true);
	}
	
	public static ArrayList<Block> getCubeBlocks(Block centerBlock, double radius) {
		return getRadiusBlocks(centerBlock, radius, false);
	}
	
	public static ArrayList<Block> getCubeBlocksAroundPlayer(Block centerBlock, double radius) {
		ArrayList<Block> retval = getRadiusBlocks(centerBlock, radius, false);
		retval.remove(centerBlock);
		retval.remove(centerBlock.getRelative(BlockFace.UP));
		return retval;
	}
	
	public static ArrayList<Block> getRadiusBlocks(Block centerBlock, double radius, boolean ball) {
		return getRadiusBlocks(centerBlock, radius, ball, 1.0,  1.0,  1.0,  1.0,  1.0,  1.0);
	}
	
	// All those radius factors make it possible to select half balls etc.
	public static ArrayList<Block> getRadiusBlocks(Block centerBlock, double radius, boolean ball, double xFromRadiusFactor, double xToRadiusFactor, double yFromRadiusFactor, double yToRadiusFactor, double zFromRadiusFactor, double zToRadiusFactor) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		int xFrom = (int)(- xFromRadiusFactor * radius);
		int xTo = (int)(xToRadiusFactor * radius);
		int yFrom = (int)(- yFromRadiusFactor * radius);
		int yTo = (int)(yToRadiusFactor * radius);
		int zFrom = (int)(- zFromRadiusFactor * radius);
		int zTo = (int)(zToRadiusFactor * radius);
		
		for(int y=yFrom; y<=yTo; y++)
		{
			for(int z=zFrom; z<=zTo; z++)
			{
				for(int x=xFrom; x<=xTo; x++)
				{
					if( ! ball || x*x+y*y+z*z <= radius*radius)
					{
						blocks.add(centerBlock.getRelative(x, -y, z));
					}
				}
			}
		}
		return blocks;
	}
	
	// How long between two locations?
	public static double distanceBetweenLocations(Location location1, Location location2)
	{
		double X = location1.getX() - location2.getX();
		double Y = location1.getY() - location2.getY();
		double Z = location1.getZ() - location2.getZ();
		return Math.sqrt(X*X+Y*Y+Z*Z);
	}
	
	public static int countNearby(Block centerBlock, Material material, double radius)
	{
		ArrayList<Block> ballBlocks = GeometryUtil.getBallBlocks(centerBlock, radius);
		int count = 0;
		for (Block block : ballBlocks)
		{
			if (block.getType() == material)
			{
				count += 1;
			}
		}
		return count;
	}
}
