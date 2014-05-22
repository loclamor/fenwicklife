package com.m2dl.fenwicklife.engine;

public class Tile {
	private int X;
	private int Y;
	private CaseType type;
	
	public Tile(int x, int y, CaseType type) {
		super();
		X = x;
		Y = y;
		this.type = type;
	}
	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	public CaseType getType() {
		return type;
	}
	public void setType(CaseType type) {
		this.type = type;
	}
}
