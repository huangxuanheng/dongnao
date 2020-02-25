package edu.dongnao.study.dubbo.registry;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.registry.NotifyListener;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.support.FailbackRegistry;


/**
 * MyRegistryFactory
 * 
 */
public class MyRegistry extends FailbackRegistry implements Registry {

	public MyRegistry(URL url) {
		super(url);
	}

	@Override
	public void doRegister(URL url) {
		System.out.println("注册了："+url);
	}

	@Override
	public void doUnregister(URL url) {
		System.out.println("取消注册："+url);
	}

	@Override
	public void doSubscribe(URL url, NotifyListener listener) {
		System.out.println("订阅："+url);
	}

	@Override
	public void doUnsubscribe(URL url, NotifyListener listener) {
		System.out.println("取消订阅："+url);
	}


	@Override
	public boolean isAvailable() {
		System.out.println("算法可用");
		return true;
	}

}

