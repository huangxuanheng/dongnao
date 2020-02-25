package com.dongnaoedu.mall.manager.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
public class LanguageController {
	
	// 输入http://localhost:8888/hello 测试
	@RequestMapping("/changeSessionLanauage")
	public String changeSessionLanauage(HttpServletRequest request, HttpServletResponse response, String lang) {
		System.out.println(lang);
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		if ("zh".equals(lang)) {
			localeResolver.setLocale(request, response, new Locale("zh", "CN"));
		} else if ("en".equals(lang)) {
			localeResolver.setLocale(request, response, new Locale("en", "US"));
		}
		System.out.println("1111");
		return "hello";
	}
	
	@RequestMapping("/changeCookieLanauage")
	public void changeCookieLanauage(HttpServletRequest request, HttpServletResponse response, String lang) {
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		if ("zh".equals(lang)) {
			localeResolver.setLocale(request, response, new Locale("zh", "CN"));
		} else if ("en".equals(lang)) {
			localeResolver.setLocale(request, response, new Locale("en", "US"));
		}
	}
	
}
