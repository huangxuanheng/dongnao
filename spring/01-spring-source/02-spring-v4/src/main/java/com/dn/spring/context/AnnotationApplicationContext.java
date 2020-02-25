package com.dn.spring.context;

import java.io.IOException;

import com.dn.spring.beans.BeanDefinitionRegistry;

public class AnnotationApplicationContext extends AbstractApplicationContext {

	private ClassPathBeanDefinitionScanner scanner;

	public AnnotationApplicationContext(String... basePackages) throws Throwable {
		scanner = new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry) this.beanFactory);
		scanner.scan(basePackages);
	}

	@Override
	public Resource getResource(String location) throws IOException {
		return null;
	}

}
