package com.m2dl.fenwicklife.agent;

import com.m2dl.fenwicklife.engine.Tile;

public interface IEngineProxy {
	
	public boolean hello( Agent me );
	
	public boolean move( Agent me, int to_x, int to_y );

	public boolean takeBox( Agent me );
	
	public Tile[][] getSurroundings( int x, int y );
	public Tile[][] getSurroundings( int x, int y, int size );
	
	public boolean create( Agent me, int x, int y ) ;
	
	public void suicide( Agent me );

}
