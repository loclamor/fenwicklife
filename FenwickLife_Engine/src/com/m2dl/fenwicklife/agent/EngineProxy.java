package com.m2dl.fenwicklife.agent;

import com.m2dl.fenwicklife.Position;
import com.m2dl.fenwicklife.xmlrpc.consumer.ConsumerImpl;
import com.m2dl.fenwicklife.xmlrpc.consumer.IConsumer;
import com.m2dl.fenwicklife.xmlrpc.messages.SimpleAgent;
import com.m2dl.fenwicklife.xmlrpc.messages.Surroundings;
import com.m2dl.fenwicklife.engine.service.IAgentAction;

public class EngineProxy implements IEngineProxy {

	private static EngineProxy instance = null;
	
	private IConsumer consumer;
	
	private EngineProxy( String serverAdress, int serverPort) {
		this.consumer = new ConsumerImpl(serverAdress, serverPort, IAgentAction.class);
		System.out.println("EngineProxy consumer initialised with " + serverAdress + ":" + serverPort);
	}
	
	public static EngineProxy getInstance(){
		return getInstance("127.0.0.1", 8080);
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
		Object[] paramsIntern = { new SimpleAgent(me) };
		return (boolean)consumer.consumeService( "hello", paramsIntern );
	}

	@Override
	public boolean move(Agent me, int to_x, int to_y) {
		Object[] paramsIntern = { new SimpleAgent(me), to_x, to_y };
		return (boolean)consumer.consumeService( "move", paramsIntern );
	}

	@Override
	public boolean takeBox(Agent me) {
		Object[] paramsIntern = { new SimpleAgent(me) };
		return (boolean)consumer.consumeService( "takeBox", paramsIntern );
	}
	
	@Override
	public boolean dropBox(Agent me) {
		Object[] paramsIntern = { new SimpleAgent(me) };
		return (boolean)consumer.consumeService( "dropBox", paramsIntern );
	}

	@Override
	public Surroundings getSurroundings(Agent me) {
		Object[] paramsIntern = { new SimpleAgent(me) };
		return (Surroundings)consumer.consumeService( "getSurroundings", paramsIntern );
	}

	@Override
	public Surroundings getSurroundings(Agent me, int size) {
		Object[] paramsIntern = { new SimpleAgent(me), size };
		return (Surroundings) consumer.consumeService( "getSurroundings", paramsIntern );
	}

	@Override
	public boolean create(Agent me, int x, int y) {
		Object[] paramsIntern = { new SimpleAgent(me), x, y };
		return (boolean)consumer.consumeService( "create", paramsIntern );
	}

	@Override
	public boolean suicide(Agent me) {
		Object[] paramsIntern = { new SimpleAgent(me) };
		return (boolean) consumer.consumeService( "suicide", paramsIntern );
	}

	public Position getStoreAreaTopCorner() {
		Object[] paramsIntern = { true };
		return (Position)consumer.consumeService( "getStoreAreaTopCorner", paramsIntern );
	}

	public Position getStoreAreaBottomCorner() {
		Object[] paramsIntern = { true };
		return (Position)consumer.consumeService( "getStoreAreaBottomCorner", paramsIntern );
	}

	public Position getHomeAreaTopCorner() {
		Object[] paramsIntern = { true };
		return (Position)consumer.consumeService( "getHomeAreaTopCorner", paramsIntern );
	}

	public Position getHomeAreaBottomCorner() {
		Object[] paramsIntern = { true };
		return (Position)consumer.consumeService( "getHomeAreaBottomCorner", paramsIntern );
	}

	@Override
	public boolean isInPauseMode() {
		return (boolean) consumer.consumeService( "isInPauseMode" );
	}

	@Override
	public int getSpeed() {
		return (int) consumer.consumeService( "getSpeed" );
	}
}
