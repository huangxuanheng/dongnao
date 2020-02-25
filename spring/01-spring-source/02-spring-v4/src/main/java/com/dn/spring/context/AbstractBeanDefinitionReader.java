package com.dn.spring.context;

import com.dn.spring.beans.BeanDefinitionRegistry;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

	protected BeanDefinitionRegistry registry;

	public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
		super();
		this.registry = registry;
	}
}
