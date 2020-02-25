package com.study.activemq.le2_example.spring.send.expiration;

import javax.annotation.PostConstruct;
import javax.jms.DeliveryMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
public class ExpirationMesssageProducer {

	@Autowired
	private JmsTemplate jmsTemplate;

	@PostConstruct
	public void sendMessage() {

		System.out.println("Sending an email message with Time to Live.");

		// 单独设置某条消息的过期时间
		jmsTemplate.execute("ExpirationTestQueue", (session, producer) -> {
			producer.send(session.createTextMessage("Expiration set Type 1"), DeliveryMode.PERSISTENT, 4, 30000L);
			return null;
		});

		// jmsTemplate级别消息设置过期时间
		jmsTemplate.setExplicitQosEnabled(true);
		jmsTemplate.setTimeToLive(30000L);
		jmsTemplate.convertAndSend("ExpirationTestQueue", "message with Time to Live");

	}

	public static void main(String[] args) {
		SpringApplication.run(ExpirationMesssageProducer.class, args);
	}
}
