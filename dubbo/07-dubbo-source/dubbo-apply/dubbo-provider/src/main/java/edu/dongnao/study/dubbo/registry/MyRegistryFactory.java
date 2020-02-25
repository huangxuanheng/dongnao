package edu.dongnao.study.dubbo.registry;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.RegistryFactory;
import com.alibaba.dubbo.registry.support.AbstractRegistryFactory;


/**
 * DubboRegistryFactory
 * 
 */
public class MyRegistryFactory extends AbstractRegistryFactory implements RegistryFactory {

	@Override
	public Registry getRegistry(URL url) {
		System.out.println("获取myRegistry");
		return super.getRegistry(url);
	}

	@Override
	protected Registry createRegistry(URL url) {
		return new MyRegistry(url);
	}

}

