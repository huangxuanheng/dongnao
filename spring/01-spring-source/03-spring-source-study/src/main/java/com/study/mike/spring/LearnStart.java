package com.study.mike.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.study.mike.spring.service.Abean;

public class LearnStart {

	public static void main(String[] args) {
		// 注解方式，指定扫描的包
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				"com.study.mike.spring.sample", "com.study.mike.spring.service", "com.study.mike.spring.ext");
		Abean ab = context.getBean(Abean.class);
		ab.doSomething();
		context.close();

	}
}

/*
 * GenericXmlApplicationContext context1 = new
 * GenericXmlApplicationContext("file:e:/study/application.xml");
 * context1.close();
 */