package com.m2dl.fenwicklife.agent;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class AgentMain {
	public static void main(String[] args) {
		// Get params for 
		if (args.length == 1) {
			serverAdress = args[0];
		}
		if (args.length >= 2) {
			serverAdress = args[0];
			serverPort = new Integer(args[1]).intValue();
		}

		// Initialize the consumer
		consumer = new ConsumerImpl();
		
		// Initialize the windows
		JFrame window = new JFrame();
	    window.setVisible(true);
	    window.setSize(600, 400);
	    window.setTitle("Fenwick Life UI");
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // Init the panel to display drawables
	    panel = new ScenePanel(1, 1);
	    window.add(panel);
	    
	    // Launch the timer to get current state from engine server
	    refreshdisplay();   
	    TimerTask task = new TimerTask()
		{
			@Override
			public void run() 
			{
				// Refresh the UI
				refreshdisplay();
			}	
		};
		timer = new Timer();
		timer.scheduleAtFixedRate(task, 0, 1000);
	}
}
