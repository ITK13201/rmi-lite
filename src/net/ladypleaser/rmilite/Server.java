//$Id: Server.java,v 1.1 2003/06/12 15:53:41 cowboyd Exp $
package net.ladypleaser.rmilite;

import net.ladypleaser.rmilite.impl.RemoteInvocationHandlerImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.HashSet;

public class Server {
	public static final int DEFAULT_PORT = Registry.REGISTRY_PORT;

	private Registry registry;

	public Server() throws RemoteException {
		this(DEFAULT_PORT);
	}

	public Server(int port) throws RemoteException {
		registry = LocateRegistry.createRegistry(port);
	}

	public void publish(Class iface, Object impl, Class[] exportedInterfaces) throws RemoteException {
		RemoteInvocationHandlerImpl handler = new RemoteInvocationHandlerImpl(impl, new HashSet(Arrays.asList(exportedInterfaces)));
		registry.rebind(iface.getName(), handler);
	}
}
