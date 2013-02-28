package com.massivecraft.vampire;

public enum InfectionReason
{
	ALTAR(true, false, "altar", "<i>You infected yourself using an <h>altar<i>.", "<i>%1$s was infected using an <h>altar<i>."),
	COMBAT_MISTAKE(false, true, "combat mistake", "<h>%2$s <i>infected you during combat by mistake.", "<h>%2$s <i>infected %1$s during combat by mistake."),
	COMBAT_INTENDED(false, true, "combat intended", "<h>%2$s <i>infected you during combat on purpose.", "<h>%2$s <i>infected %1$s during combat on purpose."),
	TRADE(false, true, "offer", "<i>You were infected from drinking <h>%2$ss <i>blood.", "<i>%1$s was infected from drinking <h>%2$ss <i>blood."),
	OPERATOR(true, false, "evil powers", "<i>You were infected by <h>evil powers<i>.", "<i>%1$s was infected by <h>evil powers<i>."),
	UNKNOWN(true, false, "unknown", "<i>You were infected for <h>unknown <i>reasons.", "<i>%1$s was infected for <h>unknown <i>reasons."),
	;
	
	// Would the victim notice this way of infection?
	private final boolean noticeable;
	public boolean isNoticeable() { return this.noticeable; }
		
	// Was another player the reason?
	private final boolean maker;
	public boolean isMaker() { return this.maker; }
	
	// Short name for the reason.
	private final String shortname;
	public String getShortname() { return this.shortname; }
	
	// Desc when showing yourself.
	private final String selfdesc;
	public String getSelfdesc() { return this.selfdesc; }
	
	// Desc when showing other player.
	private final String otherdesc;
	public String getOtherdesc() { return this.otherdesc; }
	
	private InfectionReason(final boolean notice, final boolean player, final String shortname, final String selfdesc, final String otherdesc)
	{
		this.noticeable = notice;
		this.maker = player;
		this.shortname = shortname;
		this.selfdesc = selfdesc;
		this.otherdesc = otherdesc;
	}
	
	public String getDesc(VPlayer vplayer, boolean self)
	{
		if (self)
		{
			return String.format(this.selfdesc, vplayer.getDisplayName(), vplayer.makerId);
		}
		else
		{
			return String.format(this.otherdesc, vplayer.getDisplayName(), vplayer.makerId);
		}
	}
}
