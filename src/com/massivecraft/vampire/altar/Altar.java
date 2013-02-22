package com.massivecraft.vampire.altar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore.util.Txt;
import com.massivecraft.vampire.Conf;
import com.massivecraft.vampire.Lang;
import com.massivecraft.vampire.VPlayer;

public abstract class Altar
{	
	public String name;
	public String desc;
	public Material coreMaterial;
	public Map<Material, Integer> materialCounts;
	public List<ItemStack> resources;
	
	public void evalBlockUse(Block coreBlock, Player player)
	{
		if (coreBlock.getType() != coreMaterial) return;
		VPlayer vplayer = VPlayer.get(player);
		Conf conf = Conf.get(player);
		
		// Make sure we include the coreBlock material in the wanted ones
		if ( ! this.materialCounts.containsKey(this.coreMaterial))
		{
			this.materialCounts.put(this.coreMaterial, 1);
		}
		
		ArrayList<Block> blocks = getCubeBlocks(coreBlock, conf.altarSearchRadius);
		Map<Material, Integer> nearbyMaterialCounts = countMaterials(blocks, this.materialCounts.keySet());
		
		int requiredMaterialCountSum = this.sumCollection(this.materialCounts.values());
		int nearbyMaterialCountSum   = this.sumCollection(nearbyMaterialCounts.values());
		
		// If the blocks are to far from looking anything like an altar we will just skip.
		if (nearbyMaterialCountSum < requiredMaterialCountSum * conf.altarMinRatioForInfo) return;
		
		// What alter blocks are missing?
		Map<Material, Integer> missingMaterialCounts = this.getMissingMaterialCounts(nearbyMaterialCounts);
		
		// Is the altar complete?
		if (this.sumCollection(missingMaterialCounts.values()) > 0)
		{
			// Send info on what to do to finish the altar 
			player.sendMessage(Txt.parse(Lang.altarIncomplete, this.name));
			for (Entry<Material, Integer> entry : missingMaterialCounts.entrySet())
			{
				Material material = entry.getKey();
				int count = entry.getValue();
				player.sendMessage(Txt.parse("<h>%d <p>%s", count, Txt.getMaterialName(material)));
			}
			return;
		}
		
		this.use(vplayer, player);
		
	}
	
	public abstract void use(VPlayer vplayer, Player player);
	
	public void watch (VPlayer vplayer, Player player)
	{
		vplayer.msg(this.desc);
	}
	
	// ------------------------------------------------------------ //
	// Some calculation utilities
	// ------------------------------------------------------------ //
	
	public int sumCollection(Collection<Integer> collection)
	{
		int ret = 0;
		for (Integer i : collection) ret += i;
		return ret;
	}
	
	public Map<Material, Integer> getMissingMaterialCounts(Map<Material, Integer> floodMaterialCounts)
	{
		Map<Material, Integer> ret = new HashMap<Material, Integer>();
		
		for (Entry<Material, Integer> entry : materialCounts.entrySet())
		{
			Integer ihave = floodMaterialCounts.get(entry.getKey());
			if (ihave == null) ihave = 0;
			int missing = entry.getValue() - ihave;
			if (missing < 0) missing = 0;
			ret.put(entry.getKey(), missing);
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
	
	public static ArrayList<Block> getCubeBlocks(Block centerBlock, int radius)
	{
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		for (int y = -radius; y <= radius; y++)
		{
			for (int z = -radius; z <= radius; z++)
			{
				for (int x = -radius; x <= radius; x++)
				{
					blocks.add(centerBlock.getRelative(x, y, z));
				}
			}
		}
		return blocks;
	}
}

