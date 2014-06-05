package com.m2dl.fenwicklife.engine.service;

import com.m2dl.fenwicklife.Position;
import com.m2dl.fenwicklife.agent.Agent;
import com.m2dl.fenwicklife.engine.Engine;
import com.m2dl.fenwicklife.engine.Tile;
import com.m2dl.fenwicklife.xmlrpc.messages.SimpleAgent;
import com.m2dl.fenwicklife.xmlrpc.messages.Surroundings;

public class AgentAction implements IAgentAction{

	@Override
	public boolean hello(SimpleAgent me) {
		return Engine.getInstance().hello(me);
	}

	@Override
	public boolean move(SimpleAgent me, int to_x, int to_y) {
		return Engine.getInstance().move(me, to_x, to_y);
	}

	@Override
	public boolean takeBox(SimpleAgent me) {
		return Engine.getInstance().takeBox(me);
	}

	@Override
	public Surroundings getSurroundings(SimpleAgent me) {
		return getSurroundings(me, 3); 
	}

	@Override
	public Surroundings getSurroundings(SimpleAgent me, int size) {
		return Engine.getInstance().getSurroundings(me, size);
	}
	
	public Position getStoreAreaTopCorner( boolean please ) {
		return Engine.getInstance().getStoreAreaTopCorner(please);
	}

	public Position getStoreAreaBottomCorner( boolean please ) {
		return Engine.getInstance().getStoreAreaBottomCorner(please);
	}

	public Position getHomeAreaTopCorner( boolean please ) {
		return Engine.getInstance().getHomeAreaTopCorner(please);
	}

	public Position getHomeAreaBottomCorner( boolean please ) {
		return Engine.getInstance().getHomeAreaBottomCorner(please);
	}

	@Override
	public boolean create(SimpleAgent me, int x, int y) {
		return Engine.getInstance().create(me, x, y);
	}

	@Override
	public void suicide(SimpleAgent me) {
		Engine.getInstance().suicide(me);		
	}

}
