//$Id: RemoteInvocationHandlerImpl.java,v 1.2 2003/06/21 13:57:20 cowboyd Exp $
package net.ladypleaser.rmilite.impl;

import net.ladypleaser.rmilite.RemoteInvocationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

public class RemoteInvocationHandlerImpl extends UnicastRemoteObject implements RemoteInvocationHandler {
	private Object impl;
	private Set exportedInterfaces;

	public RemoteInvocationHandlerImpl(Object impl, Set exportedInterfaces) throws RemoteException {
		this.impl = impl;
		this.exportedInterfaces = exportedInterfaces;
	}

	public Object invoke(String methodName, Class[] paramTypes, Object[] args) throws RemoteException {
		try {
			if (args == null) {
				args = new Object[0];
			}
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof RemoteInvocationHandler) {
					RemoteInvocationHandler handler = (RemoteInvocationHandler) args[i];
					args[i] = LocalInvocationHandlerImpl.create(paramTypes[i], handler, exportedInterfaces);
				}
			}
			Method method = impl.getClass().getMethod(methodName, paramTypes);
			Object returnValue = method.invoke(impl, args);

			if (exportedInterfaces.contains(method.getReturnType())) {
				returnValue = RemoteObject.toStub(new RemoteInvocationHandlerImpl(returnValue, exportedInterfaces));
			}

			return returnValue;
		} catch (InvocationTargetException e) {
			throw new RemoteInvocationException(methodName,e.getTargetException());
		} catch (Exception e) {
			throw new RemoteInvocationException(methodName, e);
		}
	}
}
