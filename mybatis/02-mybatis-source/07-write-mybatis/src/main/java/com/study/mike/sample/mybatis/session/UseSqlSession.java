package com.study.mike.sample.mybatis.session;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

public class UseSqlSession {

	public static void main(String[] args) {
		TestInterface ti = (TestInterface) Proxy.newProxyInstance(TestInterface.class.getClassLoader(),
				new Class<?>[] { TestInterface.class }, new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if(method.getReturnType() == List.class) {
						Type genericType = method.getGenericReturnType();
						if(genericType == null) {
							// 当集合中放Map
						}
						else if (genericType instanceof ParameterizedType) {
							ParameterizedType t = (ParameterizedType) genericType;
							Class<?> elementType = (Class<?>)t.getActualTypeArguments()[0];
						}
						System.out.println(args[0]);
						return null;
					}
				});
		// ti.do1("aaaa");
		System.out.println(ti.do3("aaaaaaaaaaaaa"));
	}
}
