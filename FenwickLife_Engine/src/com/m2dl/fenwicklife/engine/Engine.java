package com.m2dl.fenwicklife.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Engine {
	
	public static final int DEFAULT_SIZE_X = 60;
	public static final int DEFAULT_SIZE_Y = 40;
	public static final int DEFAULT_CENTER_WALL_SIZE = 10;
	public static final int DEFAULT_FIRST_CORRIDOR_Y = 3;
	public static final int DEFAULT_SECOND_CORRIDOR_Y = 6;
	
	
	private List<Agent> listOfAgents;
	private List<Box> listOfBoxes;
	private Field field;
	
	private static Engine instance = new Engine(); 
	
	private Engine() {
		this.field = new Field(DEFAULT_SIZE_X, DEFAULT_SIZE_Y, DEFAULT_CENTER_WALL_SIZE,
				DEFAULT_FIRST_CORRIDOR_Y, DEFAULT_SECOND_CORRIDOR_Y);
		
		this.listOfAgents = new ArrayList<Agent>();
		this.listOfBoxes = new ArrayList<Box>();
	}
	
	public Field getField() {
		return field;
	}
	
	public List<Agent> getAllAgents() {
		return this.listOfAgents;
	}
	
	public List<Box> getAllBoxes() {
		return this.listOfBoxes;
	}
	
	public static Engine getInstance() {
		return instance;
	}
	
	public Map<Position, TileType> getFieldState() {
		return field.getFieldState();
	}
	
}
