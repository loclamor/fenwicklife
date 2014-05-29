package com.m2dl.fenwicklife.agent;

import java.util.ArrayList;
import java.util.List;

import com.m2dl.fenwicklife.Active;
import com.m2dl.fenwicklife.engine.Box;
import com.m2dl.fenwicklife.engine.Engine;
import com.m2dl.fenwicklife.engine.Tile;
import com.m2dl.fenwicklife.engine.TileType;

public class Agent extends Active {
	
	private Box box;
	private List<Tile> availableDestinations = new ArrayList<Tile>();
	private boolean isInStoreZone;
	private boolean isInHomeZone;
	
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
	
	private Tile[][] getSurroundings() {
		Tile[][] surroundings = Engine.getInstance().getField().getSurroundings(this.getX(), this.getY());
		
		return surroundings;
	}
	
	private boolean canMove(Tile t) {
		return !Engine.getInstance().getField().isObstacle(t.getX(), t.getY());
	}
	
	private List<Tile> getAvailableDestinations(Tile[][] surroundings) {
		List<Tile> availableDestinations = new ArrayList<Tile>();
		
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
		// TODO changer Engine.getInstance() par proxy
		
		availableDestinations = getAvailableDestinations(getSurroundings());
		isInStoreZone = Engine.getInstance().getField().getTileType(this.getX(), this.getY()) == TileType.STORE;
		isInHomeZone = Engine.getInstance().getField().getTileType(this.getX(), this.getY()) == TileType.HOME;
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
		Tile firstDestination = availableDestinations.get(0);
		EngineProxy.getInstance().move(this, firstDestination.getX(), firstDestination.getY());
	}
}
