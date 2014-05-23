package com.m2dl.fenwicklife.agent;

import com.m2dl.fenwicklife.Active;
import com.m2dl.fenwicklife.engine.Box;
import com.m2dl.fenwicklife.engine.Engine;
import com.m2dl.fenwicklife.engine.Tile;

public class Agent extends Active {
	
	private Box box;
	
	public Agent(int x, int y) {
		super(x, y);
	}
	
	public boolean isCarryingBox() {
		return box != null;
	}
	
	public boolean takeBox(Box box) {
		// TODO : exception if take box when already carrying one ?
		if(this.isCarryingBox()) return false;
		this.box = box;
		return true;
	}
	
	public void dropBox() {
		this.box = null;
	}
	
	private Tile[][] getSurroundings() {
		Tile[][] surroundings = Engine.getInstance().getField().getSurroundings(this.getX(), this.getY());
		
		return surroundings;
	}
}