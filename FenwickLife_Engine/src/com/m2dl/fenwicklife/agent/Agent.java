package com.m2dl.fenwicklife.agent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.m2dl.fenwicklife.Active;
import com.m2dl.fenwicklife.Position;
import com.m2dl.fenwicklife.engine.Box;
import com.m2dl.fenwicklife.engine.Tile;
import com.m2dl.fenwicklife.xmlrpc.messages.Surroundings;

public class Agent extends Active implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Box box;
	private List<Tile> availableDestinations = new ArrayList<Tile>();
	
	private Position storeAreaTopCorner;
	private Position storeAreaBottomCorner;
	private Position homeAreaTopCorner;
	private Position homeAreaBottomCorner;
	
	private Direction nextMove;
	
	private Surroundings currentSurroundings;
	
	public Agent() {
		this(1,1);
	}
	
	public Agent(int x, int y) {
		super(x, y);
		//first, get positions of home and store
		storeAreaTopCorner = EngineProxy.getInstance().getStoreAreaTopCorner();
		storeAreaBottomCorner = EngineProxy.getInstance().getStoreAreaBottomCorner();
		homeAreaTopCorner = EngineProxy.getInstance().getHomeAreaTopCorner();
		homeAreaBottomCorner = EngineProxy.getInstance().getHomeAreaBottomCorner();
		nextMove = Direction.NONE;
	}
	
	public boolean isCarryingBox() {
		return box != null;
	}
	
	public boolean takeBox(Box box) {
		// TODO : exception if take box when already carrying one ?
		if(this.isCarryingBox()) return false;
		
		if( EngineProxy.getInstance().takeBox(this) ) {
			this.box = box;
		}
		
		return true;
	}
	
	public void dropBox() {
		if( EngineProxy.getInstance().dropBox(this) ) {
			this.box = null;
		}
	}
	
	/**
	 * Get actual surroundings from Engine, store it in private currentSurroundings field and return it
	 * @return current currentSurroundings {@link Surroundings}
	 */
	private Surroundings getSurroundings() {
		currentSurroundings = EngineProxy.getInstance().getSurroundings(this);
		return currentSurroundings;
	}
	
	private boolean canMove(Tile t) {
		if( t == null ) {
			System.err.println("WARN : Tile is null");
			return false;
		}
		return t.allowToPass();
	}
	
	private List<Tile> getAvailableDestinations(Tile[][] surroundings) {
		List<Tile> availableDestinations = new ArrayList<Tile>();
		if(surroundings == null) {
			return null;
		}
		for(int i=0; i < surroundings.length; i++) {
			for(int j=0; j < surroundings[i].length; j++) {
				Tile currentTile = surroundings[i][j];
				if(canMove(currentTile)) {
					availableDestinations.add(currentTile);
				}
			}
		}
		
		return availableDestinations;
	}
	
	public void perceive() {
		getSurroundings();
	}
	
	private boolean isInHomeZone() {
		if( this.getY() >= homeAreaTopCorner.getY() && this.getY() <= homeAreaBottomCorner.getY() 
			&& this.getX() >= homeAreaTopCorner.getX() ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean isInStoreZone() {
		if( this.getY() >= storeAreaTopCorner.getY() && this.getY() <= storeAreaBottomCorner.getY() 
			&& this.getX() <= storeAreaTopCorner.getX() ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void decide() {
		System.out.println("Agent Decide");
		Tile northTile = currentSurroundings.getTileInDirection(Direction.NORTH);
		Tile southTile = currentSurroundings.getTileInDirection(Direction.SOUTH);
		Tile westTile = currentSurroundings.getTileInDirection(Direction.WEST);
		Tile eastTile = currentSurroundings.getTileInDirection(Direction.EAST);
		if(!isCarryingBox()) {
			System.out.println("Agent doesn't have box");
			if(isInStoreZone() && !isCarryingBox()) {
				System.out.println("Agent is in store");
				nextMove = Direction.NONE;
			}
			else {
				if(canMove(westTile)) {
					nextMove = Direction.WEST;
				}
				else if(nextMove == Direction.NORTH
			     	 && canMove(northTile)) {
					nextMove = Direction.NORTH;
				}
				else if(nextMove == Direction.SOUTH
				   	 && canMove(southTile)) {
					nextMove = Direction.SOUTH;
				}
				else if(nextMove == Direction.WEST 
					 && !canMove(westTile)
					 && !canMove(northTile)
					 && !canMove(southTile)
					 && canMove(eastTile)) {
					nextMove = Direction.EAST;
				}
				else if(nextMove == Direction.SOUTH 
					 && !canMove(southTile)
					 && canMove(northTile)) {
					nextMove = Direction.NORTH;
				}
				else if(nextMove == Direction.NORTH 
					 && !canMove(northTile)
					 && canMove(southTile)) {
					nextMove = Direction.SOUTH;
				}
				else if(canMove(northTile)){
					nextMove = Direction.NORTH;
				}
				else if(canMove(southTile)){
					nextMove = Direction.SOUTH;
				}
				else {
					nextMove = Direction.NONE;
				}
			}
		} else {
			System.out.println("Agent has box");
			if(isInHomeZone() && isCarryingBox()) {
				System.out.println("Agent is at home");
				nextMove = Direction.NONE;
			}
			else {
				if(canMove(eastTile)) {
					nextMove = Direction.EAST;
				}
				else if(nextMove == Direction.NORTH
			     	 && canMove(northTile)) {
					nextMove = Direction.NORTH;
				}
				else if(nextMove == Direction.SOUTH
				   	 && canMove(southTile)) {
					nextMove = Direction.SOUTH;
				}
				else if(nextMove == Direction.SOUTH 
					 && !canMove(southTile)
					 && canMove(northTile)) {
					nextMove = Direction.NORTH;
				}
				else if(nextMove == Direction.NORTH 
					 && !canMove(northTile)
					 && canMove(southTile)) {
					nextMove = Direction.SOUTH;
				}
				else if(canMove(northTile)){
					nextMove = Direction.NORTH;
				}
				else if(canMove(southTile)){
					nextMove = Direction.SOUTH;
				}
				else {
					nextMove = Direction.NONE;
				}
			}
		}
		System.out.println("Agent next move would be " + nextMove.toString());
	}
	
	public void act() {
		// TODO executer l'action decidee
		
		// tmp : bouger n'importe ou
		boolean hasMoved = false;
		int nextX = getX();
		int nextY = getY();
		switch(nextMove) {
			case NORTH :
				nextY--;
				break;
			case WEST : 
				nextX--;
				break;
			case SOUTH :
				nextY++;
				break;
			case EAST :
				nextX++;
				break;
			case NONE :
				if(isInHomeZone() && isCarryingBox()) {
					box = null;
					// TODO Send to server
				}
				else if(isInStoreZone()  && !isCarryingBox()) {
					box = new Box();
					// TODO Send to server
				}
				return;
		}
		hasMoved = EngineProxy.getInstance().move(this, nextX, nextY);
		if( hasMoved ) {
			this.setX(nextX);
			this.setY(nextY);
		}
		else {
			//TODO : again untill move is OK ?
		}
	}
	
	public int getRandomInRange( int min, int max ) {
		return (int) (min + (Math.random() * (max - min)));
	}
}
