package com.m2dl.fenwicklife.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.m2dl.fenwicklife.Position;

public class Field {
	private Map<Position, TileType> grid;
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
		return getTileType(p) != TileType.EMPTY;
	}
	
	public TileType getTileType(int x, int y) {
		return getTileType(new Position(x, y));
	}
	
	public TileType getTileType(Position p) {
		if(p.getX() == 0 || p.getX() == sizeX - 1 || p.getY() == 0 || p.getY() == sizeY - 1) {
			return TileType.WALL;
		}
		else if(p.getX() >= centerWallXLeft && p.getX() <= centerWallXRight && p.getY() != firstCorridorY && p.getY() != secondCorridorY) {
			return TileType.WALL;
		}
		if(grid.containsKey(p)) {
			return grid.get(p);
		}
		return TileType.EMPTY;
	}
	
	public Map<Position, TileType> getFieldState() {
		Map<Position, TileType> fieldState = new HashMap<Position, TileType>();
		for(int i = 0; i < sizeX; i++) {
			for(int j = 0; j < sizeY; j++) {
				fieldState.put(new Position(i,  j), getTileType(i, j));
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
		this.secondCorridorY = secondCorridorY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.centerWallXLeft   = sizeX / 2 - centerWallSize;
		this.centerWallXRight  = sizeX / 2 + centerWallSize;
		this.grid = new HashMap<Position, TileType>();
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
		// TODO Auto-generated method stub
		return null;
	}
}
