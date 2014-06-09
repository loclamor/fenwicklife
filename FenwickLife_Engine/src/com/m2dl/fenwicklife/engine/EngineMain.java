package com.m2dl.fenwicklife.engine;

import java.util.ArrayList;
import java.util.List;

import com.m2dl.fenwicklife.engine.service.AgentAction;
import com.m2dl.fenwicklife.engine.service.GlobalStatus;
import com.m2dl.fenwicklife.engine.service.IAgentAction;
import com.m2dl.fenwicklife.engine.service.IGlobalStatus;
import com.m2dl.fenwicklife.engine.service.IUIAction;
import com.m2dl.fenwicklife.engine.service.UIAction;
import com.m2dl.fenwicklife.xmlrpc.ClassInterfaceRelation;
import com.m2dl.fenwicklife.xmlrpc.ClassInterfaceRelationException;
import com.m2dl.fenwicklife.xmlrpc.provider.IProvider;
import com.m2dl.fenwicklife.xmlrpc.provider.ProviderImpl;

public class EngineMain {
	private static IProvider provider;
	public static Engine engine;
	
	public static int port = 8081;
	public static int execSpeed = 100;
	
	public static void main(String[] args) throws ClassInterfaceRelationException {
		
		if (args.length == 0) {
			System.out
					.println("Options are : [ port [, execSpedd ]]");
			System.out.println("\tport :\t int, optional, default "
					+ port);
			System.out.println("\texecSpeed :\t int (ms), optional, default "
					+ execSpeed);
		}
		
		
		
		if (args.length >= 1) {
			port = new Integer(args[0]).intValue();
		}
		if (args.length >= 2) {
			execSpeed = new Integer(args[1]).intValue();
		}
		
		// Init engine
		EngineMain.engine = Engine.getInstance(execSpeed);
		
		// Init services list
		List<Object> classesServices = new ArrayList<Object>();
		classesServices.add(new ClassInterfaceRelation(GlobalStatus.class, IGlobalStatus.class));
		classesServices.add(new ClassInterfaceRelation(AgentAction.class, IAgentAction.class));
		classesServices.add(new ClassInterfaceRelation(UIAction.class, IUIAction.class));
		
		provider = new ProviderImpl();
		provider.initProvider(port, classesServices);
	}

}
