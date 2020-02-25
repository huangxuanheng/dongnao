package com.dn.spring.context;

import com.dn.spring.beans.BeanFactory;
import com.dn.spring.beans.BeanPostProcessor;
import com.dn.spring.beans.PreBuildBeanFactory;

public abstract class AbstractApplicationContext implements ApplicationContext {

	protected BeanFactory beanFactory;

	public AbstractApplicationContext() {
		super();
		this.beanFactory = new PreBuildBeanFactory();
	}

	@Override
	public Object getBean(String name) throws Throwable {
		return beanFactory.getBean(name);
	}

	@Override
	public void registerBeanPostProcessor(BeanPostProcessor bpp) {
		this.beanFactory.registerBeanPostProcessor(bpp);
	}

}
