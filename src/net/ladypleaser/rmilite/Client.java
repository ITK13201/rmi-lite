//$Id: Client.java,v 1.1 2003/06/12 15:53:41 cowboyd Exp $
package net.ladypleaser.rmilite;

import net.ladypleaser.rmilite.impl.LocalInvocationHandlerImpl;
import net.ladypleaser.rmilite.impl.RemoteInvocationHandler;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.HashSet;
import java.util.Set;

public class Client {
	private Set exportedInterfaces = new HashSet();
	private String serverHost;
	private int serverPort;

	public Client(String serverHost) {
		this(serverHost, Server.DEFAULT_PORT);
	}

	public Client(String serverHost, int serverPort) {
		this.serverHost = serverHost;
		this.serverPort = serverPort;
	}

	public void exportInterface(Class iface) {
		exportedInterfaces.add(iface);
	}

	public Object lookup(Class iface) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(serverHost, serverPort);
		RemoteInvocationHandler remote = (RemoteInvocationHandler) registry.lookup(iface.getName());
		return LocalInvocationHandlerImpl.create(iface, remote, exportedInterfaces);
	}
}
