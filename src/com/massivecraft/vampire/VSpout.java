package com.massivecraft.vampire;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.AppearanceManager;
//import org.getspout.spoutapi.player.SpoutPlayer;

import com.massivecraft.vampire.config.SpoutConf;

public class VSpout {
	
	public static Plugin plugin;
	private static AppearanceManager vampApp = SpoutManager.getAppearanceManager();
	
	public static void makeVampire(Player player){
		
		if(SpoutConf.EnableSpout == false){
			P.log("Spout Disabled");
			return;
		}
		 /* //Used To Check If We Can Modify Their Gui
		SpoutPlayer spoutPlayer = SpoutManager.getPlayer(player);
		boolean spoutClient = spoutPlayer.isSpoutCraftEnabled();
		*/
		
		//Get The Name Of The Player For Changing The Title...
		String name = player.getName();
		
		//...And Add The Additional Strings That Go Above The Players Name.
		String trueBloodTitle = SpoutConf.TrueBloodTitle+name;
		String commonTitle = SpoutConf.CommonTitle+name;
				
		//Get A VPlayer Of player Just To Check If The Player Is A TrueBlood.
		VPlayer vplayer = VPlayer.get(player);
		
		if(vplayer.isTrueBlood()){
			if(SpoutConf.EnableTrueBloodSkin == true){
				vampApp.setGlobalSkin(player, SpoutConf.TrueBloodSkinUrl);
				P.log("Skin Set");
			}
			if(SpoutConf.EnableTrueBloodCape == true){
				vampApp.setGlobalCloak(player, SpoutConf.TrueBloodCapeUrl);
			}
			if(SpoutConf.EnableTrueBloodTitle == true){
				vampApp.setGlobalTitle(player, trueBloodTitle);
			}
		}else{
			if(SpoutConf.EnableCommonSkin == true){
				vampApp.setGlobalSkin(player, SpoutConf.CommonSkinUrl);
			}
			if(SpoutConf.EnableCommonCape == true){
				vampApp.setGlobalCloak(player, SpoutConf.CommonCapeUrl);
			}
			if(SpoutConf.EnableCommonTitle == true){
				vampApp.setGlobalTitle(player, commonTitle);
			}
		}
	}

	public static void unMakeVampire(Player player){
		if(SpoutConf.EnableSpout == false){
			return;
		}
		
		/*//Same As Above But This Time To Disable
		SpoutPlayer spoutPlayer = SpoutManager.getPlayer(player);
		boolean spoutClient = spoutPlayer.isSpoutCraftEnabled();
		*/
		vampApp.resetGlobalCloak(player);	//Resets The Player's
		vampApp.resetGlobalSkin(player);	//Skin, Title, And
		vampApp.resetGlobalTitle(player);	//Cape.
	}
}

