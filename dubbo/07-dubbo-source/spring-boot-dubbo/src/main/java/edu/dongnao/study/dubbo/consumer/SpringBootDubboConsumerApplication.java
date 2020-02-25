package edu.dongnao.study.dubbo.consumer;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * spring-boot结合dubbo示例
 * SpringBootDubboConsumerApplication
 */
@SpringBootApplication
@EnableDubbo(scanBasePackages = "edu.dongnao.study.dubbo.consumer")
public class SpringBootDubboConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDubboConsumerApplication.class, args);
	}

}
