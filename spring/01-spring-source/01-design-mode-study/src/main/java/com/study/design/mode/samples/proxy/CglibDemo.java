package com.study.design.mode.samples.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibDemo {

	// 功能增强的实现
	static class MyMethodInterceptor implements MethodInterceptor {
		private Object target;

		public MyMethodInterceptor(Object target) {
			this.target = target;
		}

		public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

			System.out.println("**************** " + method.getName());
			// 前置增强
			doSomethingBefore();
			// 返回值
			Object res = null;
			// 调用父类的该方法，当是生成接口的代理时不可调用。
			// Object res = methodProxy.invokeSuper(proxy, args);
			// 通过method来调用被代理对象的方法
			if (this.target != null) {
				res = method.invoke(target, args);
			}
			// 后置增强
			doSomethingAfter();
			return res;
		}

		private void doSomethingBefore() {
			System.out.println("老板你好，这个我试过了，很不错，推荐给你！");
		}

		private void doSomethingAfter() {
			System.out.println("老板你觉得怎样？ 欢迎下次.....");
		}
	};

	public static void main(String[] args) {
		Enhancer e = new Enhancer();
		TeacherCang tc = new TeacherCang();
		// 设置增强回调
		e.setCallback(new MyMethodInterceptor(tc));
		// 获得接口代理对象

		e.setInterfaces(new Class[] { Girl.class });
		Girl g = (Girl) e.create();
		g.dating(1.8f);

		System.out.println("------------------------------------------");
		// 对类生成代理对象
		e.setSuperclass(TeacherCang.class);
		e.setInterfaces(null);
		//当有多个callback时，需要通过callbackFilter来指定被代理方法使用第几个callback
		/* e.setCallbackFilter(new CallbackFilter() {
			@Override
			public int accept(Method method) {
				return 0;
			}
		});*/

		TeacherCang proxy = (TeacherCang) e.create();
		proxy.dating(1.8f);
	}
}
