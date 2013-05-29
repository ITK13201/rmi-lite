package net.ladypleaser.rmilite;

import junit.framework.TestCase;

public class ClientServerTest extends TestCase {

	private static final String EXCEPTION_MESSAGE = "this is the exception";
	private static Server server;
	private Client client;
	private TestInterfaceImpl serverTester;
	private TestInterface clientTester;

	public void setUp() throws Exception {
		if (server == null) {
			server = new Server();
		}
		serverTester = new TestInterfaceImpl();
		server.publish(TestInterface.class, serverTester, new Class[] {TestCallbackInterface.class});

		client = new Client("localhost");
		client.exportInterface(TestCallbackInterface.class);
		clientTester = (TestInterface) client.lookup(TestInterface.class);
		assertNotNull("could not find interface", clientTester);
	}

	public void testBasicMethodInvocation() throws Exception {
		clientTester.call();
		assertEquals(true, serverTester.called);
	}

	public void testInvokesOverloadedMethods() throws Exception {
		Integer num = new Integer(10);
		clientTester.overload(num);
		String name = "Rupert";
		clientTester.overload(name);
		assertEquals(num, serverTester.integer);
		assertEquals(name, serverTester.name);
	}

	public void testCallback() throws Exception {
		TestCallbackImpl testCallback = new TestCallbackImpl();
		TestValueObject TestValueObject = new TestValueObject();
		clientTester.testCallback(testCallback, TestValueObject);

		assertEquals(true, testCallback.called);
		assertEquals(false, TestValueObject.called);
	}

	public void testCallbackOnReturnValue() throws Exception {
		TestCallbackInterface callback = clientTester.testReturnCallback();
		TestValueObject stupid = clientTester.returnStupid();
		callback.call();
		stupid.call();
		assertEquals("callback not invoked on server", true, serverTester.returnCallback.called);
		assertEquals("value object invoked on server", false, serverTester.returnStupid.called);
	}

	public void testCallbackOnCallback() throws Exception {
		clientTester.testDoubleCallback(new TestCallbackImpl());
		assertEquals(true, serverTester.doubleCallback.called);
	}

	public void testCheckedExceptionThrownOnRemoteHost() throws Exception {
		TestException prototype = new TestException();
		try {
			clientTester.throwTestException(prototype);
			fail("no exception");
		} catch (TestException e) {
			assertNotSame("hey, this wasn't a remote call", prototype, e);
		}
	}

	public void testCheckedExceptionSubclassThrownOnRemoteHost() throws Exception {
		TestException prototype = new TestException.SubClass();
		try {
			clientTester.throwTestException(prototype);
			fail("no exception");
		} catch (TestException.SubClass e) {
			assertNotSame("hey, this wasn't a remote call", prototype, e);
		}
	}

	public void testRuntimeExceptionThrownOnRemoteHost() throws Exception {
		server.publish(TestInterface.class, new Barfer(new TestRuntimeException()), new Class[0]);
		clientTester = (TestInterface) client.lookup(TestInterface.class);
		try {
			clientTester.call();
			fail("did not throw exception");
		} catch (TestRuntimeException e) {
			assertEquals(EXCEPTION_MESSAGE, e.getMessage());
		}
	}

	public void testErrorThrownOnRemoteHost() throws Exception {
		server.publish(TestInterface.class, new Barfer(new TestError()), new Class[0]);
		clientTester = (TestInterface) client.lookup(TestInterface.class);
		try {
			clientTester.call();
			fail("did not throw exception");
		} catch (TestError e) {
			assertEquals(EXCEPTION_MESSAGE, e.getMessage());
		}
	}

	public static class Barfer extends TestInterfaceImpl {
		private TestRuntimeException rte;
		private Error err;

		public Barfer(TestRuntimeException rte) {
			this.rte = rte;
		}

		public Barfer(Error err) {
			this.err = err;
		}

		public void call() {
			if (rte != null) throw rte;
			throw err;
		}
	}

	public static class TestRuntimeException extends RuntimeException {
		public TestRuntimeException() {
			super(EXCEPTION_MESSAGE);
		}
	}

	public static class TestError extends Error {
		public TestError() {
			super(EXCEPTION_MESSAGE);
		}
	}
}
