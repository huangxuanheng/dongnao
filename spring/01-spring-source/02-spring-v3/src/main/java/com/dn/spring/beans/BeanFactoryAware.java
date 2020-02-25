package com.dn.spring.beans;

public interface BeanFactoryAware extends Aware {

	void setBeanFactory(BeanFactory bf);
}
