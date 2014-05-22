package com.m2dl.fenwicklife.engine;

import java.util.ArrayList;
import java.util.List;

public class Field {
	private List<Agent> listOfAgents;
	private List<Box> listOfBoxes;
	private int firstCorridorY;
	private int secondCorridorX;
	private int sizeX;
	private int sizeY;
	private int centerWallXLeft;
	private int centerWallXRight;
	
	public List<Case> getFieldState() {
		List<Case> fieldState = new ArrayList<Case>();
		for(int i = 0; i < sizeX; i++) {
			for(int j = 0; j < sizeY; j++) {
				if(i == 0 || i == sizeX - 1 || j == 0 || j == sizeY - 1) {
					fieldState.add(new Case(i, j, CaseType.WALL));
				}
				else if(i >= centerWallXLeft && i <= centerWallXRight && j != firstCorridorY && j != secondCorridorX) {
					fieldState.add(new Case(i, j, CaseType.WALL));
				}
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

		this.listOfAgents = new ArrayList<Agent>();
		this.listOfBoxes = new ArrayList<Box>();
	}
	public List<Agent> getListOfAgents() {
		return listOfAgents;
	}
	public void setListOfAgents(List<Agent> listOfAgents) {
		this.listOfAgents = listOfAgents;
	}
	public List<Box> getListOfBoxes() {
		return listOfBoxes;
	}
	public void setListOfBoxes(List<Box> listOfBoxes) {
		this.listOfBoxes = listOfBoxes;
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
