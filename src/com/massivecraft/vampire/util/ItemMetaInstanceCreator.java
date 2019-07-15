package com.massivecraft.vampire.util;

import com.google.gson.InstanceCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Type;

public class ItemMetaInstanceCreator implements InstanceCreator<ItemMeta> {
    @Override
    public ItemMeta createInstance(Type type) {
        return new ItemStack(Material.POTION).getItemMeta();
    }
}
