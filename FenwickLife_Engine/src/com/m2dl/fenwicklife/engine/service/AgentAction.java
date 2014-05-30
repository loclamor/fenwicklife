package com.m2dl.fenwicklife.engine.service;

import com.m2dl.fenwicklife.agent.Agent;
import com.m2dl.fenwicklife.engine.Engine;
import com.m2dl.fenwicklife.engine.Tile;
import com.m2dl.fenwicklife.xmlrpc.messages.TileArrayMessage;

public class AgentAction implements IAgentAction{

	@Override
	public boolean hello(Agent me) {
		return Engine.getInstance().hello(me);
	}

	@Override
	public boolean move(Agent me, int to_x, int to_y) {
		return Engine.getInstance().move(me, to_x, to_y);
	}

	@Override
	public boolean takeBox(Agent me) {
		return Engine.getInstance().takeBox(me);
	}

	@Override
	public TileArrayMessage getSurroundings(Agent me) {
		return getSurroundings(me, 3); 
	}

	@Override
	public TileArrayMessage getSurroundings(Agent me, int size) {
		Tile[][] tiles = Engine.getInstance().getSurroundings(me, size);
		TileArrayMessage m = new TileArrayMessage();
		m.tileArray = tiles;
		return m;
	}

	@Override
	public boolean create(Agent me, int x, int y) {
		return Engine.getInstance().create(me, x, y);
	}

	@Override
	public void suicide(Agent me) {
		Engine.getInstance().suicide(me);		
	}

}
