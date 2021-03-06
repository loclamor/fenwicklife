package com.m2dl.fenwicklife.engine.service;

import com.m2dl.fenwicklife.Position;
import com.m2dl.fenwicklife.xmlrpc.messages.EngineState;
import com.m2dl.fenwicklife.xmlrpc.messages.SimpleAgent;
import com.m2dl.fenwicklife.xmlrpc.messages.Surroundings;

public interface IAgentAction {
	
	public boolean hello( SimpleAgent me );
	
	public boolean move( SimpleAgent me, int to_x, int to_y );

	public boolean takeBox( SimpleAgent me );
	
	public boolean dropBox( SimpleAgent me );
	
	public Surroundings getSurroundings( SimpleAgent me );
	public Surroundings getSurroundings( SimpleAgent me, int size );
	
	public boolean create( SimpleAgent me, int x, int y ) ;
	
	public boolean suicide( SimpleAgent me );
	
	public Position getStoreAreaTopCorner( boolean please );

	public Position getStoreAreaBottomCorner( boolean please );

	public Position getHomeAreaTopCorner( boolean please );

	public Position getHomeAreaBottomCorner( boolean please );
	
	public EngineState getEngineState();
	
}
