package com.m2dl.fenwicklife.xmlrpc;

/**
 * Exception from ClassInterfaceRelation
 * 
 * @author Florian CLAVEL
 * 
 */
public class ClassInterfaceRelationException extends Exception{
	public ClassInterfaceRelationException() {
		super("ClassInterfaceRelation must be construct with an interface and a class");
	}
}
