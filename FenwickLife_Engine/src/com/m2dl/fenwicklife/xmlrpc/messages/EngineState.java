package com.m2dl.fenwicklife.xmlrpc.messages;

import java.io.Serializable;

import com.m2dl.fenwicklife.engine.Engine;

public class EngineState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isInPauseMode;
	private int speed; // time between each action in ms
	
	private boolean isInStepMode;
	private int currentStep;
	
	public EngineState( Engine e ) {
		this.isInPauseMode = e.isInPauseMode();
		this.speed = e.getSpeed();
		this.isInStepMode = e.isInStepMode();
		this.currentStep = e.getCurrentStep();
	}
	
	public boolean isInPauseMode() {
		return isInPauseMode;
	}

	public int getSpeed() {
		return speed;
	}

	public boolean isInStepMode() {
		return isInStepMode;
	}

	public int getCurrentStep() {
		return currentStep;
	}



	
}
