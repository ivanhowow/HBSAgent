package com.hbsensorsuite.hbsagent.core;

import java.net.UnknownHostException;

import com.hbsensorsuite.hbsagent.App;
import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoManager {
	private static final MongoManager INSTANCE = new MongoManager();
	
	public static MongoManager instance() {
		return INSTANCE;
	}
	
	private MongoManager() {
		
	}
	
	public boolean connect() throws NumberFormatException, UnknownHostException {
		MongoClient mongoClient = new MongoClient(App.USER_CFG.getProperty("mongo.host"), Integer.parseInt(App.USER_CFG.getProperty("mongo.port")));
		DB db = mongoClient.getDB(App.USER_CFG.getProperty("mongo.dbname"));
		boolean auth = db.authenticate(App.USER_CFG.getProperty("mongo.username"), App.USER_CFG.getProperty("mongo.password").toCharArray());
		if (!auth)
			return false;
		//DBCollection profileOverviewCol = db.getCollection("ProfileOverview");
		return true;
	}
}
