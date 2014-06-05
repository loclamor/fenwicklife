package com.m2dl.fenwicklife.xmlrpc.messages;

import com.m2dl.fenwicklife.Active;
import com.m2dl.fenwicklife.agent.Agent;

public class SimpleAgent extends Active{

	private boolean hasBox;
	
	public SimpleAgent( Agent a ) {
		this.setX( a.getX() );
		this.setY( a.getY() );
		this.hasBox = a.isCarryingBox();
		
	}
	
	public SimpleAgent( int x, int y ) {
		this.setX( x );
		this.setY( y );
		this.hasBox = false;
	}
	
	public boolean isCarryingBox() {
		return hasBox;
	}

}
