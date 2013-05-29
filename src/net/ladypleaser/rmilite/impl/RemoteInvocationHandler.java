//$Id: RemoteInvocationHandler.java,v 1.1 2003/06/12 15:53:41 cowboyd Exp $
package net.ladypleaser.rmilite.impl;

import java.rmi.RemoteException;
import java.rmi.Remote;

public interface RemoteInvocationHandler extends Remote {
	Object invoke(String methodName, Class[] paramTypes, Object[] args) throws RemoteException;
}
