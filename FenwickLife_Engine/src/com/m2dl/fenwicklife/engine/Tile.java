package com.m2dl.fenwicklife.engine;

import java.io.Serializable;

import com.m2dl.fenwicklife.Position;
import com.m2dl.fenwicklife.agent.Agent;

public abstract class Tile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Position pos;
	private boolean hasBox;
	private Agent agent = null;
	
	public Tile(Position pos) {
		this.pos = pos;
	}
	public Tile(int x, int y) {
		this( new Position( x, y ) );
	}
	
	public void setAgent( Agent agent ) {
		this.agent = agent;
	}
	
	public void removeAgent() {
		this.agent = null;
	}
	
	public boolean allowToPass() {
		return !hasAgent();// && !hasBox(); //FIXME : can we pass over a box ?
	}
	
	public Position getPosition() {
		return this.pos;
	}
	
	public boolean hasAgent() {
		return this.agent != null;
	}
	
	public boolean hasBox() {
		return this.hasBox;
	}
	public void setPosition(Position pos) {
		this.pos = pos;
	}
	public void setBox() {
		this.hasBox = true;
	}
	
	public void removeBox() {
		this.hasBox = false;
	}

	public String getTileType() {
		return this.getClass().getSimpleName();
	}
}
