package com.m2dl.fenwicklife.engine;

import java.util.HashMap;
import java.util.Map;

import com.m2dl.fenwicklife.Position;
import com.m2dl.fenwicklife.agent.Agent;
import com.m2dl.fenwicklife.xmlrpc.messages.SimpleAgent;
import com.m2dl.fenwicklife.xmlrpc.messages.Surroundings;

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
		
		// TODO : check coordiantes calculation
		this.storeAreaTopCorner = new Position(storeHomeWidth -1, (sizeY - storeHomeHeight) / 2); //width - 1 because field start at 0
		this.storeAreaBottomCorner = new Position(storeHomeWidth -1, sizeY - ((sizeY - storeHomeHeight) / 2));
		this.homeAreaTopCorner = new Position(sizeX - storeHomeWidth, (sizeY - storeHomeHeight) / 2);
		this.homeAreaBottomCorner = new Position(sizeX - storeHomeWidth, sizeY - ((sizeY - storeHomeHeight) / 2));
	
		this.grid = initGrid();
		printField();
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
				map.put(new Position(i, j), new Empty(i, j));
			}
		}
		
		// central wall
		for(int i = this.centerWallXLeft; i < this.centerWallXRight; i++) {
			for(int j = 0; j < this.sizeY; j++) {
				map.put(new Position(i, j), new Wall(i, j));
			}
		}
		
		// corridors
		for(int i = this.centerWallXLeft; i < this.centerWallXRight; i++) {
			map.put(new Position(i, firstCorridorY), new Empty(i, firstCorridorY));
			map.put(new Position(i, secondCorridorY), new Empty(i, secondCorridorY));
		}
		
		// home area
		for(int i = this.homeAreaTopCorner.getY(); i <= this.homeAreaBottomCorner.getY(); i++) {
			for(int j = sizeX -1; j >= this.homeAreaBottomCorner.getX(); j--) {
				map.put(new Position(j, i), new Home(j,i));
			}
		}
		
		// store area
		for(int i = this.storeAreaTopCorner.getY(); i <= this.storeAreaBottomCorner.getY(); i++) {
			for(int j = 0; j <= this.storeAreaTopCorner.getX(); j++) {
				map.put(new Position(j, i), new Storage(j, i));
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
	
	public Tile getTile( int x, int y ) {
		return getTile( new Position( x, y ) );
	}
	
	public Tile getTile( Position pos ) {
		if(grid.containsKey(pos)) {
			Tile t = grid.get(pos);
			if(t == null) {
				// if outside : wall
				return new Wall( pos );
			}
			return grid.get(pos);
		}
		return new Wall( pos ); 
	}
	
	public void setTile( int x, int y, Tile t ) {
		setTile( new Position(x,y), t );
	}
	
	public void setTile( Position p, Tile t ) {
		grid.put( p, t);
	}
	
	public void setAgent( int x, int y, SimpleAgent a ) {
		setAgent( new Position(x,y), a );
	}
	
	public void setAgent( Position p, SimpleAgent a ) {
		Tile t = getTile( p );
		t.setAgent( a );
		setTile( p, t);
	}
	
	public void removeAgent( int x, int y ) {
		removeAgent( new Position(x,y) );
	}
	
	public void removeAgent( Position p ) {
		Tile t = getTile( p );
		t.removeAgent();
		setTile( p, t);
	}
	
	public void setBox(int x,int y) {
		setBox( new Position(x, y) );
	}
	
	public void setBox( Position p ) {
		Tile t = getTile( p );
		t.setBox();
		setTile( p, t);
	}
	
	public void removeBox(int x,int y) {
		removeBox( new Position(x, y) );
	}
	
	public void removeBox( Position p ) {
		Tile t = getTile( p );
		t.removeBox();
		setTile( p, t);
	}
	
	/**
	 * Returns the surrounding og the position at the given coordinates, with default size = 3
	 */
	public Surroundings getSurroundings(int x, int y) {
		return getSurroundings(x, y, 3);
	}

	public Surroundings getSurroundings(int x, int y, int size) {
		Tile[][] tilesSubGrid = new Tile[7][7];
		for( int i = 0; i < (2 * size + 1); i++) {
			for( int j = 0; j < (2*size + 1); j++) {
				tilesSubGrid[i][j] = getTile(x-size+i, y-size+j);
			}
		}
		Surroundings surroundings = new Surroundings();
		surroundings.setTileArray( tilesSubGrid );
		return surroundings;
	}

	public boolean isObstacle(int x, int y) {
		return isObstacle(new Position(x, y));
	}
	
	public boolean isObstacle(Position p) {
		if(grid.get(p) == null) {
			return false;
		}
		return !grid.get(p).allowToPass();
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
	
	public Position getStoreAreaTopCorner() {
		return storeAreaTopCorner;
	}

	public Position getStoreAreaBottomCorner() {
		return storeAreaBottomCorner;
	}

	public Position getHomeAreaTopCorner() {
		return homeAreaTopCorner;
	}

	public Position getHomeAreaBottomCorner() {
		return homeAreaBottomCorner;
	}
	
	public void printField() {
		Position p;
		for(int y = 0; y < this.sizeY; y++) {
			
			for(int x = 0; x < this.sizeX; x++) {
				p = new Position( x, y );
				System.out.print( p + " "+ grid.get( p ).getTileType() + "\t");//
			}
			System.out.println("| ");
		}
		System.out.println("Home top corner : " + getHomeAreaTopCorner() );
		System.out.println("Home bottom corner : " + getHomeAreaBottomCorner() );
		System.out.println("Store top corner : " + getStoreAreaTopCorner() );
		System.out.println("Store bottom corner : " + getStoreAreaBottomCorner() );
	}
}
