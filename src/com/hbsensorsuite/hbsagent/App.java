package com.hbsensorsuite.hbsagent;

import java.awt.Image;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import com.hbsensorsuite.hbsagent.ui.LoginScreen;

public class App {
	public static String USER_HOME = System.getProperty("user.home") + File.separator + ".hbs" + File.separator;
	public static Properties USER_CFG = new Properties();
	public static List<Image> APP_ICON_LIST = new ArrayList<Image>();
	
	static {
		APP_ICON_LIST.add((new ImageIcon(App.class.getClassLoader().getResource("HBSAgent96.png"))).getImage());
		APP_ICON_LIST.add((new ImageIcon(App.class.getClassLoader().getResource("HBSAgent72.png"))).getImage());
		APP_ICON_LIST.add((new ImageIcon(App.class.getClassLoader().getResource("HBSAgent64.png"))).getImage());
		APP_ICON_LIST.add((new ImageIcon(App.class.getClassLoader().getResource("HBSAgent48.png"))).getImage());
		APP_ICON_LIST.add((new ImageIcon(App.class.getClassLoader().getResource("HBSAgent32.png"))).getImage());
		APP_ICON_LIST.add((new ImageIcon(App.class.getClassLoader().getResource("HBSAgent24.png"))).getImage());
		APP_ICON_LIST.add((new ImageIcon(App.class.getClassLoader().getResource("HBSAgent16.png"))).getImage());
		File userHomeDir = new File(App.USER_HOME);
		if (!userHomeDir.exists() || !userHomeDir.isDirectory()) {
			userHomeDir.mkdirs();
		}
		File agentCfgFile = new File(userHomeDir, "HBSAgent.cfg");
		if (!agentCfgFile.exists()) {
			try {
				USER_CFG.store(new FileWriter(agentCfgFile), "Initial creation");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		} else {
			try {
				USER_CFG.load(new FileReader(agentCfgFile));
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	public static void saveConfig() {
		File userHomeDir = new File(App.USER_HOME);
		File agentCfgFile = new File(userHomeDir, "HBSAgent.cfg");
		try {
			USER_CFG.store(new FileWriter(agentCfgFile), "Edit by Login Screen");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginScreen loginScreen = new LoginScreen();
                loginScreen.setVisible(true);
            }
        });
		
		//Start a thread for PluginEventListener
		//Whenever an event is received, the listener checks if an existing HBInstance exists
		//If so, it passes the event to the instance, if not it creates a new instance
	}

}
