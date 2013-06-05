package com.hbsensorsuite.hbsagent.core;

import com.hbsensorsuite.hbsagent.events.HBSEvent;

public class PlayerInstance {
	public String realm = "";
	public String faction = "";
	public String playerName = "";
	public String playerClass = "";
	public String race = "";
	public int level = 0;
	public long lastUpdatedOn = 0;

	public PlayerInstance(HBSEvent e) {
		this.realm = e.Realm;
		this.faction = e.Faction;
		this.playerName = e.Name;
		this.playerClass = e.Class;
		this.race = e.Race;
		this.level = e.Level;
		this.lastUpdatedOn = System.currentTimeMillis();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PlayerInstance) {
			PlayerInstance otherObj = (PlayerInstance)obj;
			return (otherObj.realm.equals(this.realm) && otherObj.faction.equals(this.faction) && otherObj.playerName.equals(this.playerName));
		} else {
			return false;
		}
	}
	
	public void processEvent(HBSEvent e) {
		this.lastUpdatedOn = System.currentTimeMillis();
		System.out.println("GOT IT!");
	}

}
