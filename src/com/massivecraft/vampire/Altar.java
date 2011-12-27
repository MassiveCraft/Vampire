package com.massivecraft.vampire;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.massivecraft.mcore1.util.Txt;
import com.massivecraft.vampire.config.Conf;
import com.massivecraft.vampire.config.Lang;
import com.massivecraft.vampire.util.GeometryUtil;

public abstract class Altar
{
	public static final transient double reach = 5;
	public static final transient double minRatioForInfo = 0.2;
	
	public String name;
	public String desc;
	public Material coreMaterial;
	public Map<Material, Integer> materialCounts;
	public Recipe recipe;
	
	public Altar()
	{
		
	}
	
	public void evalBlockUse(Block coreBlock, Player player)
	{
		if (coreBlock.getType() != coreMaterial) return;
		
		// Make sure we include the coreBlock material in the wanted ones
		if ( ! this.materialCounts.containsKey(this.coreMaterial))
		{
			this.materialCounts.put(this.coreMaterial, 1);
		}
		
		ArrayList<Block> blocks = GeometryUtil.getCubeBlocks(coreBlock, Conf.altarSearchRadius);
		Map<Material, Integer> nearbyMaterialCounts = GeometryUtil.countMaterials(blocks, this.materialCounts.keySet());
				
		//P.p.log("This is our nearby materials");
		//P.p.log(nearbyMaterialCounts);
		
		int requiredMaterialCountSum = this.sumCollection(this.materialCounts.values());
		int nearbyMaterialCountSum   = this.sumCollection(nearbyMaterialCounts.values());
		
		//P.p.log("requiredMaterialCountSum: "+requiredMaterialCountSum);
		//P.p.log("nearbyMaterialCountSum: "+nearbyMaterialCountSum);
		
		// If the blocks are to far from looking anything like an altar we will just skip.
		if (nearbyMaterialCountSum < requiredMaterialCountSum * minRatioForInfo) return;
		
		// This is what's missing
		Map<Material, Integer> missingMaterialCounts = this.getMissingMaterialCounts(nearbyMaterialCounts);
		//P.p.log("This is what's missing:");
		//P.p.log(missingMaterialCounts);
		
		// Have we got all we need?
		if (this.sumCollection(missingMaterialCounts.values()) == 0)
		{
			this.onUse(player);
			return;
		}
		
		// Send info on what to do to finish the altar 
		player.sendMessage(Txt.parse(Lang.altarIncomplete, this.name));
		
		for (Entry<Material, Integer> entry : missingMaterialCounts.entrySet())
		{
			Material material = entry.getKey();
			int count = entry.getValue();
			player.sendMessage(Txt.parse("<h>%d <p>%s", count, Txt.getMaterialName(material)));
		}
	}
	
	public void onUse(Player player)
	{
		player.sendMessage(Txt.parse(this.desc));
		
		if ( ! this.validateUser(player)) return;
		if ( ! this.validateIngredients(player)) return;
		
		this.applyEffect(player);
	}
	
	public void applyEffect(Player player)
	{
		player.sendMessage("DERP :)!");
	}
	
	public boolean validateIngredients(Player player)
	{
		if ( ! this.recipe.playerHasEnough(player))
		{
			player.sendMessage(Txt.parse(Lang.altarUseIngredientsFail));
			player.sendMessage(this.recipe.getRecipeLine());
			return false;
		}

		player.sendMessage(Txt.parse(Lang.altarUseIngredientsSuccess));
		player.sendMessage(this.recipe.getRecipeLine());
		this.recipe.removeFromPlayer(player);
		return true;
	}
	
	public boolean validateUser(Player player)
	{
		return true;
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
	
}

