package com.dongnaoedu.mall.wagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@Configuration
//@EnableSwagger2
//@EnableWebMvc
//@ComponentScan("com.XXX.controller")
public class SwaggerConfig1 {
	
	private String test = "从制定地方获取到的环境变量";

	@Bean
	public Docket customDocket() {
		if (test == "启动时候的环境变量") {
			return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfoOnline()).select().paths(PathSelectors.none())// 如果是线上环境，添加路径过滤，设置为全部都不符合
					.build();
		} else {
			return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo());
		}
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("XXX系统").description("XXX系统接口").license("").licenseUrl("")
				.termsOfServiceUrl("").version("1.0.0").contact(new Contact("", "", "")).build();
	}

	private ApiInfo apiInfoOnline() {
		return new ApiInfoBuilder().title("").description("").license("").licenseUrl("").termsOfServiceUrl("")
				.version("").contact(new Contact("", "", "")).build();
	}
	
}