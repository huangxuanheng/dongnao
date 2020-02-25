package com.study.mike.sample.mybatis.jdbc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class GetParamName {

	public static void main(String[] args) throws Throwable {
		Class<?> clazz = UserDao.class;
		Method m = clazz.getMethod("do1", String.class, String.class);
		for (Parameter p : m.getParameters()) {
			System.out.println(p.getName());
		}
	}

}
