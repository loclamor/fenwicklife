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
	 * @param adress
	 *            Adress of the service
	 * @param port
	 *            Port of the service
	 * @param serviceClass
	 *            Class of the service
	 * @param serviceFunction
	 *            Function of the service
	 * @param params
	 *            Params of the services
	 * @return Return value of the service
	 */
	public abstract Object consumeService(String adress, int port,
			Class serviceClass, String serviceFunction, Object[] params);

	// See above (default adress and port)
	public Object consumeService(Class serviceClass, String serviceFunction,
			Object[] params);

	// See above (default adress and port, no params)
	public Object consumeService(Class serviceClass, String serviceFunction);

	// See above (default port)
	public Object consumeService(String adress, Class serviceClass,
			String serviceFunction, Object[] params);

	// See above (default port, no params)
	public Object consumeService(String adress, Class serviceClass,
			String serviceFunction);

	// See above (no params)
	public Object consumeService(String adress, int port, Class serviceClass,
			String serviceFunction);
}
