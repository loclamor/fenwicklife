package com.m2dl.fenwicklife.ui;

import com.m2dl.fenwicklife.engine.service.IUIAction;
import com.m2dl.fenwicklife.xmlrpc.consumer.ConsumerImpl;
import com.m2dl.fenwicklife.xmlrpc.consumer.IConsumer;

public class UIEngineProxy implements IUIEngineProxy {
	
	private static UIEngineProxy instance = null;
 	
	private IConsumer consumer;
	
	private UIEngineProxy( String serverAdress, int serverPort) {
		this.consumer = new ConsumerImpl(serverAdress, serverPort, IUIAction.class);
		System.out.println("UIEngineProxy consumer initialised with " + serverAdress + ":" + serverPort);
	}
	
	public static UIEngineProxy getInstance(){
		return getInstance("127.0.0.1", 8081);
	}
	
	public static UIEngineProxy getInstance(String serverAdress, int serverPort){
		if( instance == null ) {
			instance = new UIEngineProxy( serverAdress, serverPort );
		}
		return instance;
	}

	//xmlRpc call stand there
	
	@Override
	public boolean pause() {
		consumer.consumeService( "pause" );
		return true;
	}

}
