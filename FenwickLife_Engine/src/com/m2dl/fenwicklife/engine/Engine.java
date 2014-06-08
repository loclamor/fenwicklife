package com.m2dl.fenwicklife.engine;

import com.m2dl.fenwicklife.Position;
import com.m2dl.fenwicklife.engine.service.IAgentAction;
import com.m2dl.fenwicklife.xmlrpc.messages.SimpleAgent;
import com.m2dl.fenwicklife.xmlrpc.messages.Surroundings;

public class Engine implements IAgentAction {

	public static final int DEFAULT_SIZE_X = 60;
	public static final int DEFAULT_SIZE_Y = 40;
	public static final int DEFAULT_CENTER_WALL_SIZE = 10;
	public static final int DEFAULT_FIRST_CORRIDOR_Y = 3;
	public static final int DEFAULT_SECOND_CORRIDOR_Y = 36;
	public static final int DEFAULT_STORE_HOME_WIDTH = 10;
	public static final int DEFAULT_STORE_HOME_HEIGHT = 20;
	public static final int SPEED_STEP = 25; // time between each action in ms
	public static final int MAX_SPEED = 50; // time between each action in ms
	public static final int MIN_SPEED = 3000; // time between each action in ms
	public static final int DEFAULT_SPEED = 100;

	private Field field;
	private boolean isInPauseMode = false;
	private int speed = 100; // time between each action in ms

	private static Engine instance = new Engine();

	private Engine() {
		this.field = new Field(DEFAULT_SIZE_X, DEFAULT_SIZE_Y,
				DEFAULT_CENTER_WALL_SIZE, DEFAULT_FIRST_CORRIDOR_Y,
				DEFAULT_SECOND_CORRIDOR_Y, DEFAULT_STORE_HOME_WIDTH,
				DEFAULT_STORE_HOME_HEIGHT);
	}

	public Field getField() {
		return field;
	}

	public static Engine getInstance() {
		return instance;
	}

	public boolean hello(SimpleAgent me) {
		if (!field.isObstacle(me.getX(), me.getY())) {
			field.setAgent(me);
			return true;
		}
		return false;
	}

	public boolean move(SimpleAgent me, int to_x, int to_y) {
		if (field.isObstacle(to_x, to_y)) {
			return false;
		}
		if (to_x >= me.getX() - 1 && to_x <= me.getX() + 1
				&& to_y >= me.getY() - 1 && to_y <= me.getY() + 1) {
			// move the agent
			field.removeAgent(me);
			field.setAgent(to_x, to_y, me);
			return true;
		}
		return false;
	}

	public boolean takeBox(SimpleAgent me) {
		if (field.getTile(me.getX(), me.getY()).hasBox()) {
			field.removeBox(me.getX(), me.getY());
			field.setAgent(me);
			return true;
		}
		return false;
	}

	public boolean dropBox(SimpleAgent me) {
		Tile t = field.getTile(me.getX(), me.getY());
		if (!t.hasBox() && t instanceof Home && t.hasAgentWithBox()
				&& me.isCarryingBox()) {
			field.setBox(t.getPosition());
			return true;
		}
		return false;
	}

	public boolean create(SimpleAgent me, int x, int y) {
		if (field.isObstacle(x, y)) {
			return false;
		}
		if (x >= me.getX() - 1 && x <= me.getX() + 1 && y >= me.getY() - 1
				&& y <= me.getY() + 1) {
			field.setAgent(x, y, new SimpleAgent(x, y));
			return true;
		}
		return false;
	}

	public boolean suicide(SimpleAgent me) {
		if (me.isCarryingBox()) {
			System.err.println("Engine : Try to remove an agent with box !");
			return false;
		}
		return field.removeAgent(me.getX(), me.getY());
	}

	public Surroundings getSurroundings(SimpleAgent me, int size) {
		return field.getSurroundings(me.getX(), me.getY(), size);
	}

	public Position getStoreAreaTopCorner(boolean please) {
		return field.getStoreAreaTopCorner();
	}

	public Position getStoreAreaBottomCorner(boolean please) {
		return field.getStoreAreaBottomCorner();
	}

	public Position getHomeAreaTopCorner(boolean please) {
		return field.getHomeAreaTopCorner();
	}

	public Position getHomeAreaBottomCorner(boolean please) {
		return field.getHomeAreaBottomCorner();
	}

	@Override
	public Surroundings getSurroundings(SimpleAgent me) {
		return getSurroundings(me, 3);
	}

	public int getWidth() {
		return DEFAULT_SIZE_X;
	}

	public int getHeight() {
		return DEFAULT_SIZE_Y;
	}

	@Override
	public boolean isInPauseMode() {
		return isInPauseMode;
	}

	public boolean setPauseMode(boolean pause) {
		this.isInPauseMode = pause;
		return this.isInPauseMode;
	}

	public void increaseSpeed() {
		if (this.speed > MAX_SPEED) {
			this.speed -= SPEED_STEP;
		}
	}

	public void decreaseSpeed() {
		if (this.speed < MIN_SPEED) {
			this.speed += SPEED_STEP;
		}
	}

	@Override
	public int getSpeed() {
		return this.speed;
	}
}
