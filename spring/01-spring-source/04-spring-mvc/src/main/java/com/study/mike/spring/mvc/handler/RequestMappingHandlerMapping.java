package com.study.mike.spring.mvc.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.study.mike.spring.mvc.annotation.RequestMappingInfo;

public class RequestMappingHandlerMapping implements HandlerMapping, InitializingBean, ApplicationContextAware {

	private ApplicationContext applicationContext;

	private Set<RequestMappingInfo> requestMappingInfos = new HashSet<>();

	private Map<String, List<RequestMappingInfo>> urlMaps = new HashMap<>();

	public ApplicationContext obtainApplicationContext() {
		return this.applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 检测@Controller Bean
		for (String beanName : this.applicationContext.getBeanNamesForType(Object.class)) {
			Class<?> beanType = this.applicationContext.getType(beanName);
			if (isHandlerBean(beanType)) {
				detectHandlerMethod(beanType);
			}
		}
	}

	private void detectHandlerMethod(Class<?> beanType) {
		// TODO Auto-generated method stub

	}

	private boolean isHandlerBean(Class<?> beanType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getHandler(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
