package com.m2dl.fenwicklife.engine;

import com.m2dl.fenwicklife.Position;
import com.m2dl.fenwicklife.agent.Agent;
import com.m2dl.fenwicklife.xmlrpc.messages.Surroundings;

public class Engine {
	
	public static final int DEFAULT_SIZE_X = 60;
	public static final int DEFAULT_SIZE_Y = 40;
	public static final int DEFAULT_CENTER_WALL_SIZE = 10;
	public static final int DEFAULT_FIRST_CORRIDOR_Y = 3;
	public static final int DEFAULT_SECOND_CORRIDOR_Y = 6;
	public static final int DEFAULT_STORE_HOME_WIDTH = 10;
	public static final int DEFAULT_STORE_HOME_HEIGHT = 20;
	
	private Field field;
	
	private static Engine instance = new Engine(); 
	
	private Engine() {
		this.field = new Field(DEFAULT_SIZE_X, DEFAULT_SIZE_Y, DEFAULT_CENTER_WALL_SIZE,
				DEFAULT_FIRST_CORRIDOR_Y, DEFAULT_SECOND_CORRIDOR_Y, DEFAULT_STORE_HOME_WIDTH, DEFAULT_STORE_HOME_HEIGHT);
	}
	
	public Field getField() {
		return field;
	}
	
	public static Engine getInstance() {
		return instance;
	}

	public boolean hello(Agent me) {
		if( !field.isObstacle(me.getX(), me.getY())) {
			field.setAgent( me.getX(), me.getY(), me);
			return true;
		}
		return false;
	}

	public boolean move(Agent me, int to_x, int to_y) {
		if(field.isObstacle(to_x, to_y)) {
			return false;
		}
		if(to_x >= me.getX() - 1 && to_x <= me.getX() + 1 
		&& to_y >= me.getY() - 1 && to_y <= me.getY() + 1) {
			field.removeAgent( me.getX(), me.getY() );
			field.removeBox( me.getX(), me.getY() );
			if(me.isCarryingBox()) {
				field.setBox(to_x, to_y);
			}
			
			field.setAgent(to_x, to_y, me);
			
			return true;
		}
		return false;
	}

	public boolean takeBox(Agent me) {
		if(field.getTile(me.getX(), me.getY()).hasBox() ) {
			//FIXME : is a carrying box stored in agent or in field ?
			return true;
		}
		return false;
	}

	public boolean create(Agent me, int x, int y) {
		if(field.isObstacle(x, y)) {
			return false;
		}
		if(x >= me.getX() - 1 && x <= me.getX() + 1 
		&& y >= me.getY() - 1 && y <= me.getY() + 1) {
			field.setAgent(x, y, new Agent(x, y));
			return true;
		}
		return false;
	}

	public void suicide(Agent me) {
		field.removeAgent(me.getX(), me.getY());
	}

	public Surroundings getSurroundings(Agent me, int size) {
		return field.getSurroundings(me.getX(), me.getY(), size);
	}
	public Position getStoreAreaTopCorner() {
		return field.getStoreAreaTopCorner();
	}

	public Position getStoreAreaBottomCorner() {
		return field.getStoreAreaBottomCorner();
	}

	public Position getHomeAreaTopCorner() {
		return field.getHomeAreaTopCorner();
	}

	public Position getHomeAreaBottomCorner() {
		return field.getHomeAreaBottomCorner();
	}
	
	
}
