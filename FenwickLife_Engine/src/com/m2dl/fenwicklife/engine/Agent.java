package com.m2dl.fenwicklife.engine;

public class Agent extends Active{
	
	private boolean isEMpty;
	public Agent(int x, int y, boolean isEMpty) {
		super(x, y);
		this.isEMpty = isEMpty;
	}
	public boolean isEMpty() {
		return isEMpty;
	}
	public void setEMpty(boolean isEMpty) {
		this.isEMpty = isEMpty;
	}
}
