package com.study.mike.spring.mvc.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.study.mike.spring.mvc.mvc.ModelAndView;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

	@Override
	public boolean supports(Object handler) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
