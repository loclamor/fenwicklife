package com.m2dl.fenwicklife.engine;

import java.io.Serializable;

import com.m2dl.fenwicklife.Position;

public class Tile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	private TileType type;
	
	public Tile(Position pos, TileType type) {
		super();
		this.x = pos.getX();
		this.y = pos.getY();
		this.type = type;
	}
	public Tile(int x, int y, TileType type) {
		super();
		this.x = x;
		this.y = y;
		this.type = type;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public TileType getType() {
		return type;
	}
	public void setType(TileType type) {
		this.type = type;
	}
}
