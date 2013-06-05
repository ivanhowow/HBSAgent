package com.hbsensorsuite.hbsagent.core;

import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.hbsensorsuite.hbsagent.events.HBSEvent;


public class PluginEventListener extends Thread {
	private Logger LOG = Logger.getLogger(PluginEventListener.class.getName());
	
	private final int MAX_ERRORS = 10;
	
	private DatagramSocket socket;
	private boolean isRunning;

	private int listeningPort;

	private Gson gson;

	private HBSEventProcessor eventProcessor;
	public PluginEventListener(int listeningPort) throws SocketException {
		this.listeningPort = listeningPort;
		socket = new DatagramSocket(listeningPort);
		gson = new Gson();
		eventProcessor = HBSEventProcessor.instance();
	}
	
	public void startup() {
		super.start();
		isRunning = true;
	}
	
	public void shutdown() throws InterruptedException {
		super.interrupt();
		super.join();
		isRunning = false;
	}
	
	@Override
	public void run() {
		int errCntr = 0;
		while (isRunning) {
			try {
				byte[] buffer = new byte[4096]; //Initialize a 4k buffer. Its quite big for our needs, but lets be safe!
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String line = new String(packet.getData());
				JsonReader reader = new JsonReader(new StringReader(line));
				reader.setLenient(true);
				HBSEvent hbe = gson.fromJson(reader, HBSEvent.class);
				eventProcessor.process(hbe);
				reader.close();
			} catch (Exception e) {
				if (!isRunning) {
					break;
				}
				LOG.log(Level.SEVERE, "Exception thrown in PluginEventListener thread for port " + listeningPort, e);
				errCntr++;
				if (errCntr >= MAX_ERRORS) {
					LOG.log(Level.SEVERE, "PluginEventListener thread for port " + listeningPort + " has reached the max allowed errors. It is exiting now");
					break;
				}
			}
		}
		LOG.info("PluginEventListener thread for port " + listeningPort + " is returning");
		return;
		/*
		long interval = 1; // every minute
		long lastUpdate = -1;
		while (true) {
			byte[] buffer = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			
			String line = new String(packet.getData());
			System.out.println(">" + line);
			long curTime = System.currentTimeMillis();
			long diffSecs = (curTime - lastUpdate) / 1000;
			if (diffSecs > interval || lastUpdate == -1) {
				System.out.println("**************** UPDATING DB - START");
				JsonReader reader = new JsonReader(new StringReader(line));
				reader.setLenient(true);
				HBEvent hbe = gson.fromJson(reader, HBEvent.class);

				// HBEvent hbe = gson.fromJson(line, HBEvent.class);
				// System.out.println(hbe.Realm);
				// System.out.println(hbe.Faction);
				// System.out.println(hbe.Name);
				// System.out.println(hbe.Class);
				System.out.println(hbe);
				System.out.println(hbe.getTimeToLevelRelative());
				System.out.println(hbe.getTimeToLevelDate().toString());
				
				Calendar now = Calendar.getInstance();
				long nowVal = now.getTimeInMillis();
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
				String nowStr = format1.format(now.getTime());
				
				BasicDBObject query = new BasicDBObject("name", hbe.Realm + "-"
						+ hbe.Faction + "-" + hbe.Name);
				DBObject documentBuilder = BasicDBObjectBuilder.start()
						.add("Class", hbe.Class)
						.add("Race", hbe.Race)
						.add("Level", hbe.Level)
						.add("NextLevelUpIn", hbe.getTimeToLevelRelative())
						.add("NextLevelUpOn", hbe.getTimeToLevelDate())
						.add("XPPerHour", hbe.XPPerHour)
						.add("CurrentEXP", hbe.CurrentEXP)
						.add("NextEXP", hbe.NextEXP)
						.add("Copper", hbe.Copper)
						.add("Silver", hbe.Silver)
						.add("Gold", hbe.Gold)
						.add("BagFreeSlots", hbe.BagFreeSlots)
						.add("Realm", hbe.Realm)
						.add("Faction", hbe.Faction)
						.add("Name", hbe.Name)
						.add("Zone", hbe.Zone)
						.add("SubZone", hbe.SubZone)
						
						.add("CastingSpellId", hbe.CastingSpellId)
						.add("IsAlive", hbe.IsAlive)
						.add("IsAutoAttacking", hbe.IsAutoAttacking)
						.add("IsCasting", hbe.IsCasting)
						.add("IsGhost", hbe.IsGhost)
						.add("IsMoving", hbe.IsMoving)
						.add("Heading", hbe.Heading)
						
						.add("UpdatedOn", nowStr)
						.add("UpdatedOnVal", nowVal)
						.get();
				profileOverviewCol.update(query,new BasicDBObject("$set", documentBuilder), true, false);

				lastUpdate = curTime;
				System.out.println("**************** UPDATING DB - FINISH");
			}
		}	*/
	
	}
}
