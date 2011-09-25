package com.massivecraft.vampire.util;


import java.util.*;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class FloodUtil
{
	// Finds all directly connected blocks of the same material as the startBock
	public static ArrayList<Block> get(Block startBlock) 
	{
		return get(startBlock, 1.0d);
	}
	
	// Finds all blocks of the same material as the startBock. Gaps of length "reachRadius-1" between blocks are tolerated.  
	public static ArrayList<Block> get(Block startBlock, double reachRadius)
	{
		HashSet<Material> materials = new HashSet<Material>();
		materials.add(startBlock.getType());
		
		return get(startBlock, materials, reachRadius);
	}
	
	// From here on you define all materials to include and how long they reach
	public static ArrayList<Block> get(Block startBlock, Map<Material, Double> materialReachRadius)
	{
		return get(startBlock, materialReachRadius, false);
	}
	
	// From here on you define all materials to include and how long they reach
	public static ArrayList<Block> get(Block startBlock, Set<Material> materials, double reachRadius)
	{
		return get(startBlock, materials, reachRadius, false);
	}
	
	// "Comment above" + Should we select a ball or a cube when we flood away?
	public static ArrayList<Block> get(Block startBlock, Map<Material, Double> materialReachRadius, boolean ball)
	{
		return get(startBlock, materialReachRadius, ball, 512);
	}
	
	// "Comment above" + Should we select a ball or a cube when we flood away?
	public static ArrayList<Block> get(Block startBlock, Set<Material> materials, double reachRadius, boolean ball)
	{
		return get(startBlock, materials, reachRadius, ball, 512);
	}
	
	// "Comment above" + Limit the result set
	public static ArrayList<Block> get(Block startBlock, Map<Material, Double> materialReachRadius, boolean ball, int limit)
	{
		return get(startBlock, materialReachRadius, ball, limit, new ArrayList<Block>());
	}
	
	// "Comment above" + Limit the result set
	public static ArrayList<Block> get(Block startBlock, Set<Material> materials, double reachRadius, boolean ball, int limit)
	{
		return get(startBlock, materials, reachRadius, ball, limit, new ArrayList<Block>());
	}
	
	// Base recursive function where each block has an individual reach
	public static ArrayList<Block> get(Block startBlock, Map<Material, Double> materialReachRadius, boolean ball, int limit, ArrayList<Block> foundBlocks)
	{
		// Base cases: 
		// * This block is already found
		// * Limit is reached
		if (foundBlocks.contains(startBlock) || foundBlocks.size() > limit)
		{
			return foundBlocks;
		}
		
		// If this is one of the materials we are searching for...
		Double radius = materialReachRadius.get(startBlock.getType());
		if (radius != null) {
			// ... We found a block :D ...
			foundBlocks.add(startBlock);
			
			// ... And flood away !
			for (Block potentialBlock : GeometryUtil.getRadiusBlocks(startBlock, radius, ball))
			{
				foundBlocks = get(potentialBlock, materialReachRadius, ball, limit, foundBlocks);
			}
		}
		
		return foundBlocks;
	}
	
	// Base recursive function where each block has an individual reach
	public static ArrayList<Block> get(Block startBlock, Set<Material> materials, double reachRadius, boolean ball, int limit, ArrayList<Block> foundBlocks)
	{
		// Base cases: 
		// * This block is already found
		// * Limit is reached
		if (foundBlocks.contains(startBlock) || foundBlocks.size() > limit)
		{
			return foundBlocks;
		}
		
		// If this is one of the materials we are searching for...
		if (materials.contains(startBlock.getType()))
		{
			// ... We found a block :D ...
			foundBlocks.add(startBlock);
			
			// ... And flood away !
			for (Block potentialBlock : GeometryUtil.getRadiusBlocks(startBlock, reachRadius, ball))
			{
				foundBlocks = get(potentialBlock, materials, reachRadius, ball, limit, foundBlocks);
			}
		}
		
		return foundBlocks;
	}
}
