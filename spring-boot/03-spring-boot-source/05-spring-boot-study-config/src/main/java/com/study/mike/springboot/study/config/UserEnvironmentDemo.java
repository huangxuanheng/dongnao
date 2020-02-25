package com.study.mike.springboot.study.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class UserEnvironmentDemo {

	@Autowired
	private Environment env;

	public void service() {
		System.out.println(env.getProperty("my.name"));
	}
}
