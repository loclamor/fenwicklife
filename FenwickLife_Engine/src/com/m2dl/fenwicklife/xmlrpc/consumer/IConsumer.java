package com.m2dl.fenwicklife.xmlrpc.consumer;

/**
 * Interface used by all services like customer that need to call a external
 * service
 * 
 * @author Florian CLAVEL
 * 
 */
public interface IConsumer {
	
	/**
	 * Call a service method and return his result
	 * 
	 * @param serviceFunction
	 *            Function of the service
	 * @param params
	 *            Params of the services
	 * @return Return value of the service
	 */
	public Object consumeService(String serviceFunction, Object[] params);

	public Object consumeService(String serviceFunction);
}
