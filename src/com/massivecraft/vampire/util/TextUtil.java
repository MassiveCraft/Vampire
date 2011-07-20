package com.massivecraft.vampire.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

public class TextUtil {
	public static String repeat(String s, int times) {
	    if (times <= 0) return "";
	    else return s + repeat(s, times-1);
	}
	
	public static ArrayList<String> split(String str) {
		return new ArrayList<String>(Arrays.asList(str.trim().split("\\s+")));
	}
	
	public static String implode(List<String> list, String glue) {
	    String ret = "";
	    for (int i=0; i<list.size(); i++) {
	        if (i!=0) {
	        	ret += glue;
	        }
	        ret += list.get(i);
	    }
	    return ret;
	}
	public static String implode(List<String> list) {
		return implode(list, " ");
	}
	
	public static String getMaterialName(Material material) {
		String ret = material.toString();
		ret = ret.replace('_', ' ');
		ret = ret.toLowerCase();
		return ret;
	}
	
	public static String upperCaseFirst(String string) {
		return string.substring(0, 1).toUpperCase()+string.substring(1);
	}
}


