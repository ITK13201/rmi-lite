package net.ladypleaser.rmilite;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.io.PrintStream;

public class RemoteInvocationException extends RuntimeException {

	private Throwable cause;

	public RemoteInvocationException(String method, Throwable cause) {
		super("error invoking " + method + ": " + cause);
		this.cause = cause;
	}

	public Throwable getCause() {
		return cause;
	}

	public void printStackTrace(PrintStream stream) {
		super.printStackTrace(stream);
		if (cause != null) {
			stream.println("caused by: ");
			cause.printStackTrace(stream);
		}
	}

	public static void rethrow(Method method, RemoteInvocationException e) throws Throwable {
		Throwable cause = e.getCause();
		if (cause instanceof RuntimeException) {
			throw (RuntimeException) cause;
		}
		if (cause instanceof Error) {
			throw (Error) cause;
		}
		Class[] exceptionTypes = method.getExceptionTypes();
		for (int i = 0; i < exceptionTypes.length; i++) {
			if (exceptionTypes[i].isAssignableFrom(cause.getClass())) {
				throw cause;
			}
		}
		throw e;
	}

	public static void rethrow(Method method, RemoteException e) {
		if (e.detail != null) {
			throw new RemoteInvocationException(method.getName(), e);
		}
		throw new RemoteInvocationException(method.getName(), e);
	}
}
