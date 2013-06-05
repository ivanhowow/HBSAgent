package com.hbsensorsuite.hbsagent.core;

import java.util.*;

import com.hbsensorsuite.hbsagent.events.HBSEvent;

public class HBSEventProcessor {
	private static final HBSEventProcessor INSTANCE = new HBSEventProcessor();
	private Map<String, PlayerInstance> instanceList = new HashMap<String, PlayerInstance>();
	
	public static HBSEventProcessor instance() {
		return INSTANCE;
	}
	
	private HBSEventProcessor() {
		
	}
	
	public synchronized void process(HBSEvent e) {
		String key = e.key();
		PlayerInstance player = null;
		if (instanceList.containsKey(key))
			player = instanceList.get(key);
		else {
			player = new PlayerInstance(e);
			instanceList.put(key, player);
		}
		player.processEvent(e);
	}
}
