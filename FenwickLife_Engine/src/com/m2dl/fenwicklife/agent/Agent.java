package com.m2dl.fenwicklife.agent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.m2dl.fenwicklife.Active;
import com.m2dl.fenwicklife.Position;
import com.m2dl.fenwicklife.engine.Box;
import com.m2dl.fenwicklife.engine.Engine;
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
	private List<Position> lastPositionsWhenCarrying;
	private Map<Position, Integer> fieldMemoryWhenNotCarrying;
	private List<Position> lastPositionsWhenNotCarrying;
	private int NUMBER_OF_POSITIONS_MEMORIZED = 50;

	// Used to know current surronding tiles
	private Surroundings currentSurroundings;

	// Memorize store and home position
	private Position storeAreaTopCorner;
	private Position storeAreaBottomCorner;
	private Position homeAreaTopCorner;
	private Position homeAreaBottomCorner;

	// Computing next move
	private AgentDecision nextMove; 
	private int northScore;
	private int southScore;
	private int westScore;
	private int eastScore;

	// Number of move without finding a box
	private int nbMoveUseless;

	// The agent is dead
	private boolean isDead;


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
		nextMove = AgentDecision.NONE;
		fieldMemoryWhenCarrying = new HashMap<Position, Integer>();
		fieldMemoryWhenNotCarrying = new HashMap<Position, Integer>();
		lastPositionsWhenCarrying = new ArrayList<Position>();
		lastPositionsWhenNotCarrying = new ArrayList<Position>();
		northScore = 0;
		southScore = 0;
		westScore = 0;
		eastScore = 0;
		nbMoveUseless = 0;
		isDead = false;
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
	 * Try to suicide
	 * @return true if succeed
	 */
	public boolean suicide() {
		if(!isCarryingBox()) {
			if(EngineProxy.getInstance().suicide(this)) {
				isDead = true;
				return true;
			}
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
			if(t instanceof Storage || t instanceof Home) {
				return 0;
			}
			return -70;
		}
		// Do the same to force the agent to go back only when needed
		if(isCarryingBox() && lastPositionsWhenCarrying.contains(t.getPosition())) {
			return -100;	
		}
		if(!isCarryingBox() && lastPositionsWhenNotCarrying.contains(t.getPosition())) {
			return -100;	
		}
		// Compute the score of the tile depending of his position and chere the agent want to go
		if(isCarryingBox()) {
			if(isInHomeZone()) {
				// Maybe improve this one
				if(t instanceof Home) {
					if(!((Home)t).hasBox()) {
						return 105;
					}
				}
				if(tY > homeDown || tY < homeUp || tX < homeX) {
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
			if(isInStoreZone() && (tY > storeDown || tY < storeUp || tX > storeX)) {
				nowInterest=Math.abs(nowInterest)*-1;
			}
			if(t instanceof Storage) {
				if(((Storage)t).hasBox()) {
					return 105;
				}
			}
			else {
				if("Box".equals(t.getTileType())) {
					return 105;
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
			nowInterest = (nowInterest + 1) * 3;
		}

		return Math.max(-100, Math.min(nowInterest, 100));
	}

	/**
	 * Used to perceive the environnement
	 */
	public void perceive() {
		if(isDead) {
			return;
		}
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
							tmpFieldMemory.put(t.getPosition(), 1);
						}
						if(tmpFieldMemory.containsKey(t.getPosition())) {
							int oldCount = tmpFieldMemory.get(t.getPosition()).intValue();
							if(!canMove(t)) {
								if(oldCount > 0) {
									oldCount = -10;
								}
								else {
									oldCount *= 2;
								}
							}
							else {
								if(oldCount < 0) {
									oldCount = oldCount / 2 + 1;	
								}
							}
							oldCount = oldCount == 0 ? 1 : oldCount;
							oldCount = Math.max(Math.min(oldCount, 100), -100);
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
					if(!isInHomeZone() && !isInStoreZone()) {
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
					else {
						if(tY > aY && Math.abs(tY - aY) <= 1 && Math.abs(tX - aX) <= Math.abs(tY - aY) ) {
							southScore += getTileScore(t);
						}
						if(tY < aY && Math.abs(tY - aY) <= 1 && Math.abs(tX - aX) <= Math.abs(tY - aY)) {
							northScore += getTileScore(t);
						}
						if(tX > aX && Math.abs(tX - aX) <= 1 && Math.abs(tY - aY) <= Math.abs(tX - aX)) {
							eastScore += getTileScore(t);
						}
						if(tX < aX && Math.abs(tX - aX) <= 1 && Math.abs(tY - aY) <= Math.abs(tX - aX)) {
							westScore += getTileScore(t);
						}	
					}
				}
			}
		}
		Position lastPosition = new Position(getX(), getY());
		if(isCarryingBox()) {
			lastPositionsWhenCarrying.add(lastPosition);
			if(lastPositionsWhenCarrying.size() >= NUMBER_OF_POSITIONS_MEMORIZED) {
				lastPositionsWhenCarrying.remove(0);
			}
		}
		else {
			lastPositionsWhenNotCarrying.add(lastPosition);
			if(lastPositionsWhenNotCarrying.size() >= NUMBER_OF_POSITIONS_MEMORIZED) {
				lastPositionsWhenNotCarrying.remove(0);
			}
		}
	}

	/**
	 * Used to decide where to go
	 */
	public void decide() {
		if(isDead) {
			return;
		}
		// Now, choose a direction depending of the directions score and agent possibility
		if(northScore > southScore
				&& northScore > westScore
				&& northScore > eastScore
				&& canMove(currentSurroundings.getTileInDirection(AgentDecision.NORTH))) {
			nextMove = AgentDecision.NORTH;
		}
		else if(southScore > westScore
				&& southScore > eastScore
				&& canMove(currentSurroundings.getTileInDirection(AgentDecision.SOUTH))) {
			nextMove = AgentDecision.SOUTH;
		}
		else if(eastScore > westScore
				&& canMove(currentSurroundings.getTileInDirection(AgentDecision.EAST))){ 
			nextMove = AgentDecision.EAST;
		}
		else if(canMove(currentSurroundings.getTileInDirection(AgentDecision.WEST))) {
			nextMove = AgentDecision.WEST;
		}
		else {
			nextMove = AgentDecision.NONE;
		}
		if(isInStoreZone() && nbMoveUseless > Engine.DEFAULT_STORE_HOME_WIDTH * Engine.DEFAULT_STORE_HOME_HEIGHT * 3) {
			nextMove = AgentDecision.SUICIDE;
		}
		if((isInHomeZone() || isInStoreZone()) && nbMoveUseless > Engine.DEFAULT_STORE_HOME_WIDTH * Engine.DEFAULT_STORE_HOME_HEIGHT
		&& southScore < 200 && northScore < 200 && eastScore < 200 && westScore < 200) {
			int random = (int) (Math.random() * 100);
			// FAVORIZE NORTH DIRECTION (because agent naturally prefer go in south)
			if(random < 20) { 
				nextMove = AgentDecision.EAST;
			}
			else if(random < 50) { 
				nextMove = AgentDecision.NORTH;
			}
			else if(random < 80) {
				nextMove = AgentDecision.SOUTH;
			}
			else {
				nextMove = AgentDecision.WEST;
			}
		}

		// Force the agent to move if he can't take or drop the box last time
		//		if(!cantTakeBoxLastTime) {
		if(isInStoreZone() && !isCarryingBox() && currentSurroundings.getLocalTile().hasBox() ) {
			System.out.println( currentSurroundings.getLocalTile().hasBox()?"Box":"PasBox");
			nextMove = AgentDecision.TAKE;
		}
		if(isInHomeZone() && isCarryingBox() && !currentSurroundings.getLocalTile().hasBox() ) {
			nextMove = AgentDecision.DROP;
		}
		if(((isCarryingBox() && isInStoreZone()) || (!isCarryingBox() && isInHomeZone()))) {
			nbMoveUseless = 0;
		}
		if((!isCarryingBox() && isInStoreZone()) || (isInHomeZone() && isCarryingBox())) {
			nbMoveUseless++;
		}
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
		if(nextMove == AgentDecision.SUICIDE) {
			suicide();
		}
		if(isDead) {
			return;
		}
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
		case TAKE :
			takeBox();
			return;
		case DROP :
			dropBox();
			return;
		case NONE :
			return;
		}
		if( nextMove != AgentDecision.NONE ) {
			hasMoved = EngineProxy.getInstance().move(this, nextX, nextY);
			if( hasMoved ) {
				this.setX(nextX);
				this.setY(nextY);
			}
		}
	}

	public boolean isDead() {
		return isDead;
	}
}
