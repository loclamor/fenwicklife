package com.m2dl.fenwicklife.agent;

import java.util.Timer;
import java.util.TimerTask;

public class AgentMain {
	
	// Engine server adress
	private static String serverAddress = "127.0.0.1";
	// engine server port
	private static Integer serverPort = 8081;
	// Timer to refresh informations 
	private static Timer timer;
	// Agent
	private static Agent agent;
	// Agent execution speed (in ms)
	private static int agentExecSpeed = 300;
		
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
		
		// Initialize the EngineProxy with server address and port
		EngineProxy.getInstance(serverAddress, serverPort);
		
		// Initialize the agent
		agent = new Agent(0, 0);
		
		EngineProxy.getInstance().hello(agent);
		
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
		timer.schedule(task, 0, agentExecSpeed);//AtFixedRate
	}

	private static void agentAction() {
		agent.perceive();
		agent.decide();
		agent.act();
		if(agent.isDead() && timer != null) {
			timer.cancel();
			timer = null;
			return;
		}
	}
}
