package net.ladypleaser.rmilite;

public class TestInterfaceImpl implements TestInterface {
	public boolean called = false;
	public Integer integer;
	public String name;
	public TestCallbackImpl returnCallback = new TestCallbackImpl();
	public TestValueObject returnStupid = new TestValueObject();
	public TestCallbackImpl doubleCallback;

	public void call() {
		called = true;
	}

	public void throwTestException(TestException e) throws TestException {
		throw e;
	}

	public void overload(Integer integer) {
		this.integer = integer;
	}

	public void overload(String name) {
		this.name = name;
	}

	public void testCallback(TestCallbackInterface callback, TestValueObject stupid) {
		callback.call();
		stupid.call();
	}

	public void testDoubleCallback(TestCallbackInterface callback) {
		doubleCallback = new TestCallbackImpl();
		callback.call(doubleCallback);
	}

	public TestCallbackInterface testReturnCallback() {
		return returnCallback;
	}

	public TestValueObject returnStupid() {
		return returnStupid;
	}

}
