package com.m2dl.fenwicklife.agent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.m2dl.fenwicklife.Active;
import com.m2dl.fenwicklife.Position;
import com.m2dl.fenwicklife.engine.Box;
import com.m2dl.fenwicklife.engine.Home;
import com.m2dl.fenwicklife.engine.Storage;
import com.m2dl.fenwicklife.engine.Tile;
import com.m2dl.fenwicklife.xmlrpc.messages.Surroundings;

public class Agent extends Active implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Box box;
	// The global interest for moving into a position
	// More the integer is, more the agent like to move into the position (not usually blocked)
	private Map<Position, Integer> fieldMemoryWhenCarrying;
	private Map<Position, Integer> fieldMemoryWhenNotCarrying;
	
	// Used to know current surronding tiles
	private Surroundings currentSurroundings;

	// Memorize store and home position
	private Position storeAreaTopCorner;
	private Position storeAreaBottomCorner;
	private Position homeAreaTopCorner;
	private Position homeAreaBottomCorner;
	
	// Memorize if there is a problem when taking box last time
	private boolean cantTakeBoxLastTime;
	// Memorize last position
	private Position lastPosition;

	// Computing next move
	private Direction nextMove; 
	private int northScore;
	private int southScore;
	private int westScore;
	private int eastScore;


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
		cantTakeBoxLastTime = false;
		fieldMemoryWhenCarrying = new HashMap<Position, Integer>();
		fieldMemoryWhenNotCarrying = new HashMap<Position, Integer>();
		lastPosition = new Position(x,y);
		northScore = 0;
		southScore = 0;
		westScore = 0;
		eastScore = 0;
	}

	/**
	 * Return if the agent is caarying a box
	 * @return true if yes
	 */
	public boolean isCarryingBox() {
		return box != null;
	}

	/**
	 * Try to take a box
	 * @return true if can take a box, else return false
	 */
	public boolean takeBox() {
		if( this.isCarryingBox() ) {
			System.err.println("Already has a box");
			return false;
		}

		if( EngineProxy.getInstance().takeBox(this) ) {
			this.box = new Box( getX(), getY() );
			return true;
		}
		System.err.println("Cannot take box");
		return false;
	}

	/**
	 * Try to drop a box
	 * @return true if can drop it, else return false
	 */
	public boolean dropBox() {
		if( EngineProxy.getInstance().dropBox(this) ) {
			this.box = null;
			return true;
		}
		return false;
	}

	/**
	 * Get actual surroundings from Engine, store it in private currentSurroundings field and return it
	 * @return current currentSurroundings {@link Surroundings}
	 */
	private Surroundings getSurroundings() {
		currentSurroundings = EngineProxy.getInstance().getSurroundings(this);
		return currentSurroundings;
	}

	/**
	 * Test if the agent can move on a tile (depending of his type)
	 * @param t
	 * @return true if yes
	 */
	private boolean canMove(Tile t) {
		if( t == null ) {
			System.err.println("WARN : Tile is null");
			return false;
		}
		return t.allowToPass();
	}

	/**
	 * Test if the agent is in the home zone
	 * @return true if yes
	 */
	private boolean isInHomeZone() {
		if( this.getY() >= homeAreaTopCorner.getY() && this.getY() <= homeAreaBottomCorner.getY() 
				&& this.getX() >= homeAreaTopCorner.getX() ) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Test if the agent is in the store zone
	 * @return true if yes
	 */
	private boolean isInStoreZone() {
		if( this.getY() >= storeAreaTopCorner.getY() && this.getY() <= storeAreaBottomCorner.getY() 
				&& this.getX() <= storeAreaTopCorner.getX() ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Compute the interest to go to the tile in parameter
	 * Depending of his type, his position and the position of the agent
	 * and if the agent is carrying a box or not
	 * @param t
	 * @return the score of the tile
	 */
	public int getTileScore(Tile t) {
		// The score of the tile, more important the szcore is, 
		// more the tile is a good one to go 
		int nowInterest = 1;
		
		int tX = t.getPosition().getX();
		int tY = t.getPosition().getY();

		int aX = getX();
		int aY = getY();
		
		int storeX    = storeAreaTopCorner.getX();
		int storeUp   = storeAreaTopCorner.getY();
		int storeDown = storeAreaBottomCorner.getY();
		
		int homeX    = homeAreaTopCorner.getX();
		int homeUp   = homeAreaTopCorner.getY();
		int homeDown = homeAreaBottomCorner.getY();
		
		// Start with the score memorized
		if(!isInHomeZone() && !isInStoreZone()) {
			if(isCarryingBox()) {
				nowInterest = fieldMemoryWhenCarrying.containsKey(t.getPosition()) ? fieldMemoryWhenCarrying.get(t.getPosition()) : 1;
			}
			else {
				nowInterest = fieldMemoryWhenNotCarrying.containsKey(t.getPosition()) ? fieldMemoryWhenNotCarrying.get(t.getPosition()) : 1;
			}
		}
		nowInterest = nowInterest == 0 ? 1 : nowInterest;

		
		// If we can't move on the tile, we make it very uninterested
		if(!canMove(t)) {
			return Math.abs(nowInterest)*-1;
		}
		// Do the same to force the agent to go back only when needed
		if(lastPosition.getX() == tX && lastPosition.getY() == tY) {
			return Math.abs(nowInterest)*-2;	
		}
		// Compute the score of the tile depending of his position and chere the agent want to go
		if(isCarryingBox()) {
			if(isInHomeZone()) {
				// Maybe improve this one
				if(t instanceof Home) {
					if(!((Home)t).hasBox() && tX > aX) {
						return Math.abs(nowInterest)*10;
					}
				}
				if(tX < homeX) {
					nowInterest=Math.abs(nowInterest)*-1;
				}
			}
			if(aX > homeX) {
				if(aY < homeUp && aY < tY) {
					nowInterest+=2;
				}
				if(aY < homeUp && aY > tY) {
					nowInterest=Math.abs(nowInterest)*-1;				}
				if(aY > homeDown && aY > tY) {
					nowInterest+=2;
				}
				if(aY > homeDown && aY < tY) {
					nowInterest=Math.abs(nowInterest)*-1;				}
			}
			if(aX < homeX && aX < tX) {
				nowInterest+=2;
			}
			if(aX < homeX && aX > tX) {
				nowInterest=Math.abs(nowInterest)*-1;
			}
		}
		else {
			
			if(isInStoreZone() && tX > storeX) {
				nowInterest=Math.abs(nowInterest)*-1;
			}
			if(t instanceof Storage) {
				if(((Storage)t).hasBox()) {
					nowInterest=Math.abs(nowInterest)*10;
				}
			}
			else {
				if("Box".equals(t.getTileType())) {
					nowInterest=Math.abs(nowInterest)*10;
				}
			}
			if(aX < storeX) {
				if(aY < storeUp && aY < tY) {
					nowInterest+=2;
				}
				if(aY < storeUp && aY > tY) {
					nowInterest=Math.abs(nowInterest)*-1;				}
				if(aY > storeDown && aY > tY) {
					nowInterest+=2;
				}
				if(aY > storeDown && aY < tY) {
					nowInterest=Math.abs(nowInterest)*-1;				}
			}
			if(aX > storeX && aX > tX) {
				nowInterest+=2;
			}
			if(aX > storeX && aX < tX) {
				nowInterest=Math.abs(nowInterest)*-1;
			}
		}
		
		// When not in the vertical range of the store or the home, prefer go horizontally
		if(aY == tY && nowInterest > 0
		&& aX < homeX && aX > storeX) {
			nowInterest = (nowInterest + 1) * 20;
		}

		return nowInterest;
	}

	/**
	 * Used to perceive the environnement
	 */
	public void perceive() {
		// First retrieve the surronding
		getSurroundings();
		// For each tile perceived, refresh field memory
		// Divide the interest of the tile by two if its blocked
		// This part is used to memorize tile interest in time
		Map<Position, Integer> tmpFieldMemory;
		tmpFieldMemory = isCarryingBox() ? fieldMemoryWhenCarrying : fieldMemoryWhenNotCarrying;
		for(Tile[] tArray : currentSurroundings.getTileArray()) {
			if(tArray != null) {
				for(Tile t : tArray) {
					if(t != null) {
						if(!tmpFieldMemory.containsKey(t.getPosition())) {
							tmpFieldMemory.put(t.getPosition(), 0);
						}
						if(tmpFieldMemory.containsKey(t.getPosition())) {
							int oldCount = tmpFieldMemory.get(t.getPosition()).intValue();
							if(!canMove(t)) {
								if(oldCount > 0) {
									oldCount /= 2;
								}
								else {
									oldCount *= 2;
								}
							}
							tmpFieldMemory.put(t.getPosition(), oldCount);
						}
					}
				}	
			}
		}
		if(isCarryingBox()) {
			fieldMemoryWhenCarrying = tmpFieldMemory;
		}
		else {
			fieldMemoryWhenNotCarrying = tmpFieldMemory;
		}
		// Now perceive the surronding and computing the best movement
		// For the current state
		northScore = 0;
		southScore = 0;
		westScore = 0;
		eastScore = 0;
		// For each tile in max 3 distance in four direction, 
		// Calculate the interest to go in that direction depending
		// Of the type of tile, if its blocked, etc...
		for(Tile[] tArray : currentSurroundings.getTileArray()) {
			if(tArray != null) {
				for(Tile t : tArray) {
					int tX = t.getPosition().getX();
					int tY = t.getPosition().getY();
	
					int aX = getX();
					int aY = getY();
					
					if(aX == tX) {
						if(tY > aY) {
							southScore += getTileScore(t);
						}
						if(tY < aY) {
							northScore += getTileScore(t);
						}	
					}
					if(aY == tY) {
						if(tX > aX) {
							eastScore += getTileScore(t);
						}
						if(tX < aX) {
							westScore += getTileScore(t);
						}	
					}
				}
			}
		}
	}

	/**
	 * Used to decide where to go
	 */
	public void decide() {
		// Now, choose a direction depending of the directions score and agent possibility
		if(northScore > southScore
		&& northScore > westScore
		&& northScore > eastScore
		&& canMove(currentSurroundings.getTileInDirection(Direction.NORTH))) {
			nextMove = Direction.NORTH;
		}
		else if(southScore > westScore
		&& southScore > eastScore
		&& canMove(currentSurroundings.getTileInDirection(Direction.SOUTH))) {
			nextMove = Direction.SOUTH;
		}
		else if(eastScore > westScore
		&& canMove(currentSurroundings.getTileInDirection(Direction.EAST))){ 
			nextMove = Direction.EAST;
		}
		else if(canMove(currentSurroundings.getTileInDirection(Direction.WEST))) {
			nextMove = Direction.WEST;
		}
		else {
			nextMove = Direction.NONE;
		}

		// Force the agent to move if he can't take or drop the box last time
		if(!cantTakeBoxLastTime) {
			if(isInStoreZone() && !isCarryingBox()) {
				nextMove = Direction.NONE;
			}
			if(isInHomeZone() && isCarryingBox()) {
				nextMove = Direction.NONE;
			}
		}
		cantTakeBoxLastTime = false;
		System.out.println("Decide to go to " + nextMove.toString());
		System.out.println("North Score : " + northScore);
		System.out.println("East Score : " + eastScore);
		System.out.println("West Score : " + westScore);
		System.out.println("South Score : " + southScore);
	}

	/**
	 * Used to act on the environnement (move, take box, drop box)
	 */
	public void act() {
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
				cantTakeBoxLastTime = !dropBox();
			}
			else if(isInStoreZone()  && !isCarryingBox()) {
				cantTakeBoxLastTime = !takeBox();
			}
			return;
		}
		if( nextMove != Direction.NONE ) {
			hasMoved = EngineProxy.getInstance().move(this, nextX, nextY);
			lastPosition.setX(getX());
			lastPosition.setY(getY());
			if( hasMoved ) {
				this.setX(nextX);
				this.setY(nextY);
			}
		}
	}
}
