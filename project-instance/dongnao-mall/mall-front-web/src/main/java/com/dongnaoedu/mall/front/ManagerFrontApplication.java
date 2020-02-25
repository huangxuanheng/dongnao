package com.dongnaoedu.mall.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.dongnaoedu.mall.common.jedis", "com.dongnaoedu.mall.front"})
public class ManagerFrontApplication extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ManagerFrontApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ManagerFrontApplication.class, args);
	}

}
