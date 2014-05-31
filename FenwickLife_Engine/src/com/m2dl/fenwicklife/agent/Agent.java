package com.m2dl.fenwicklife.agent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.m2dl.fenwicklife.Active;
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
	private boolean isInStoreZone;
	private boolean isInHomeZone;
	
	public Agent() {
		super(1,1);
	}
	
	public Agent(int x, int y) {
		super(x, y);
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
	
	private Surroundings getSurroundings() {
		Surroundings surroundings = EngineProxy.getInstance().getSurroundings(this);
		
		return surroundings;
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
		availableDestinations = null;
		while(availableDestinations == null) {
			availableDestinations = getAvailableDestinations(getSurroundings().getSurroundings(1));
		}
		// TODO
		isInStoreZone = false;
		isInHomeZone = false;
	}
	
	public void decide() {
		if(isCarryingBox()) {
			if(isInHomeZone) {
				// TODO act = dropBox()
			} else {
				// TODO act = move to right (vers home zone)
			}
		} else {
			if(isInStoreZone) {
				// TODO act = takeBox(...)
			} else {
				// TODO act = move to left (vers store zone)
			}
		}
		
	}
	
	public void act() {
		// TODO executer l'action decidee
		
		// tmp : bouger n'importe ou
		Tile firstDestination = availableDestinations.get( getRandomInRange(0,availableDestinations.size() ) );
		boolean hasMoved = EngineProxy.getInstance().move(this, firstDestination.getPosition().getX(), firstDestination.getPosition().getY());
		if( hasMoved ) {
			this.setX(firstDestination.getPosition().getX());
			this.setY(firstDestination.getPosition().getY());
		}
		else {
			//TODO : again untill move is OK ?
		}
	}
	
	public int getRandomInRange( int min, int max ) {
		return (int) (min + (Math.random() * (max - min)));
	}
}
