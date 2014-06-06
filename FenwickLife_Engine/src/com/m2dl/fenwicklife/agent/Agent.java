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
	private Map<Position, Integer> fieldMemory;

	private Position storeAreaTopCorner;
	private Position storeAreaBottomCorner;
	private Position homeAreaTopCorner;
	private Position homeAreaBottomCorner;
	private boolean cantTakeBoxLastTime;

	private Direction nextMove; 
	private int northScore;
	private int southScore;
	private int westScore;
	private int eastScore;
	private Position lastPosition;

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
		cantTakeBoxLastTime = false;
		fieldMemory = new HashMap<Position, Integer>();
		lastPosition = new Position(x,y);
		northScore = 0;
		southScore = 0;
		westScore = 0;
		eastScore = 0;
	}

	public boolean isCarryingBox() {
		return box != null;
	}

	public boolean takeBox() {
		// TODO : exception if take box when already carrying one ?
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

	private boolean canMove(Tile t) {
		if( t == null ) {
			System.err.println("WARN : Tile is null");
			return false;
		}
		return t.allowToPass();
	}

	//FIXME : is it still useful ?
	//	private List<Tile> getAvailableDestinations(Tile[][] surroundings) {
	//		List<Tile> availableDestinations = new ArrayList<Tile>();
	//		if(surroundings == null) {
	//			return null;
	//		}
	//		for(int i=0; i < surroundings.length; i++) {
	//			for(int j=0; j < surroundings[i].length; j++) {
	//				Tile currentTile = surroundings[i][j];
	//				if(canMove(currentTile)) {
	//					availableDestinations.add(currentTile);
	//				}
	//			}
	//		}
	//		
	//		return availableDestinations;
	//	}



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

	public void perceive() {
		getSurroundings();
		// For each tile perceived, refresh field memory
		// If tile blocked, don't like to go into it and substract 1
		// If tile not blocked, like it and add 1
		for(Tile[] tArray : currentSurroundings.getTileArray()) {
			if(tArray != null) {
				for(Tile t : tArray) {
					if(t != null) {
						if(!fieldMemory.containsKey(t.getPosition())) {
							fieldMemory.put(t.getPosition(), 0);
						}
						if(fieldMemory.containsKey(t.getPosition())) {
							int oldCount = fieldMemory.get(t.getPosition()).intValue();
							if(!canMove(t)) {
								oldCount --;
							}
							else {
								oldCount ++;							
							}
							fieldMemory.put(t.getPosition(), oldCount);
						}
					}
				}	
			}
		}
		
		northScore = 0;
		southScore = 0;
		westScore = 0;
		eastScore = 0;
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

	public int getTileScore(Tile t) {
		int nowInterest = fieldMemory.containsKey(t.getPosition()) ? fieldMemory.get(t.getPosition()) : 0;
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
		
		if(!canMove(t)) {
			if(aX == tX) {
				return Math.abs(nowInterest)*-2;
			}
		}
		if(lastPosition.getX() == tX && lastPosition.getY() == tY) {
			nowInterest=Math.abs(nowInterest)*-1;	
		}
		if(isCarryingBox()) {
			if(isInHomeZone()) {
				if(t instanceof Home) {
					if(!((Home)t).hasBox()) {
						nowInterest=Math.abs(nowInterest)*10;
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
		
		if(aY == tY && nowInterest > 0
		&& aX < homeX && aX > storeX) {
			nowInterest = (nowInterest + 1) * 20;
		}

		return nowInterest;
	}

	public void decide() {


		System.out.println("Agent decide");
		System.out.println("North Score : " + northScore);
		System.out.println("South Score : " + southScore);
		System.out.println("West Score : " + westScore);
		System.out.println("East Score : " + eastScore);

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
		else {
			nextMove = Direction.WEST;
		}

		if(!cantTakeBoxLastTime) {
			if(isInStoreZone() && !isCarryingBox()) {
				nextMove = Direction.NONE;
			}
			if(isInHomeZone() && isCarryingBox()) {
				nextMove = Direction.NONE;
			}
		}
		cantTakeBoxLastTime = false;
		System.out.println("Decide to go to" + nextMove.toString());
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

	public int getRandomInRange( int min, int max ) {
		return (int) (min + (Math.random() * (max - min)));
	}
}
