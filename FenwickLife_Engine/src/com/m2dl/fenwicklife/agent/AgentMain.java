package com.m2dl.fenwicklife.agent;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import com.m2dl.fenwicklife.xmlrpc.consumer.ConsumerImpl;
import com.m2dl.fenwicklife.xmlrpc.consumer.IConsumer;

public class AgentMain {
	
	// Engine server adress
	private static String serverAdress = "127.0.0.1";
	// engine server port
	private static Integer serverPort = 8081;
	// consumer to do xml-rpc request
	private static IConsumer consumer;
	// Timer to refresh informations 
	private static Timer timer;
	// Agent
	private static Agent agent;
		
	public static void main(String[] args) {
		// Get params for 
		if (args.length == 1) {
			serverAdress = args[0];
		}
		if (args.length >= 2) {
			serverAdress = args[0];
			serverPort = new Integer(args[1]).intValue();
		}
		// Initialize the agent
		agent = new Agent(0, 0);
		// Initialize the consumer
		consumer = new ConsumerImpl();
		
		// Launch the timer to get current state from engine server
	    agentAction();   
	    TimerTask task = new TimerTask()
		{
			@Override
			public void run() 
			{
				// Refresh the UI
				agentAction();
			}	
		};
		timer = new Timer();
		timer.scheduleAtFixedRate(task, 0, 1000);
	}

	private static void agentAction() {
		// TODO Auto-generated method stub
		
	}
}
