package com.m2dl.fenwicklife.engine;

import java.io.Serializable;

import com.m2dl.fenwicklife.Position;
import com.m2dl.fenwicklife.xmlrpc.messages.SimpleAgent;

public abstract class Tile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Position pos;
	private boolean hasBox;
	private SimpleAgent agent = null;
	
	public Tile(Position pos) {
		this.pos = pos;
	}
	public Tile(int x, int y) {
		this( new Position( x, y ) );
	}
	
	public void setAgent( SimpleAgent agent ) {
		this.agent = agent;
	}
	
	public void removeAgent() {
		this.agent = null;
	}
	
	public boolean allowToPass() {
		return !hasAgent() ;
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
	
	public boolean hasAgentWithBox() {
		if( hasAgent() ) {
			return agent.isCarryingBox();
		}
		return false;
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
