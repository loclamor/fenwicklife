package com.m2dl.fenwicklife;

import java.io.Serializable;

public class Active implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
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
	public Active() {
		this.x = 0;
		this.y = 0;
	}
	public Active(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public boolean inCollision(Active a) {
		return a.getX() == getX() && a.getY() == getY();
	}
}
