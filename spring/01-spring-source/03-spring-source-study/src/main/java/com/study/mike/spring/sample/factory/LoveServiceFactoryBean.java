package com.study.mike.spring.sample.factory;

import org.springframework.beans.factory.FactoryBean;

import com.study.mike.spring.service.LoveService;
import com.study.mike.spring.service.LoveServiceImpl;

public class LoveServiceFactoryBean implements FactoryBean<LoveService> {

	@Override
	public LoveService getObject() throws Exception {
		return new LoveServiceImpl();
	}

	@Override
	public Class<?> getObjectType() {
		return LoveService.class;
	}

}
