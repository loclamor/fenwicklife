package com.m2dl.fenwicklife.engine;

import java.util.ArrayList;
import java.util.List;

import com.m2dl.fenwicklife.xmlrpc.ClassInterfaceRelation;
import com.m2dl.fenwicklife.xmlrpc.ClassInterfaceRelationException;
import com.m2dl.fenwicklife.xmlrpc.FenwickGlobalStatus;
import com.m2dl.fenwicklife.xmlrpc.IFenwickGlobalStatus;
import com.m2dl.fenwicklife.xmlrpc.IProvider;
import com.m2dl.fenwicklife.xmlrpc.ProviderImpl;

public class Main {
	private static IProvider provider;
	public static Field field;
	public static void main(String[] args) throws ClassInterfaceRelationException {
		// Retrieve args
		int port = 8081;

		// Init the field
		Main.field = new Field(60, 40, 10, 3, 6);
		
		// Init services list
		List<Object> classesServices = new ArrayList<Object>();
		classesServices.add(new ClassInterfaceRelation(FenwickGlobalStatus.class, IFenwickGlobalStatus.class));
		provider = new ProviderImpl();
		provider.initProvider(port, classesServices);
	}

}
