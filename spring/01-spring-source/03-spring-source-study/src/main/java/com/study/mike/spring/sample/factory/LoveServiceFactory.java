package com.study.mike.spring.sample.factory;

import com.study.mike.spring.service.CombatService;
import com.study.mike.spring.service.LoveService;
import com.study.mike.spring.service.LoveServiceImpl;

public class LoveServiceFactory {

	public static LoveService getLoveServiceFromStaticFactoryMethod() {
		return new LoveServiceImpl();
	}

	public CombatService getCombatServiceFromMemberFactoryMethod(int time) {
		return new CombatService(time);
	}
}
