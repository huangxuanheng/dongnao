package com.dongnaoedu.mall.manager;

import javax.jms.Destination;
import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJms
@EnableTransactionManagement
@ComponentScan(value = {"com.dongnaoedu.mall"})
@MapperScan(basePackages = "com.dongnaoedu.mall.manager.mapper")
public class ManagerApplication {
	
//	@Bean
//	public Queue queue() {
//		return new ActiveMQQueue("spring-queue");
//	}
	
	@Bean
	public Destination topicDestination() {
		return new ActiveMQTopic("itemAddTopic");
	}

	public static void main(String[] args) {
		SpringApplication.run(ManagerApplication.class, args);
	}

}
