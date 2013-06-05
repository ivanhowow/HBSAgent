package com.hbsensorsuite.hbsagent.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.hbsensorsuite.hbsagent.App;
import com.hbsensorsuite.hbsagent.core.MongoManager;

@SuppressWarnings("serial")
public class LoginScreen extends BaseFrame {
	private JTextField txtMongoHost;
	private JTextField txtMongoPort;
	private JTextField txtMongoUsername;
	private JTextField txtMongoPassword;
	private JTextField txtMongoDBName;
	private JLabel lblStatusText;
	private boolean authSuccess;
	private JPanel loginPanel;
	private JLabel topLabel;
	private JPanel mainPanel;

	public LoginScreen() {
		setSize(300, 280);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(new BorderLayout());
		
		topLabel = new JLabel("MongoDB Settings");
		mainPanel.add(topLabel, BorderLayout.NORTH);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setSize(300, 75);
		lblStatusText = new JLabel("  ");
		bottomPanel.add(lblStatusText);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		loginPanel = new JPanel();
		loginPanel.setLayout(new GridLayout(0, 2));
		
		loginPanel.add(new JLabel("Mongo Host:"));
		txtMongoHost = new JTextField();
		txtMongoHost.setText(App.USER_CFG.getProperty("mongo.host", ""));
		loginPanel.add(txtMongoHost);
		loginPanel.add(new JLabel("Mongo Port:"));
		txtMongoPort = new JTextField();
		txtMongoPort.setText(App.USER_CFG.getProperty("mongo.port", ""));
		loginPanel.add(txtMongoPort);
		loginPanel.add(new JLabel("Mongo Username:"));
		txtMongoUsername = new JTextField();
		txtMongoUsername.setText(App.USER_CFG.getProperty("mongo.username", ""));
		loginPanel.add(txtMongoUsername);
		loginPanel.add(new JLabel("Mongo Password:"));
		txtMongoPassword = new JTextField();
		txtMongoPassword.setText(App.USER_CFG.getProperty("mongo.password", ""));
		loginPanel.add(txtMongoPassword);
		loginPanel.add(new JLabel("Mongo DB Name:"));
		txtMongoDBName = new JTextField();
		txtMongoDBName.setText(App.USER_CFG.getProperty("mongo.dbname", ""));
		loginPanel.add(txtMongoDBName);

		JButton btnQuit = new JButton("Quit");
		
		btnQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		JButton btnConnect = new JButton("Connect");
		
		btnConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Save the settings to preferences file
				//Attempt to connect to Mongo
				connectToMongo();
			}
		});
		
		loginPanel.add(btnQuit);
		loginPanel.add(btnConnect);
		
		mainPanel.add(loginPanel, BorderLayout.CENTER);
		
		add(mainPanel);
	}
	
	private void connectToMongo() {
		App.USER_CFG.setProperty("mongo.host", txtMongoHost.getText());
		App.USER_CFG.setProperty("mongo.port", txtMongoPort.getText());
		App.USER_CFG.setProperty("mongo.username", txtMongoUsername.getText());
		App.USER_CFG.setProperty("mongo.password", txtMongoPassword.getText());
		App.USER_CFG.setProperty("mongo.dbname", txtMongoDBName.getText());
		App.saveConfig();
		disableFields();
		lblStatusText.setText("Connecting...");
		new Thread() {
			public void run() {
				try {
					authSuccess = MongoManager.instance().connect();
					if (!authSuccess) {
						lblStatusText.setText("Failed. Please check your settings");
						enableFields();
					} else {
						lblStatusText.setText("Connected");
						connected();
					}
				} catch (NumberFormatException | UnknownHostException e) {
					e.printStackTrace();
					authSuccess = false;
					lblStatusText.setText("Failed. " + e.getMessage());
					enableFields();
				}
			}
		}.start();
	}
	
	private void connected() {
		lblStatusText.setText("");
		topLabel.setText("Connected to MongoDB");
		JPanel connectedPanel = new JPanel();
		mainPanel.remove(loginPanel);
		mainPanel.add(connectedPanel, BorderLayout.CENTER);
	}
	
	private void enableFields() {
		txtMongoDBName.setEnabled(true);
		txtMongoHost.setEnabled(true);
		txtMongoPassword.setEnabled(true);
		txtMongoPort.setEnabled(true);
		txtMongoUsername.setEnabled(true);
	}
	
	private void disableFields() {
		txtMongoDBName.setEnabled(false);
		txtMongoHost.setEnabled(false);
		txtMongoPassword.setEnabled(false);
		txtMongoPort.setEnabled(false);
		txtMongoUsername.setEnabled(false);
	}
}
