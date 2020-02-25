package com.dn.spring.context;

import com.dn.spring.beans.BeanDefinitionRegistry;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
		super(registry);
	}

	@Override
	public void loadBeanDefintions(Resource resource) {
		this.loadBeanDefintions(new Resource[] { resource });
	}

	@Override
	public void loadBeanDefintions(Resource... resource) {
		if (resource != null && resource.length > 0) {
			for (Resource r : resource) {
				this.parseXml(r);
			}
		}
	}

	private void parseXml(Resource r) {
		// TODO 解析xml文档，获取bean定义 ，创建bean定义对象，注册到BeanDefinitionRegistry中。
	}

}
