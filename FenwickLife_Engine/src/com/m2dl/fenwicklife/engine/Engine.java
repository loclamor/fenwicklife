package com.m2dl.fenwicklife.engine;

import java.util.ArrayList;
import java.util.List;
import com.m2dl.fenwicklife.agent.Agent;

public class Engine {
	
	public static final int DEFAULT_SIZE_X = 60;
	public static final int DEFAULT_SIZE_Y = 40;
	public static final int DEFAULT_CENTER_WALL_SIZE = 10;
	public static final int DEFAULT_FIRST_CORRIDOR_Y = 3;
	public static final int DEFAULT_SECOND_CORRIDOR_Y = 6;
	
	
	private List<Agent> listOfAgents;
	private Field field;
	
	private static Engine instance = new Engine(); 
	
	private Engine() {
		this.field = new Field(DEFAULT_SIZE_X, DEFAULT_SIZE_Y, DEFAULT_CENTER_WALL_SIZE,
				DEFAULT_FIRST_CORRIDOR_Y, DEFAULT_SECOND_CORRIDOR_Y);
		
		this.listOfAgents = new ArrayList<Agent>();
	}
	
	public Field getField() {
		return field;
	}
	
	public List<Agent> getAllAgents() {
		return this.listOfAgents;
	}
	
	public static Engine getInstance() {
		return instance;
	}

	public boolean hello(Agent me) {
		if(!listOfAgents.contains(me) && !field.isObstacle(me.getX(), me.getY())) {
			listOfAgents.add(me);
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
			field.setTileType(TileType.EMPTY, me.getX(), me.getY());
			if(me.isCarryingBox()) {
				field.setTileType(TileType.AGENTWITHBOX, to_x, to_y);
			}
			else {
				field.setTileType(TileType.AGENT, to_x, to_y);
			}
			return true;
		}
		return false;
	}

	public boolean takeBox(Agent me) {
		if(field.getTileType(me.getX(), me.getY()) == TileType.BOX) {
			//TODO
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
			if(me.isCarryingBox()) {
				field.setTileType(TileType.AGENT, x, y);
				// TODO
			}
			return true;
		}
		return false;
	}

	public void suicide(Agent me) {
		if(!listOfAgents.contains(me)) {
			listOfAgents.remove(me);
		}
		field.setTileType(TileType.EMPTY, me.getX(), me.getY());
	}

	public Tile[][] getSurroundings(Agent me, int size) {
		return field.getSurroundings(me.getX(), me.getY(), size);
	}
	
}
