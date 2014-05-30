package com.m2dl.fenwicklife.engine;

import java.util.ArrayList;
import java.util.List;

import com.m2dl.fenwicklife.engine.service.AgentAction;
import com.m2dl.fenwicklife.engine.service.GlobalStatus;
import com.m2dl.fenwicklife.engine.service.IAgentAction;
import com.m2dl.fenwicklife.engine.service.IGlobalStatus;
import com.m2dl.fenwicklife.xmlrpc.ClassInterfaceRelation;
import com.m2dl.fenwicklife.xmlrpc.ClassInterfaceRelationException;
import com.m2dl.fenwicklife.xmlrpc.provider.IProvider;
import com.m2dl.fenwicklife.xmlrpc.provider.ProviderImpl;

public class EngineMain {
	private static IProvider provider;
	public static Engine engine;
	public static void main(String[] args) throws ClassInterfaceRelationException {
		// Retrieve args
		int port = 8081;
		
		// Init engine
		EngineMain.engine = Engine.getInstance();
		
		// Init services list
		List<Object> classesServices = new ArrayList<Object>();
		classesServices.add(new ClassInterfaceRelation(GlobalStatus.class, IGlobalStatus.class));
		classesServices.add(new ClassInterfaceRelation(AgentAction.class, IAgentAction.class));
		provider = new ProviderImpl();
		provider.initProvider(port, classesServices);
	}

}
