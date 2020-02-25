package com.dn.spring.aop.advisor;

public interface Advisor {

	String getAdviceBeanName();

	String getExpression();
}
