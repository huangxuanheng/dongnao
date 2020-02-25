package com.study.design.mode.samples.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TonyCompany {

	public static Object proxy(Object target) {
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
				new MyInvationHandler(target));
	}

	private static class MyInvationHandler implements InvocationHandler {

		private Object target;

		public MyInvationHandler(Object target) {
			super();
			this.target = target;
		}

		public Object getTarget() {
			return target;
		}

		public void setTarget(Object target) {
			this.target = target;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// 前置增强
			doSomethingBefore();

			// 调用被代理对象的方法
			Object res = method.invoke(target, args);

			// 后置增强
			doSomethingAfter();

			return res;
		}

		private void doSomethingAfter() {
			System.out.println("老板，你觉得怎样，欢迎下次再约！");
		}

		private void doSomethingBefore() {
			System.out.println("老板，这个我试过了，很不错，推荐给你！");
		}

	}
}
