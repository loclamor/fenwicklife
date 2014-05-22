package com.m2dl.fenwicklife.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Field {
	private Map<Position, Active> listOfActive;
	private int firstCorridorY;
	private int secondCorridorX;
	private int sizeX;
	private int sizeY;
	private int centerWallXLeft;
	private int centerWallXRight;
	
	public boolean isObstacle(int x, int y) {
		return isObstacle(new Position(x, y));
	}
	
	public boolean isObstacle(Position p) {
		if(p.getX() == 0 || p.getX() == sizeX - 1 || p.getY() == 0 || p.getY() == sizeY - 1) {
			return true;
		}
		else if(p.getX() >= centerWallXLeft && p.getX() <= centerWallXRight && p.getY() != firstCorridorY && p.getY() != secondCorridorX) {
			return true;
		}
		return listOfActive.containsKey(p);
	}
	
	public TileType getTileType(int x, int y) {
		return getTileType(new Position(x, y));
	}
	
	public TileType getTileType(Position p) {
		if(p.getX() == 0 || p.getX() == sizeX - 1 || p.getY() == 0 || p.getY() == sizeY - 1) {
			return TileType.WALL;
		}
		else if(p.getX() >= centerWallXLeft && p.getX() <= centerWallXRight && p.getY() != firstCorridorY && p.getY() != secondCorridorX) {
			return TileType.WALL;
		}
		Active a = listOfActive.get(p);
		if(a instanceof Agent) {
			if(((Agent)a).isCarryingBox()) {
				return TileType.AGENTWITHBOX;
			}
			return TileType.AGENT;
		}
		else if(a instanceof Box) {
			return TileType.BOX;
		}
		return TileType.EMPTY;
	}
	
	public Map<Position, TileType> getFieldState() {
		Map<Position, TileType> fieldState = new HashMap<Position, TileType>();
		for(int i = 0; i < sizeX; i++) {
			for(int j = 0; j < sizeY; j++) {
				fieldState.put(new Position(i,  j), getTileType(i, j));
			}
		}
		return fieldState;
	}

	public Field(int sizeX, int sizeY, int centerWallSize, int firstCorridorY, int secondCorridorX) {
		super();
		this.firstCorridorY = firstCorridorY;
		this.secondCorridorX = secondCorridorX;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.centerWallXLeft   = sizeX / 2 - centerWallSize;
		this.centerWallXRight  = sizeX / 2 + centerWallSize;

		this.listOfActive = new HashMap<Position, Active>();
	}
	public List<Agent> getListOfAgents() {
		Collection<Active> actives = listOfActive.values();
		List<Agent> listOfAgents = new ArrayList<Agent>();
		for(Active a : actives) {
			if(a instanceof Agent) {
				listOfAgents.add((Agent) a);
			}
		}
		return listOfAgents;
	}

	public List<Box> getListOfBoxes() {
		Collection<Active> actives = listOfActive.values();
		List<Box> listOfBoxes = new ArrayList<Box>();
		for(Active a : actives) {
			if(a instanceof Box) {
				listOfBoxes.add((Box) a);
			}
		}
		return listOfBoxes;
	}

	public int getFirstCorridorY() {
		return firstCorridorY;
	}
	public void setFirstCorridorY(int firstCorridorY) {
		this.firstCorridorY = firstCorridorY;
	}
	public int getSecondCorridorX() {
		return secondCorridorX;
	}
	public void setSecondCorridorX(int secondCorridorX) {
		this.secondCorridorX = secondCorridorX;
	}
}
