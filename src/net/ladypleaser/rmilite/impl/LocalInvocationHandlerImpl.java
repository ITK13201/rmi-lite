//$Id: LocalInvocationHandlerImpl.java,v 1.4 2003/06/30 11:58:10 cowboyd Exp $
package net.ladypleaser.rmilite.impl;

import net.ladypleaser.rmilite.RemoteInvocationException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.util.Set;
import java.util.ArrayList;

public class LocalInvocationHandlerImpl implements InvocationHandler {
	private RemoteInvocationHandler handler;
	private Set exportedInterfaces;

	public LocalInvocationHandlerImpl(RemoteInvocationHandler handler, Set exportedInterfaces) {
		this.handler = handler;
		this.exportedInterfaces = exportedInterfaces;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//keep a reference to remote invocation handlers that are created as a result of this method invocation
		//so that they will not be garbage collected
		ArrayList keeparound = new ArrayList();

		Class[] parameterTypes = method.getParameterTypes();
		if (args == null) {
			args = new Object[0];
		}
		for (int i = 0; i < parameterTypes.length; i++) {
			Class type = parameterTypes[i];
			if (exportedInterfaces.contains(type)) {
				RemoteInvocationHandlerImpl obj = new RemoteInvocationHandlerImpl(args[i], exportedInterfaces);
				keeparound.add(obj);
				args[i] = RemoteObject.toStub(obj);
			}
		}
		Object returnValue = invokeRemote(method, parameterTypes, args);
		if (returnValue instanceof RemoteInvocationHandler) {
			returnValue = LocalInvocationHandlerImpl.create(method.getReturnType(), (RemoteInvocationHandler) returnValue, exportedInterfaces);
		}
		return returnValue;
	}

	private Object invokeRemote(Method method, Class[] parameterTypes, Object[] args) throws Throwable {
		try {
			return handler.invoke(method.getName(), parameterTypes, args);
		} catch (RemoteInvocationException e) {
			RemoteInvocationException.rethrow(method, e);
			throw new Error("should have thrown an exception in the previous statement");
		} catch (RemoteException e) {
			RemoteInvocationException.rethrow(method, e);
			throw new Error("should have thrown an exception in the previous statement");
		}
	}

	public static Object create(Class iface, RemoteInvocationHandler remote, Set exportedInterfaces) {
		return Proxy.newProxyInstance(LocalInvocationHandlerImpl.class.getClassLoader(), new Class[]{iface}, new LocalInvocationHandlerImpl(remote, exportedInterfaces));
	}


}
