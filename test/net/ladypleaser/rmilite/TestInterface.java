package net.ladypleaser.rmilite;

public interface TestInterface {
	void call();

	void throwTestException(TestException e) throws TestException;

	void overload(Integer integer);

	void overload(String integer);

	void testCallback(TestCallbackInterface callback, TestValueObject object);

	TestCallbackInterface testReturnCallback();

	TestValueObject returnStupid();

	void testDoubleCallback(TestCallbackInterface callback);

}
