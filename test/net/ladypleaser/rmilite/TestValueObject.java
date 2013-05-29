package net.ladypleaser.rmilite;

import java.io.Serializable;

public class TestValueObject implements Serializable {
	public boolean called = false;

	public void call() {
		called = true;
	}

}
