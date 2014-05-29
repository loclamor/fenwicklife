package com.m2dl.fenwicklife.agent;

import com.m2dl.fenwicklife.engine.Tile;

public interface IEngineProxy {
	
	/**
	 * Ask the engine to spawn a new agent (me) at {@link Agent}'s position
	 * @param me, an {@link Agent}
	 * @return true if the engine as saved the new agent, false if it failed
	 */
	public boolean hello( Agent me );
	
	/**
	 * Ask the engine to move the me {@link Agent} at the new position
	 * @param me, the {@link Agent}
	 * @param to_x new x position
	 * @param to_y new y position
	 * @return true if the engine as saved the new location and accept the move, false otherwise
	 */
	public boolean move( Agent me, int to_x, int to_y );

	/**
	 * ask the server to take the box at {@link Agent} position
	 * @param me, the {@link Agent}
	 * @return true if the engine accept the {@link Agent} to take the box, false otherwise
	 */
	public boolean takeBox( Agent me );
	
	/**
	 * Retrieve tiles surrounding the {@link Agent}
	 * @param me, the {@link Agent}
	 * @return an array of tile
	 */
	public Tile[][] getSurroundings( Agent me );
	
	/**
	 * Retrieve tiles surrounding the {@link Agent} in the size rayon
	 * @param me, the {@link Agent}
	 * @param size, the size to surround
	 * @return an array of tile
	 */
	public Tile[][] getSurroundings( Agent me, int size );
	
	/**
	 * Ask the server that {@link Agent} me to create a new {@link Agent} in x y position
	 * @param me, the {@link Agent} which want to create a new agent
	 * @param x the new {@link Agent} x position
	 * @param y the new {@link Agent} y position
	 * @return true if the engine accept the new {@link Agent} creation, false otherwise
	 */
	public boolean create( Agent me, int x, int y ) ;
	
	/**
	 * Inform the engine that the me {@link Agent} as killed himself
	 * @param me, the feu {@link Agent}
	 */
	public void suicide( Agent me );

}
