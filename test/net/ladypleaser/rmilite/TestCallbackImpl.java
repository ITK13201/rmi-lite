package net.ladypleaser.rmilite;

public class TestCallbackImpl implements TestCallbackInterface {
	public boolean called = false;

	public void call() {
		called = true;
	}

	public void call(TestCallbackInterface doubleback) {
		doubleback.call();
	}
}
