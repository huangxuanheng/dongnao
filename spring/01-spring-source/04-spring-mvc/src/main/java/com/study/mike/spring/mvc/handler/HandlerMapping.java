package com.study.mike.spring.mvc.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.Ordered;

public interface HandlerMapping extends Ordered {

	Object getHandler(HttpServletRequest request) throws Exception;

	@Override
	default int getOrder() {
		return 0;
	}
}
