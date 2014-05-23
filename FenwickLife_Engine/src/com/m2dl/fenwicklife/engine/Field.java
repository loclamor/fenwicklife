package com.m2dl.fenwicklife.engine;

import java.util.HashMap;
import java.util.Map;

import com.m2dl.fenwicklife.Position;

public class Field {
	private Map<Position, Tile> grid;
	private int firstCorridorY;
	private int secondCorridorY;
	private int sizeX;
	private int sizeY;
	private int centerWallXLeft;
	private int centerWallXRight;
	
	public boolean isObstacle(int x, int y) {
		return isObstacle(new Position(x, y));
	}
	
	public boolean isObstacle(Position p) {
		return grid.get(p).getType() != TileType.EMPTY;
	}
	
	public TileType getTileType(int x, int y) {
		return getTileType(new Position(x, y));
	}
	
	public TileType getTileType(Position p) {
		// if outside : wall
		if(p.getX() == 0 || p.getX() == sizeX - 1 || p.getY() == 0 || p.getY() == sizeY - 1) {
			return TileType.WALL;
			// Should not be needed
//		} else if(p.getX() >= centerWallXLeft && p.getX() <= centerWallXRight && p.getY() != firstCorridorY && p.getY() != secondCorridorY) {
//			return TileType.WALL;
		} if(grid.containsKey(p)) {
			return grid.get(p).getType();
		}
		// TODO: should crash
		System.err.println("Error : Tile not Found");
		return TileType.EMPTY;
	}
	
	public Map<Position, Tile> getGrid() {
		return this.grid;
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
		this.secondCorridorY = secondCorridorY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.centerWallXLeft   = sizeX / 2 - centerWallSize;
		this.centerWallXRight  = sizeX / 2 + centerWallSize;
		this.grid = new HashMap<Position, Tile>();
	}

	public int getFirstCorridorY() {
		return firstCorridorY;
	}
	public void setFirstCorridorY(int firstCorridorY) {
		this.firstCorridorY = firstCorridorY;
	}
	public int getSecondCorridorX() {
		return secondCorridorY;
	}
	public void setSecondCorridorX(int secondCorridorX) {
		this.secondCorridorY = secondCorridorX;
	}

	public Tile[][] getSurroundings(int x, int y) {
		return getSurroundings(x, y, 3);
	}
	
	public Tile[][] getSurroundings(int x, int y, int size) {
		Tile[][] tilesSubGrid = new Tile[7][7];
		for( int i = 0; i < 7; i++) {
			for( int j = 0; j < 7; j++) {
				TileType type = getTileType(x-size+i, y-size+j);
				tilesSubGrid[i][j] = new Tile(x-size+i, y-size+j, type);
			}
		}
		return tilesSubGrid;
	}
}
