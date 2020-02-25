package com.study.mike.spring.mvc.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BeanNameUrlHandlerMapping implements HandlerMapping, ApplicationContextAware {
	private ApplicationContext applicationContext;

	public ApplicationContext obtainApplicationContext() {
		return this.applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public Object getHandler(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
