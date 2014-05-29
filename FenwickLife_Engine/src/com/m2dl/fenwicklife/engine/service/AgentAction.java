package com.m2dl.fenwicklife.engine.service;

import com.m2dl.fenwicklife.agent.Agent;
import com.m2dl.fenwicklife.engine.Engine;
import com.m2dl.fenwicklife.engine.EngineMain;
import com.m2dl.fenwicklife.engine.Tile;

public class AgentAction implements IAgentAction{

	@Override
	public boolean hello(Agent me) {
		return EngineMain.engine.hello(me);
	}

	@Override
	public boolean move(Agent me, int to_x, int to_y) {
		return EngineMain.engine.move(me, to_x, to_y);
	}

	@Override
	public boolean takeBox(Agent me) {
		return EngineMain.engine.takeBox(me);
	}

	@Override
	public Tile[][] getSurroundings(Agent me) {
		return EngineMain.engine.getSurroundings(me, 3);
	}

	@Override
	public Tile[][] getSurroundings(Agent me, int size) {
		return EngineMain.engine.getSurroundings(me, size);
	}

	@Override
	public boolean create(Agent me, int x, int y) {
		return EngineMain.engine.create(me, x, y);
	}

	@Override
	public void suicide(Agent me) {
		EngineMain.engine.suicide(me);		
	}

}
