package com.m2dl.fenwicklife.agent;

import com.m2dl.fenwicklife.engine.Tile;
import com.m2dl.fenwicklife.xmlrpc.consumer.ConsumerImpl;
import com.m2dl.fenwicklife.xmlrpc.consumer.IConsumer;
import com.m2dl.fenwicklife.engine.service.IAgentAction;

public class EngineProxy implements IEngineProxy {

	private static EngineProxy instance = null;
	
	private String serverAdress;
	private int serverPort;
	
	private IConsumer consumer;
	
	private EngineProxy( String serverAdress, int serverPort) {
		this.serverAdress = serverAdress;
		this.serverPort = serverPort;
		
		this.consumer = new ConsumerImpl(serverAdress, serverPort, IAgentAction.class);
	}
	
	public static EngineProxy getInstance(){
		if( instance == null ) {
			instance = new EngineProxy("127.0.0.1", 8080);
		}
		return instance;
	}
	public static EngineProxy getInstance(String serverAdress, int serverPort){
		if( instance == null ) {
			instance = new EngineProxy( serverAdress, serverPort );
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
	public boolean dropBox(Agent me) {
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
