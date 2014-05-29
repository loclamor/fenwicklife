package com.m2dl.fenwicklife.engine;

import java.util.HashMap;
import java.util.Map;

import com.m2dl.fenwicklife.Position;

public class Field {
	public static final int MIN_SIZE_X = 10;
	public static final int MIN_SIZE_Y = 10;
	public static final int MAX_SIZE_X = 300;
	public static final int MAX_SIZE_Y = 300;
	public static final int MAX_CENTRAL_WALL_SIZE = 100;
	public static final int MIN_CENTRAL_WALL_SIZE = 1;
	
	
	private Map<Position, Tile> grid;
	private int firstCorridorY;
	private int secondCorridorY;
	private int sizeX;
	private int sizeY;
	private int centerWallXLeft;
	private int centerWallXRight;
	private Position storeAreaTopCorner;
	private Position storeAreaBottomCorner;
	private Position homeAreaTopCorner;
	private Position homeAreaBottomCorner;
	
	public Field(int sizeX, int sizeY, int centerWallSize, int firstCorridorY, int secondCorridorY, int storeHomeWidth, int storeHomeHeight) {
		checkInitFieldParameters(sizeX, sizeY, centerWallSize, firstCorridorY,
				secondCorridorY, storeHomeWidth, storeHomeHeight);
		
		this.firstCorridorY = firstCorridorY;
		this.secondCorridorY = secondCorridorY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.centerWallXLeft   = (sizeX - centerWallSize) / 2;
		this.centerWallXRight  = (sizeX + centerWallSize) / 2;
		this.grid = initGrid();
		// TODO : check coordiantes calculation
		this.storeAreaTopCorner = new Position(storeHomeWidth, (sizeY - storeHomeHeight) / 2);
		this.storeAreaBottomCorner = new Position(storeHomeWidth, sizeY - ((sizeY - storeHomeHeight) / 2));
		this.homeAreaTopCorner = new Position(sizeX - storeHomeWidth, (sizeY - storeHomeHeight) / 2);
		this.homeAreaBottomCorner = new Position(sizeX - storeHomeWidth, sizeY - ((sizeY - storeHomeHeight) / 2));
	}

	private void checkInitFieldParameters(int sizeX, int sizeY,
			int centerWallSize, int firstCorridorY, int secondCorridorY,
			int storeHomeWidth, int storeHomeHeight) {
		if(sizeX < MIN_SIZE_X || sizeX > MAX_SIZE_X) {
			throw new IllegalArgumentException("Field Size X invalid");
		}
		
		if(sizeY < MIN_SIZE_Y || sizeY > MAX_SIZE_Y) {
			throw new IllegalArgumentException("Field Size Y invalid");
		}
		
		if(centerWallSize < MIN_CENTRAL_WALL_SIZE || centerWallSize > MAX_CENTRAL_WALL_SIZE) {
			throw new IllegalArgumentException("Center wall size invalid");
		}
		
		if(firstCorridorY == secondCorridorY) {
			throw new IllegalArgumentException("Both corridors on same coordinate");
		}
		
		if(firstCorridorY < 0 || secondCorridorY < 0 || firstCorridorY > sizeY || secondCorridorY > sizeY) {
			throw new IllegalArgumentException("Invalid corridor coordinates");
		}
		
		if(storeHomeWidth > (sizeX - centerWallSize) / 2 || storeHomeWidth < 1) {
			throw new IllegalArgumentException("Invalid store and home areas width");
		}
		
		if(storeHomeHeight > sizeY || storeHomeHeight < 1) {
			throw new IllegalArgumentException("Invalid store and home areas height");
		}
	}
	
	private Map<Position, Tile> initGrid() {
		HashMap<Position, Tile> map = new HashMap<Position, Tile>();
		// first, fill all with empty
		for(int i = 0; i < this.sizeX; i++) {
			for(int j = 0; j < this.sizeY; j++) {
				map.put(new Position(i, j), new Tile(i, j, TileType.EMPTY));
			}
		}
		
		// central wall
		for(int i = this.centerWallXLeft; i < this.centerWallXRight; i++) {
			for(int j = 0; j < this.sizeY; j++) {
				map.get(new Position(i, j)).setType(TileType.WALL);
			}
		}
		
		// corridors
		for(int i = this.centerWallXLeft; i < this.centerWallXRight; i++) {
			map.get(new Position(i, firstCorridorY)).setType(TileType.WALL);
			map.get(new Position(i, secondCorridorY)).setType(TileType.WALL);
		}
		
		// home area
		for(int i = this.homeAreaTopCorner.getY(); i <= this.homeAreaBottomCorner.getY(); i++) {
			for(int j = 0; j <= this.homeAreaBottomCorner.getX(); j++) {
				map.get(new Position(j, i)).setType(TileType.HOME);
			}
		}
		
		// store area
		for(int i = this.storeAreaTopCorner.getY(); i <= this.storeAreaBottomCorner.getY(); i++) {
			for(int j = this.storeAreaTopCorner.getX(); j <= sizeX; j++) {
				map.get(new Position(j, i)).setType(TileType.STORE);
			}
		}
		
		return map;
	}

	public int getFirstCorridorY() {
		return firstCorridorY;
	}
	
	public Map<Position, Tile> getGrid() {
		return this.grid;
	}
	
	public int getSecondCorridorX() {
		return secondCorridorY;
	}
	
	/**
	 * Returns the surrounding og the position at the given coordinates, with default size = 3
	 */
	public Tile[][] getSurroundings(int x, int y) {
		return getSurroundings(x, y, 3);
	}

	public Tile[][] getSurroundings(int x, int y, int size) {
		Tile[][] tilesSubGrid = new Tile[7][7];
		for( int i = 0; i < (2 * size + 1); i++) {
			for( int j = 0; j < (2*size + 1); j++) {
				TileType type = getTileType(x-size+i, y-size+j);
				tilesSubGrid[i][j] = new Tile(x-size+i, y-size+j, type);
			}
		}
		return tilesSubGrid;
	}

	public TileType getTileType(int x, int y) {
		return getTileType(new Position(x, y));
	}
	
	public TileType getTileType(Position p) {
		TileType type = grid.get(p).getType();
		if(type == null) {
			// if outside : wall
			return TileType.WALL;
		}
		return grid.get(p).getType();
	}
	
	public boolean isObstacle(int x, int y) {
		return isObstacle(new Position(x, y));
	}
	
	public boolean isObstacle(Position p) {
		return grid.get(p).getType() != TileType.EMPTY;
	}

	public void setFirstCorridorY(int firstCorridorY) {
		if(firstCorridorY == secondCorridorY) {
			throw new IllegalArgumentException("Both corridors on same coordinate");
		}
		
		if(firstCorridorY < 0 || firstCorridorY > sizeY) {
			throw new IllegalArgumentException("Invalid first corridor coordinates");
		}
		
		this.firstCorridorY = firstCorridorY;
	}
	
	public void setSecondCorridorY(int secondCorridorY) {
		if(firstCorridorY == secondCorridorY) {
			throw new IllegalArgumentException("Both corridors on same coordinate");
		}
		
		if(secondCorridorY < 0 || secondCorridorY > sizeY) {
			throw new IllegalArgumentException("Invalid first corridor coordinates");
		}
		
		this.secondCorridorY = secondCorridorY;
	}
}
