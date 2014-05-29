package com.m2dl.fenwicklife.engine;

import java.io.Serializable;

import com.m2dl.fenwicklife.Active;

public class Box extends Active implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Box() {
		super(0,0);
	}
	public Box(int x, int y) {
		super(x, y);
	}

}
