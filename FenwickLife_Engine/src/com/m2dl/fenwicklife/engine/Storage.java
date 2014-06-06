package com.m2dl.fenwicklife.engine;

import java.io.Serializable;

import com.m2dl.fenwicklife.Position;

public class Storage extends Empty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Storage(Position pos) {
		super(pos);
		this.setBox(); // set a box by default in storage
		// TODO Auto-generated constructor stub
	}

	public Storage(int x, int y) {
		this(new Position(x, y));
	}

}
