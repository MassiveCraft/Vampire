package com.massivecraft.vampire.config;

import java.io.File;
import java.io.IOException;

import com.massivecraft.vampire.P;
import com.massivecraft.vampire.util.DiscUtil;

public class SpoutConf {
	public static transient File file = new File(P.instance.getDataFolder(), "SpoutConf.json");
	
	public static boolean EnableSpout = false;
	public static boolean EnableTrueBloodSkin = true;
	public static boolean EnableTrueBloodCape = true;
	public static boolean EnableTrueBloodTitle = true;
	public static boolean EnableCommonSkin = true;
	public static boolean EnableCommonCape = false;
	public static boolean EnableCommonTitle = true;
	public static String TrueBloodSkinUrl = "https://dl.dropbox.com/s/m5tbslhm82k1sny/skin.png";
	public static String TrueBloodCapeUrl = "https://dl.dropbox.com/s/f5ybmjk2tn91iwa/cape.png";
	public static String TrueBloodTitle = "TrueBlood\n";
	public static String CommonSkinUrl = "https://dl.dropbox.com/s/m5tbslhm82k1sny/skin.png";
	public static String CommonCapeUrl = "https://dl.dropbox.com/s/f5ybmjk2tn91iwa/cape.png";
	public static String CommonTitle = "Common\n";

	public static boolean save() {
		P.log("Saving config to disk.");
		try {
			DiscUtil.write(file, P.instance.gson.toJson(new SpoutConf()));
		} catch (IOException e) {
			e.printStackTrace();
			P.log("Failed to save SpoutConf to disk.");
			return false;
		}
		return true;
	}
	
	public static boolean load() {
		if ( ! file.exists()) {
			P.log("No SpoutConf to load from disk. Creating new file.");
			save();
			return true;
		}
		
		try {
			P.instance.gson.fromJson(DiscUtil.read(file), SpoutConf.class);
		} catch (IOException e) {
			e.printStackTrace();
			P.log("Failed to load the SpoutConf from disk.");
			return false;
		}
		
		return true;
	}
}
