package com.m2dl.fenwicklife.engine;

import java.util.ArrayList;
import java.util.List;

public class Field {
	private int firstCorridorY;
	private int secondCorridorX;
	private int sizeX;
	private int sizeY;
	private int centerWallXLeft;
	private int centerWallXRight;
	
	public List<Tile> getFieldState() {
		List<Tile> fieldState = new ArrayList<Tile>();
		for(int i = 0; i < sizeX; i++) {
			for(int j = 0; j < sizeY; j++) {
				if(i == 0 || i == sizeX - 1 || j == 0 || j == sizeY - 1) {
					fieldState.add(new Tile(i, j, TileType.WALL));
				}
				else if(i >= centerWallXLeft && i <= centerWallXRight && j != firstCorridorY && j != secondCorridorX) {
					fieldState.add(new Tile(i, j, TileType.WALL));
				}
			}
		}
		return fieldState;
	}

	public Field(int sizeX, int sizeY, int centerWallSize, int firstCorridorY, int secondCorridorY) {
		super();
		
		if(firstCorridorY == secondCorridorY) {
			throw new RuntimeException("Both corridors on same coordinate");
		}
		
		if(firstCorridorY < 0 || secondCorridorY < 0 || firstCorridorY > sizeY || secondCorridorY > sizeY) {
			throw new RuntimeException("Invalid corridor coordinates");
		}
		
		this.firstCorridorY = firstCorridorY;
		this.secondCorridorX = secondCorridorY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.centerWallXLeft   = sizeX / 2 - centerWallSize;
		this.centerWallXRight  = sizeX / 2 + centerWallSize;

		
	}
	
	public int getFirstCorridorY() {
		return firstCorridorY;
	}
	public void setFirstCorridorY(int firstCorridorY) {
		this.firstCorridorY = firstCorridorY;
	}
	public int getSecondCorridorX() {
		return secondCorridorX;
	}
	public void setSecondCorridorX(int secondCorridorX) {
		this.secondCorridorX = secondCorridorX;
	}
}
