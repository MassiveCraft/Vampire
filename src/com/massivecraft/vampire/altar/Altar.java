package com.massivecraft.vampire.altar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.massivecraft.mcore3.util.Txt;
import com.massivecraft.vampire.Conf;
import com.massivecraft.vampire.Lang;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.VPlayers;

public abstract class Altar
{	
	public String name;
	public String desc;
	public Material coreMaterial;
	public Map<Material, Integer> materialCounts;
	public Recipe recipe;
	
	public void evalBlockUse(Block coreBlock, Player player)
	{
		if (coreBlock.getType() != coreMaterial) return;
		VPlayer vplayer = VPlayers.i.get(player);
		
		// Make sure we include the coreBlock material in the wanted ones
		if ( ! this.materialCounts.containsKey(this.coreMaterial))
		{
			this.materialCounts.put(this.coreMaterial, 1);
		}
		
		ArrayList<Block> blocks = getCubeBlocks(coreBlock, Conf.altarSearchRadius);
		Map<Material, Integer> nearbyMaterialCounts = countMaterials(blocks, this.materialCounts.keySet());
		
		int requiredMaterialCountSum = this.sumCollection(this.materialCounts.values());
		int nearbyMaterialCountSum   = this.sumCollection(nearbyMaterialCounts.values());
		
		// If the blocks are to far from looking anything like an altar we will just skip.
		if (nearbyMaterialCountSum < requiredMaterialCountSum * Conf.altarMinRatioForInfo) return;
		
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
		
		// Watch the altar
		this.watch(vplayer, player);
		
		// Attempt a worship
		if (this.worship(vplayer, player) == false) return;
		
		// Can we worship for free?
		if ( ! this.isPaymentRequired(vplayer, player))
		{
			this.effectCommon(vplayer, player);
			this.effectFree(vplayer, player);
			return;
		}
		
		// Do we manage to make the payment?
		if ( ! this.attemptTakePayment(vplayer, player)) return;
		
		// Apply paid effect
		this.effectCommon(vplayer, player);
		this.effectPaid(vplayer, player);
	}
	
	/**
	 * At the watch stage the player is simply looking at the altar.
	 */
	public void watch (VPlayer vplayer, Player player)
	{
		vplayer.msg(this.desc);
	}
	
	/**
	 * At the worship stage the player will get accepted or not.
	 * true means accepted
	 */
	public abstract boolean worship(VPlayer vplayer, Player player);
	
	/**
	 * Next the altar decides on if payment is required
	 */
	public abstract boolean isPaymentRequired(VPlayer vplayer, Player player);
	
	/**
	 * If payment was required we try to take it
	 */
	public boolean attemptTakePayment(VPlayer vplayer, Player player)
	{
		if ( ! this.recipe.playerHasEnough(player))
		{
			vplayer.msg(Lang.altarUseIngredientsFail);
			vplayer.msg(this.recipe.getRecipeLine());
			return false;
		}

		vplayer.msg(Lang.altarUseIngredientsSuccess);
		vplayer.msg(this.recipe.getRecipeLine());
		this.recipe.removeFromPlayer(player);
		return true;
	}
	
	/**
	 * Apply the free effect to the player
	 */
	public abstract void effectFree(VPlayer vplayer, Player player);
	
	/**
	 * Apply the paid effect to the player
	 */
	public abstract void effectPaid(VPlayer vplayer, Player player);
	
	/**
	 * The effectCommon is run in both of the cases
	 */
	public void effectCommon(VPlayer vplayer, Player player) { } ;
	
	
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

