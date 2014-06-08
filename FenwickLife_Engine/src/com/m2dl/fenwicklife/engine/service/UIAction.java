package com.m2dl.fenwicklife.engine.service;

import com.m2dl.fenwicklife.engine.Engine;

public class UIAction implements IUIAction {

	@Override
	public boolean pause() {
		return Engine.getInstance().setPauseMode(!Engine.getInstance().isInPauseMode());
	}

}
