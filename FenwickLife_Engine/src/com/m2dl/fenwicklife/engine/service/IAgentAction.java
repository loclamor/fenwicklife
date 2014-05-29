package com.m2dl.fenwicklife.engine.service;

import com.m2dl.fenwicklife.agent.Agent;
import com.m2dl.fenwicklife.engine.Tile;

public interface IAgentAction {
	
	public boolean hello( Agent me );
	
	public boolean move( Agent me, int to_x, int to_y );

	public boolean takeBox( Agent me );
	
	public Tile[][] getSurroundings( Agent me );
	public Tile[][] getSurroundings( Agent me, int size );
	
	public boolean create( Agent me, int x, int y ) ;
	
	public void suicide( Agent me );
}
