package com.dn.spring.samples;

public interface Driver {

	void start();

	default void stop() {

	}
}
