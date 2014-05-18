package com.m2dl.fenwicklife.xmlrpc;

/**
 * Class to do the relation between a class and an interface in the provider
 * Enable client to only use interface
 * 
 * @author Florian CLAVEL
 * 
 */
public class ClassInterfaceRelation {
	private Class correspondingInterface;
	private Class correspondingClass;
	
	public ClassInterfaceRelation(Class c1, Class c2) throws ClassInterfaceRelationException {
		if(c1.isInterface() && c2.isInterface()) {
			throw new ClassInterfaceRelationException();
		}
		if(!c1.isInterface() && !c2.isInterface()) {
			throw new ClassInterfaceRelationException();
		}
		if(c1.isInterface()) {
			correspondingInterface = c1;
			correspondingClass = c2;
		}
		else {
			correspondingInterface = c2;
			correspondingClass = c1;
		}
	}

	public Class getCorrespondingInterface() {
		return correspondingInterface;
	}

	public void setCorrespondingInterface(Class correspondingInterface) {
		this.correspondingInterface = correspondingInterface;
	}

	public Class getCorrespondingClass() {
		return correspondingClass;
	}

	public void setCorrespondingClass(Class correspondingClass) {
		this.correspondingClass = correspondingClass;
	}
}
