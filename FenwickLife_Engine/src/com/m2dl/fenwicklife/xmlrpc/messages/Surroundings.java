package com.m2dl.fenwicklife.xmlrpc.messages;

import java.io.Serializable;

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
	
	public Tile getLocalTile() {
		return tileArray[getSurroundingsSize()+1][getSurroundingsSize()+1];
	}
}
