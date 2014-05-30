package com.m2dl.fenwicklife.engine;

import java.io.Serializable;

import com.m2dl.fenwicklife.Position;

public class Empty extends Tile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Empty(Position pos) {
		super(pos);
		this.removeAgent();
		this.removeBox();
	}

	public Empty(int x, int y) {
		this( new Position( x, y ) );
	}

}
