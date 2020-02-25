package com.study.design.mode.samples.responsibility;

public interface Responsibility {

	void process(Request request, ResponsibilityChain chain);
}
