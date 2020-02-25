package com.study.mike.mybatis.session;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.study.mike.mybatis.config.Configuration;

public class DefaultSqlSession implements SqlSession {

	private Configuration configuration;

	public DefaultSqlSession(Configuration configuration) {
		super();
		this.configuration = configuration;
	}

	@Override
	public <T> T getMapper(Class<T> type) {
		if (!this.configuration.getMappers().contains(type)) {
			throw new RuntimeException(type + " 不在Mapper接口列表中！");
		}

		InvocationHandler ih = this.getMapperProxy();

		T t = (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, ih);
		return t;
	}

	private InvocationHandler getMapperProxy() {
		// TODO
		return null;
	}
}
