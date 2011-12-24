package com.massivecraft.vampire.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class GeometryUtil
{
	
	public static ArrayList<Block> getBallBlocks(Block centerBlock, double radius)
	{
		return getRadiusBlocks(centerBlock, radius, true);
	}
	
	public static ArrayList<Block> getCubeBlocks(Block centerBlock, double radius)
	{
		return getRadiusBlocks(centerBlock, radius, false);
	}
	
	public static ArrayList<Block> getRadiusBlocks(Block centerBlock, double radius, boolean ball)
	{
		return getRadiusBlocks(centerBlock, radius, ball, 1.0,  1.0,  1.0,  1.0,  1.0,  1.0);
	}
	
	// All those radius factors make it possible to select half balls etc.
	public static ArrayList<Block> getRadiusBlocks(Block centerBlock, double radius, boolean ball, double xFromRadiusFactor, double xToRadiusFactor, double yFromRadiusFactor, double yToRadiusFactor, double zFromRadiusFactor, double zToRadiusFactor)
	{
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
	
	public static Map<Material, Integer> countMaterials(Collection<Block> blocks)
	{
		Map<Material, Integer> ret = new HashMap<Material, Integer>();
		for (Block block : blocks)
		{
			Material material = block.getType();
			if ( ! ret.containsKey(material))
			{
				ret.put(material, 1);
				continue;
			}
			ret.put(material, ret.get(material)+1);
		}
		return ret;
	}
	
	public static Map<Material, Integer> countMaterials(Collection<Block> blocks, Set<Material> materialsToCount)
	{
		Map<Material, Integer> ret = new HashMap<Material, Integer>();
		for (Block block : blocks)
		{
			Material material = block.getType();
			if ( ! materialsToCount.contains(material)) continue;
			if ( ! ret.containsKey(material))
			{
				ret.put(material, 1);
				continue;
			}
			ret.put(material, ret.get(material)+1);
		}
		return ret;
	}
}
