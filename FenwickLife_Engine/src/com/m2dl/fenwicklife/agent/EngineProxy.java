package com.m2dl.fenwicklife.agent;

import com.m2dl.fenwicklife.engine.Tile;

public class EngineProxy implements IEngineProxy {

	private static EngineProxy instance = null;
	
	private EngineProxy() {
		// TODO Auto-generated constructor stub
	}
	
	public static EngineProxy getInstance(){
		if( instance == null ) {
			instance = new EngineProxy();
		}
		return instance;
	}
	
	//xmlRpc call stand there
	
	@Override
	public boolean hello(Agent me) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean move(Agent me, int to_x, int to_y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean takeBox(Agent me) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Tile[][] getSurroundings(Agent me) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tile[][] getSurroundings(Agent me, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean create(Agent me, int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void suicide(Agent me) {
		// TODO Auto-generated method stub
		
	}

	
}
