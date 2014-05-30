package com.m2dl.fenwicklife.engine;

import java.io.Serializable;

import com.m2dl.fenwicklife.Position;

public class Wall extends Tile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Wall(Position pos) {
		super(pos);
		this.removeAgent();
		this.removeBox();
	}
	
	public Wall(int x, int y) {
		this( new Position( x, y ) );
	}
	
	public boolean allowToPass() {
		return false;
	}

}
