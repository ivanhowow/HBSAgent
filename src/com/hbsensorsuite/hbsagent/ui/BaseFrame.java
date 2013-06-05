package com.hbsensorsuite.hbsagent.ui;

import javax.swing.JFrame;

import com.hbsensorsuite.hbsagent.App;

@SuppressWarnings("serial")
public abstract class BaseFrame extends JFrame {
	public BaseFrame() {
		setTitle("HB Sensor Agent");
		setResizable(false);
		setIconImages(App.APP_ICON_LIST);
		setLocationRelativeTo(null);
	}
}
