package com.m2dl.fenwicklife.xmlrpc;

import java.util.List;

/**
 * Interface used by all services like server that need to provide services
 * 
 * @author Florian CLAVEL
 * 
 */
public interface IProvider {

	/**
	 * Method call to init the provider
	 * 
	 * @param Port
	 *            The port used to launch the server
	 * @param classesServices
	 *            Class of the provided services
	 */
	void initProvider(int port, List<Object> classesServices);

	/**
	 * Convert an byte format adress to string
	 * 
	 * @param rawBytes
	 *            Byte format ip adress
	 * @return Ip adress string formatted
	 */
	String convertRawToIpAddress(byte[] rawBytes);

	/**
	 * Return the ip adress
	 * 
	 * @return String of the ip adress
	 */
	String getIPAdress();
}
