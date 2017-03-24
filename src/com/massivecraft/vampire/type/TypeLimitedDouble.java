package com.massivecraft.vampire.type;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.type.primitive.TypeDouble;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TypeLimitedDouble extends TypeDouble
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	private double min;

	private double max;

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public static TypeLimitedDouble get(double min, double max)
	{
		return new TypeLimitedDouble(min, max);
	}

	private TypeLimitedDouble(double min, double max)
	{
		this.min = min;
		this.max = max;
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Double read(String arg, CommandSender sender) throws MassiveException
	{
		// Read Double
		Double ret = super.read(arg, sender);

		// Limit
		double amount = MUtil.limitNumber(ret, min, max);
		if (ret.compareTo(amount) == 0) return ret;

		// Throw if not within limits
		throw new MassiveException().addMsg("<b>Number must be between %.1f and %.1f", min, max);
	}
 
	@Override
	public List<String> getTabList(CommandSender sender, String arg)
	{
		return new MassiveList<>();
	}
	
}
