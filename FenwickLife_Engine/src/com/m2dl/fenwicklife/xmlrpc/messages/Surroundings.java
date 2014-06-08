package com.m2dl.fenwicklife.xmlrpc.messages;

import java.io.Serializable;

import com.m2dl.fenwicklife.agent.AgentDecision;
import com.m2dl.fenwicklife.engine.Tile;

public class Surroundings implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Tile[][] tileArray = null;
	
	/**
	 * tileArray has to be a scare (x.length == y.length) with a center Tile (x.length % 2 == 1)
	 * @param tileArray
	 */
	public void setTileArray( Tile[][] tileArray ) {
		//TODO verify preconditions on tileArray
		this.tileArray = tileArray;
	}
	
	public Tile[][] getTileArray() {
		return this.tileArray;
	}
	
	public Tile[][] getSurroundings( int length ) {
		if( length > getSurroundingsSize() ) {
			length = getSurroundingsSize();
		}
		int diff = getSurroundingsSize() - length;
		int size = length*2+1;
		Tile[][] surrTiles = new Tile[size][size];
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				surrTiles[i][j] = tileArray[diff+i][diff+j];
			}
		}
		
		return surrTiles;
	}
	
	/**
	 * this is the length of view from the middle
	 * @return
	 */
	public int getSurroundingsSize() {
		return (tileArray.length - 1) / 2;
	}
	
	/**
	 * @return the {@link Tile} in the center of the surroundings (the position of the agent) 
	 */
	public Tile getLocalTile() {
		return tileArray[getSurroundingsSize()][getSurroundingsSize()];//F*ck of the f*cking +1
	}
	
	public Tile getTileInDirection(AgentDecision direction) {
		Tile[][] surrondings = getSurroundings(1);
		Tile tileToReturn = surrondings[1][1];
		switch(direction) {
			case NORTH :
				tileToReturn = surrondings[1][0];
				break;
			case WEST : 
				tileToReturn = surrondings[0][1];
				break;
			case SOUTH :
				tileToReturn = surrondings[1][2];
				break;
			case EAST :
				tileToReturn = surrondings[2][1];
				break;
		}
//		System.out.println("Try to get a " + tileToReturn.getTileType() + " tile in " + direction.toString());
//		System.out.println("Tile in NORTH : " + surrondings[1][0].getTileType());
//		System.out.println("Tile in SOUTH : " + surrondings[1][2].getTileType());
//		System.out.println("Tile in EAST : " + surrondings[2][1].getTileType());
//		System.out.println("Tile in WEST : " + surrondings[0][1].getTileType());
		return tileToReturn;
	}
}
