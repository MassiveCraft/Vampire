package com.massivecraft.vampire.commands;

import java.util.*;
import java.util.regex.Pattern;

import com.massivecraft.vampire.config.Conf;
import com.massivecraft.vampire.util.TextUtil;

public class VCommandTime extends VCommand {

	public VCommandTime() {
		aliases.add("time");

		optionalParameters.add("time");
		
		helpDescription = "get or set the time";
		
		permission = "vampire.command.time";
		senderMustBePlayer = true;
		senderMustBeVampire = false;
	}

	@Override
	public void perform() {
		// Read the time
		if (parameters.size() == 0) {
			long ticks = player.getWorld().getTime() % 24000;
			sendMessage("The time is " + Conf.colorHighlight + ticks + Conf.colorSystem + " ticks.");
			sendTimeLeftToMessage(ticks);
			return;
		}
		
		String param = parameters.get(0);
		
		// Set by ticks
		boolean isNumeric = Pattern.matches("^\\d*$", param);
		if (isNumeric) {
			long ticks = Long.parseLong(param) % 24000;
			setTime(ticks);
			sendTimeLeftToMessage(ticks);
			return;
		}
		
		// Set by alias
		param = param.toLowerCase();
		if ( ! nameToTicks.containsKey(param)) {
			String message1 = "Unknown time alias " + Conf.colorHighlight + param + Conf.colorSystem + ". These are available:";
			
			ArrayList<String> aliases = new ArrayList<String>();
			for (String alias : nameToTicks.keySet()) {
				aliases.add(Conf.colorHighlight + alias);
			}
			
			String message2 = TextUtil.implode(aliases, Conf.colorSystem + ", ");
			
			sendMessage(message1);
			sendMessage(message2);
			
			return;
		}
		
		long ticks = nameToTicks.get(param);
		setTime(ticks);
		sendTimeLeftToMessage(ticks);
	}
	
	public void setTime(long ticks) {
		player.getWorld().setTime(ticks);
		sendMessage("The time was set to " + Conf.colorHighlight + ticks + Conf.colorSystem + " ticks.");
	}
	
	public void sendTimeLeftToMessage(long ticks) {
		long left;
		long leftMin;
		long targetTicks;
		String targetName;
		
		if (ticks >= 0 && ticks < 14000) {
			targetTicks = 14000;
			targetName = "night";
		} else {
			targetTicks = 24000;
			targetName = "day";
		}
		
		left = targetTicks - ticks;
		leftMin = Math.round(20.0 * left / 24000.0);
		sendMessage("Time left to " + Conf.colorHighlight + targetName + Conf.colorSystem + " is " + Conf.colorHighlight + left + Conf.colorSystem + " ticks (" + Conf.colorHighlight + leftMin + Conf.colorSystem + " min).");
	}
	
	public static Map<String, Integer> nameToTicks = new LinkedHashMap<String, Integer>();
	
	static {
		nameToTicks.put("sunrise", 22000);
		nameToTicks.put("rise", 22000);
		nameToTicks.put("dawn", 22000);
		
		nameToTicks.put("daystart", 0);
		nameToTicks.put("day", 0);
		
		nameToTicks.put("morning", 3000);
		
		nameToTicks.put("midday", 6000);
		nameToTicks.put("noon", 6000);
		
		nameToTicks.put("afternoon", 9000);
		
		nameToTicks.put("sunset", 12000);
		nameToTicks.put("set", 12000);
		nameToTicks.put("dusk", 12000);
		nameToTicks.put("sundown", 12000);
		nameToTicks.put("nightfall", 12000);
		
		nameToTicks.put("nightstart", 14000);
		nameToTicks.put("night", 14000);
		
		nameToTicks.put("midnight", 18000);
	}
	
}
