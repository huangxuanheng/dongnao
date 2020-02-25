package com.dn.spring.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.dn.spring.beans.BeanDefinitionRegistry;

public class XmlApplicationContext extends AbstractApplicationContext {

	private List<Resource> resources;

	private BeanDefinitionReader reader;

	public XmlApplicationContext(String... location) throws Throwable {
		super();
		load(location);
		this.reader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) this.beanFactory);
		reader.loadBeanDefintions((Resource[]) resources.toArray());
	}

	@Override
	public Resource getResource(String location) throws IOException {
		if (StringUtils.isNotBlank(location)) {
			if (location.startsWith(Resource.CLASS_PATH_PREFIX)) {
				return new ClassPathResource(location.substring(Resource.CLASS_PATH_PREFIX.length()));
			} else if (location.startsWith(Resource.File_SYSTEM_PREFIX)) {
				return new FileSystemResource(location.substring(Resource.File_SYSTEM_PREFIX.length()));
			} else {
				return new UrlResource(location);
			}
		}
		return null;
	}

	private void load(String... location) throws IOException {
		if (resources == null) {
			resources = new ArrayList<>();
		}
		// 完成加载，创建好 Resource
		if (location != null && location.length > 0) {
			for (String lo : location) {
				Resource re = this.getResource(lo);
				if (re != null) {
					this.resources.add(re);
				}
			}
		}
	}
}
