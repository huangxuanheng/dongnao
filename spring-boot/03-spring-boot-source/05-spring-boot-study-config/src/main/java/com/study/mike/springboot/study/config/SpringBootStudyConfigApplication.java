package com.study.mike.springboot.study.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class SpringBootStudyConfigApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootStudyConfigApplication.class, args);

		// SpringApplication sa = new
		// SpringApplication(SpringBootStudyConfigApplication.class);
		// sa.setAdditionalProfiles("dev", "devdb");
		// sa.run(args);
	}

	@Autowired
	MyBean mybean;

	@Autowired
	Environment environment;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("***************** " + mybean);
		// System.out.println("*****************" +
		// environment.getProperty("my.text"));
	}
}
