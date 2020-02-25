package com.study.activemq.le2_example.spring.selector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class Producer {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Transactional // 在开启事务的情况下需要加事务注解
	public void sendMessage(String subtype) {

		// 发送延时消息
		jmsTemplate.convertAndSend("queue1", "message with property", message -> {
			// 设置消息属性
			message.setStringProperty("subtype", subtype);
			return message;
		});

		System.out.println("Sending an message with subtype=" + subtype);
	}

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Producer.class, args);
		Producer producer = context.getBean(Producer.class);
		producer.sendMessage("aaa");
		producer.sendMessage("bbb");
	}
}
