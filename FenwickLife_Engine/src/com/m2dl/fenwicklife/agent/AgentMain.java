package com.m2dl.fenwicklife.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.m2dl.fenwicklife.xmlrpc.messages.EngineState;

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

	private static boolean displayLogs = true;
	
	private static EngineState eState;
	
	private static int currentStep = 0;

	private static TimerTask task = null;

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out
					.println("Options are : [ serverAddress [, serverPort [, numberOfAgents [, displayLogs ]]]]");
			System.out.println("\tserverAddress :\t String, optional, default "
					+ serverAddress);
			System.out.println("\tserverPort :\t int, optional, default "
					+ serverPort);
			System.out.println("\tnumberOfAgents : int, optional, default "
					+ numberOfAgents);
			System.out
					.println("\tdisplayLogs :\t int [0=false,1=true], optional, default "
							+ (displayLogs ? "1" : "0"));
		}

		// Get params for
		if (args.length >= 1) {
			serverAddress = args[0];
		}
		if (args.length >= 2) {
			serverPort = new Integer(args[1]).intValue();
		}
		if (args.length >= 3) {
			numberOfAgents = new Integer(args[2]).intValue();
			if (numberOfAgents < 1) {
				numberOfAgents = 1;
			}
		}
		if (args.length >= 4) {
			displayLogs = (new Integer(args[3]).intValue()) == 1;
		}

		agentList = new ArrayList<Agent>();

		// Initialize the EngineProxy with server address and port
		EngineProxy.getInstance(serverAddress, serverPort);
		
		eState = EngineProxy.getInstance().getEngineState();
		
		agentExecSpeed = eState.getSpeed();

		// Initialize the agent
		for (int i = 0; i < numberOfAgents; i++) {
			Agent agent = new Agent(i, 0, uniqid());
			// Add the agent to the list if Engine has take him
			if (EngineProxy.getInstance().hello(agent)) {
				agentList.add(agent);
				System.out.println("Agent " + agent.getId() + " launch in " + agent.getX() + "-"+agent.getY());
			}
			else {
				System.err.println("Engine has refused Agent's " + agent.getId() + " born on " + agent.getX() + "-"+agent.getY());
			}

		}

		// Launch the timer to get current state from engine server
		if (!eState.isInPauseMode()) {
			currentStep = eState.getCurrentStep();
			agentsAction();
		}
		else {
			currentStep = eState.getCurrentStep() -1;
		}
		task = new CustomTimerTask();
		timer = new Timer();
		timer.schedule(task, 0, agentExecSpeed);// AtFixedRate
	}

	private static void agentsAction() {
		// Make the awesome for all agents !
		for (Agent agent : agentList) {
			agent.perceive();
			agent.decide();
			agent.act();
			if(displayLogs) {
				System.out.println(agent.toString());
			}
			if (agent.isDead() && timer != null) {
				timer.cancel();
				timer = null;
			}
		}
	}

	private static String uniqid() {
		return String
				.valueOf(1000000 + (int) (Math.random() * ((9999999 - 1000000) + 1)));
	}

	private static class CustomTimerTask extends TimerTask {

		@Override
		public void run() {
			eState = EngineProxy.getInstance().getEngineState();
			if (!eState.isInPauseMode() || (eState.isInPauseMode() && eState.getCurrentStep() > currentStep) ) {
				AgentMain.agentsAction();
				
				if(eState.isInPauseMode() && eState.getCurrentStep() > currentStep) {
					currentStep++;
				}
				else {
					currentStep = eState.getCurrentStep();
				}
				
				if (eState.getSpeed() != AgentMain.agentExecSpeed) {
					AgentMain.agentExecSpeed = eState.getSpeed();
					// reschedule
					AgentMain.timer.schedule(new CustomTimerTask(), 0,
							AgentMain.agentExecSpeed);
					this.cancel();
				}
			}
		}
	}
}
