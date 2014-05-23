package com.m2dl.fenwicklife.xmlrpc.consumer;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

/**
 * Implements IConsumer
 * 
 * @author Florian CLAVEL
 * 
 */
public class ConsumerImpl implements IConsumer {

	@Override
	public Object consumeService(String adress, int port, Class serviceClass,
			String serviceFunction, Object[] params) {

		System.out.println("[Customer] : Try to consume :");
		System.out.println("\t The function " + serviceFunction + " from service "
				+ serviceClass.getSimpleName() + " on server " + adress + ":"
				+ port + " with params " + params + ".");

		// Create the configuration
		XmlRpcClientConfigImpl configRepartiteur = new XmlRpcClientConfigImpl();
		try {
			configRepartiteur.setServerURL(new URL("http://" + adress + ":"
					+ port + "/xmlrpc"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("[Customer] : Can't connect to the service / server.");
			return null;
		}
		configRepartiteur.setEnabledForExtensions(true);
		configRepartiteur.setConnectionTimeout(60 * 1000);
		configRepartiteur.setReplyTimeout(60 * 1000);

		// Create the rpc client
		XmlRpcClient client = new XmlRpcClient();
		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
		client.setConfig(configRepartiteur);

		// Call the service's function
		Object result;
		try {
			result = client.execute(serviceClass.getName() + "."
					+ serviceFunction, params);
		} catch (XmlRpcException e) {
			e.printStackTrace();
			System.err
					.println("[Customer] : Can't use the service, maybe the function doesn't exist.");
			return null;
		}
		System.out.println("[Customer] : Provider " + adress + ":" + port + " respond" ); // + result);
		return result;
	}

	// See above (default adress and port)
	public Object consumeService(Class serviceClass, String serviceFunction,
			Object[] params) {
		return consumeService("127.0.0.1", 8080, serviceClass, serviceFunction,
				params);
	}

	// See above (default adress and port, no params)
	public Object consumeService(Class serviceClass, String serviceFunction) {
		return consumeService("127.0.0.1", 8080, serviceClass, serviceFunction);
	}

	// See above (default port)
	public Object consumeService(String adress, Class serviceClass,
			String serviceFunction, Object[] params) {
		return consumeService(adress, 8080, serviceClass, serviceFunction,
				params);
	}

	// See above (default port, no params)
	public Object consumeService(String adress, Class serviceClass,
			String serviceFunction) {
		return consumeService(adress, 8080, serviceClass, serviceFunction);
	}

	// See above (no params)
	public Object consumeService(String adress, int port, Class serviceClass,
			String serviceFunction) {
		return consumeService(adress, port, serviceClass, serviceFunction,
				new Object[0]);
	}
}
