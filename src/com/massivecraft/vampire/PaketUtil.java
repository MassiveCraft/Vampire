package com.massivecraft.vampire;

import net.minecraft.server.Packet41MobEffect;
import net.minecraft.server.Packet42RemoveMobEffect;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PaketUtil
{
	// Adds the potion effect without the graphical bubbles
	public static void addPotionEffectNoGraphic(Player p, PotionEffect pe) 
	{
		Packet41MobEffect pm = new Packet41MobEffect();
		pm.a = p.getEntityId(); //The entity ID
		pm.b = (byte)pe.getType().getId(); //The potion effect type
		pm.c = (byte)pe.getAmplifier(); //The amplifier
		pm.d = (short)pe.getDuration(); //The duration
		((CraftPlayer)p).getHandle().netServerHandler.sendPacket(pm);
		pm = null;
	}

	// Remove the potion effect
	public static void removePotionEffectNoGraphic(Player p, PotionEffectType pe) 
	{
		Packet42RemoveMobEffect pr = new Packet42RemoveMobEffect();
		pr.a = p.getEntityId();
		pr.b = (byte)pe.getId();
		((CraftPlayer)p).getHandle().netServerHandler.sendPacket(pr);
		pr = null;
	}

}
