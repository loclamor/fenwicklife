package com.m2dl.fenwicklife.engine.service;

import com.m2dl.fenwicklife.engine.Engine;

public class UIAction implements IUIAction {

	@Override
	public boolean pause() {
		return Engine.getInstance().setPauseMode(!Engine.getInstance().isInPauseMode());
	}

	@Override
	public boolean speedUp() {
		Engine.getInstance().increaseSpeed();
		return true;
	}

	@Override
	public boolean speedDown() {
		Engine.getInstance().decreaseSpeed();
		return true;
	}
	
	public boolean nextStep() {
		Engine.getInstance().nextStep();
		return true;
	}

}
