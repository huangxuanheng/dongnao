package com.study.mike.springboot.study.source;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ServletComponentScan
public class SpringBootStudySourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootStudySourceApplication.class, args);
		// context.getBean(requiredType)
		// SpringApplication sa = new
		// SpringApplication(SpringBootStudySourceApplication.class);
		// sa.setAdditionalProfiles("dev,devdb");
		//
		// sa.run(args);
	}

	@Bean
	public FilterRegistrationBean<MyFilter> getFilterRegistrationBean() {

		FilterRegistrationBean<MyFilter> frb = new FilterRegistrationBean<>();
		frb.setFilter(new MyFilter());
		frb.addUrlPatterns("/*");
		return frb;
	}

}
