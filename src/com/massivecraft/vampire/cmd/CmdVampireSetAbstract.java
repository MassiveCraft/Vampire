package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.vampire.Vampire;
import com.massivecraft.vampire.entity.UPlayer;
import com.massivecraft.vampire.entity.UPlayerColl;
import com.massivecraft.vampire.entity.UPlayerColls;

public abstract class CmdVampireSetAbstract<T> extends VCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public boolean targetMustBeOnline;
	public Type<T> type;
	
	private Parameter playerReaderParameter = new Parameter(UPlayerColls.get().getForUniverse(MassiveCore.DEFAULT).getTypeEntity(), true, "player", "you");
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireSetAbstract(boolean targetMustBeOnline, Type<T> type)
	{
		// Seup fields
		this.targetMustBeOnline = targetMustBeOnline;
		this.type = type;
		
		// Parameters
		this.addParameter(type, "val");
		this.addParameter(playerReaderParameter);
		this.addParameter(Vampire.get().playerAspect.getMultiverse().typeUniverse(), "univ", "you");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		String universe = this.readArgAt(2, senderIsConsole ? MassiveCore.DEFAULT : Vampire.get().playerAspect.getMultiverse().getUniverse(me));
		
		UPlayerColl playerColl = UPlayerColls.get().getForUniverse(universe);
		Type<UPlayer> playerType = playerColl.getTypeEntity();
		this.playerReaderParameter.setType(playerType);
		
		UPlayer uplayer = this.readArgAt(1, vme);
				
		Player player = uplayer.getPlayer();
		
		if (targetMustBeOnline && player == null)
		{
			msg("<h>%s <b>is not online.", uplayer.getDisplayName(sender));
			return;
		}
		
		T val = this.readArgAt(0);
		
		T res = this.set(uplayer, player, val);
		
		if (res == null) return;
		
		msg("<i>%s <i>now has %s = %s.", uplayer.getDisplayName(sender), this.getAliases().get(0), res.toString());
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract T set(UPlayer uplayer, Player player, T val);
	
}
