package com.m2dl.fenwicklife.engine;

public class Tile {
	private int X;
	private int Y;
	private TileType type;
	
	public Tile(int x, int y, TileType type) {
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
	public TileType getType() {
		return type;
	}
	public void setType(TileType type) {
		this.type = type;
	}
}
