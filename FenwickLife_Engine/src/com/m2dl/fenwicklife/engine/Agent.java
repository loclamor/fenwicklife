package com.m2dl.fenwicklife.engine;

public class Agent extends Active {
	
	private boolean isCarryingBox;
	
	public Agent(int x, int y) {
		super(x, y);
		this.isCarryingBox = false;
	}
	
	public boolean isCarryingBox() {
		return isCarryingBox;
	}
	
	public void setEMpty(boolean isEMpty) {
		this.isCarryingBox = isEMpty;
	}
	
	public Tile[][] getSurroundings() {
		
		Tile[][] surroundings = new Tile[3][3];
		
		return surroundings;
	}
}
