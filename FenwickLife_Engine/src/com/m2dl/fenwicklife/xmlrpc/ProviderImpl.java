package com.m2dl.fenwicklife.xmlrpc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

/**
 * Implements IProvider
 * 
 * @author Florian CLAVEL
 * 
 */
public class ProviderImpl implements IProvider {

	private String ipAdress;

	/**
	 * Method call to init the provider
	 * 
	 * @param Port
	 *            The port used to launch the server
	 * @param classesServices
	 *            Class of the provided services
	 */
	public void initProvider(int port, List<Object> classesServices) {
		System.out.println("[Provider] : Start server on port " + port);

		// Create the server
		WebServer webServer = new WebServer(port);
		XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

		// Handle the services class
		PropertyHandlerMapping phm = new PropertyHandlerMapping();
		for (Object classService : classesServices) {
			try {
				if(classService instanceof ClassInterfaceRelation) {
					ClassInterfaceRelation relTmp = (ClassInterfaceRelation)classService;
					phm.addHandler(relTmp.getCorrespondingInterface().getName(), relTmp.getCorrespondingClass());
					System.out.println("[Provider] : Now handle " + relTmp.getCorrespondingInterface().getSimpleName());
				}
				else if(classService instanceof Class) {
					Class classTmp = (Class)classService;
					if(classTmp.isInterface()) {
						System.err.println("[Provider] : Can't handle an interface " + classTmp.getSimpleName());
					}
					else {
						phm.addHandler(classTmp.getName(), classTmp);
						System.out.println("[Provider] : Now handle " + classTmp.getSimpleName());
					}
				} 
				else {
					System.err.println("[Provider] : An object can't be handled");
				}
			} catch (XmlRpcException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("[Provider] : Can't handle " + classService);
			}
		}
		xmlRpcServer.setHandlerMapping(phm);
		XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer
				.getConfig();
		serverConfig.setEnabledForExtensions(true);
		serverConfig.setContentLengthOptional(false);

		// Start the server
		System.out.println("[Provider] : Finish to configure services");
		System.out.println("[Provider] : Available functions :");
		try {
			for (String line : phm.getListMethods()) {
				System.out.println("\t" + line);
			}
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("[Provider] : Can't list functions.");
		}
		try {
			webServer.start();
			System.out.println("[Provider] : Server started.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err
					.println("[Provider] :  Can't start server");
		}

		// Retrieve and save server adress
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			// Get IP Address
			byte[] adress = addr.getAddress();
			ipAdress = convertRawToIpAddress(adress);
			System.out.println("[Provider] : Launch on adress " + ipAdress);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("[Provider] : Can't retrieve his adress.");
		}
	}

	/**
	 * Convert an byte format adress to string
	 * 
	 * @param rawBytes
	 *            Byte format ip adress
	 * @return Ip adress string formatted
	 */
	public String convertRawToIpAddress(byte[] rawBytes) {
		int i = 4;
		String ipAddress = "";
		for (byte raw : rawBytes) {
			ipAddress += (raw & 0xFF);
			if (--i > 0) {
				ipAddress += ".";
			}
		}
		return ipAddress;
	}

	
	/**
	 * Return the ip adress
	 * @return String of the ip adress
	 */
	public String getIPAdress() {
		return ipAdress;
	}
}
