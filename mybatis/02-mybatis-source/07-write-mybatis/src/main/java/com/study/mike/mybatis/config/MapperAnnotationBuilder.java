package com.study.mike.mybatis.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MapperAnnotationBuilder {

	private Configuration configuration;

	public MapperAnnotationBuilder(Configuration configuration) {
		super();
		this.configuration = configuration;
	}

	public void parse(Class<?> type) {
		// TODO
	}

	private void parseMethod(Method method, String className) {
		// TODO

		Parameter[] params = method.getParameters();

		for (Parameter p : params) {
			System.out.println(p.getName());
			for (Annotation a : p.getAnnotations()) {
				System.out.println(a.toString());
			}

		}
	}
}
