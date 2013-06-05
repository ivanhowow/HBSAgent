package com.hbsensorsuite.hbsagent.events;

public class HBSEvent {
	public String Realm;
	public String Faction;
	public String Name;
	public String Class;
	public String Race;
	public int Level;
	public double TimeToLevel;
	public long Copper;
	public long Silver;
	public long Gold;
	public long XPPerHour;
	public long CurrentEXP;
	public long NextEXP;
	public int BagFreeSlots;
	public String Zone;
	public String SubZone;
	public boolean IsAlive;
	public boolean IsAutoAttacking;
	public boolean IsCasting;
	public boolean IsGhost;
	public boolean IsMoving;
	public float Heading;
	
	public String key() {
		return this.Realm + "-" + this.Faction + "-" + this.Name;
	}
}
