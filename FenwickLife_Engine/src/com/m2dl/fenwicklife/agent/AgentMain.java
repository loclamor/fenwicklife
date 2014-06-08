package com.m2dl.fenwicklife.agent;

import java.util.ArrayList;
import java.util.List;
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
	private static List<Agent> agentList;
	// Agent execution speed (in ms)
	private static int agentExecSpeed = 50;
	// Number of agents to create
	private static int numberOfAgents = 10;
		
	public static void main(String[] args) {
		// Get params for 
		if (args.length == 1) {
			serverAddress = args[0];
		}
		else if (args.length == 2) {
			serverAddress = args[0];
			serverPort = new Integer(args[1]).intValue();
		}
		else if (args.length == 3) {
			serverAddress = args[0];
			serverPort = new Integer(args[1]).intValue();
			agentExecSpeed = new Integer(args[2]).intValue();
		}
		else if (args.length >= 4) {
			serverAddress = args[0];
			serverPort = new Integer(args[1]).intValue();
			agentExecSpeed = new Integer(args[2]).intValue();
			numberOfAgents = new Integer(args[3]).intValue();
			if( numberOfAgents < 1 ) { numberOfAgents = 1; }
		}
		
		agentList = new ArrayList<Agent>();
		
		// Initialize the EngineProxy with server address and port
		EngineProxy.getInstance(serverAddress, serverPort);
		
		// Initialize the agent
		for(int i=0; i<numberOfAgents; i++) {
			Agent agent = new Agent(i, 0);
			//Add the agent to the list if Engine has take him
			if( EngineProxy.getInstance().hello(agent) ) {
				agentList.add( agent );
			}
			
		}
		
		// Launch the timer to get current state from engine server
	    agentsAction();   
	    TimerTask task = new TimerTask()
		{
			@Override
			public void run() 
			{
				// Refresh the UI
				agentsAction();
			}	
		};
		timer = new Timer();
		timer.schedule(task, 0, agentExecSpeed);//AtFixedRate
	}

	private static void agentsAction() {
		//Make the awesome for all agents !
		for ( Agent agent : agentList ) {
			agent.perceive();
			agent.decide();
			agent.act();
			if(agent.isDead() && timer != null) {
				timer.cancel();
				timer = null;
			}
		}
	}
}
