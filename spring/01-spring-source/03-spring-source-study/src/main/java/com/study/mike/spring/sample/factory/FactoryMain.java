package com.study.mike.spring.sample.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.study.mike.spring.service.CombatService;
import com.study.mike.spring.service.LoveService;

public class FactoryMain {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"com/study/mike/spring/sample/factory/application.xml");

		CombatService cs = context.getBean(CombatService.class);
		cs.doInit();
		cs.combating();

		System.out.println("--------------------------------------");

		LoveService ls = (LoveService) context.getBean("loveService");
		ls.doLove();

		System.out.println("--------------------------------------");

		ls = (LoveService) context.getBean("loveService2");
		ls.doLove();

		System.out.println("--------------------------------------");
		// 获取工厂bean自己
		LoveServiceFactoryBean lsfb = (LoveServiceFactoryBean) context.getBean("&loveService2");
		System.out.println(lsfb);

	}

}
