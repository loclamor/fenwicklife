package com.m2dl.fenwicklife.agent;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import com.m2dl.fenwicklife.xmlrpc.consumer.ConsumerImpl;
import com.m2dl.fenwicklife.xmlrpc.consumer.IConsumer;

public class AgentMain {
	
	// Engine server adress
	private static String serverAddress = "127.0.0.1";
	// engine server port
	private static Integer serverPort = 8081;
	// consumer to do xml-rpc request
	private static IConsumer consumer;
	// Timer to refresh informations 
	private static Timer timer;
	// Agent
	private static Agent agent;
	// Agent execution speed (in ms)
	private static int agentExecSpeed = 1000;
		
	public static void main(String[] args) {
		// Get params for 
		if (args.length == 1) {
			serverAddress = args[0];
		}
		else if (args.length == 2) {
			serverAddress = args[0];
			serverPort = new Integer(args[1]).intValue();
		}
		else if (args.length >= 3) {
			serverAddress = args[0];
			serverPort = new Integer(args[1]).intValue();
			agentExecSpeed = new Integer(args[2]).intValue();
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
		timer.scheduleAtFixedRate(task, 0, agentExecSpeed);
	}

	private static void agentAction() {
		agent.perceive();
		agent.decide();
		agent.act();
	}
}
