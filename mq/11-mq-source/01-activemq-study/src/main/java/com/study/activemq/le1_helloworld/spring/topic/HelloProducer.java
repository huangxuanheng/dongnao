package com.study.activemq.le1_helloworld.spring.topic;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
public class HelloProducer {

	@Autowired
	private JmsTemplate jmsTemplate;

	@PostConstruct
	public void sendMessage() {
		// Send a message with a POJO - the template reuse the message converter
		System.out.println("Sending an email message.");
		jmsTemplate.setPubSubDomain(true);
		jmsTemplate.convertAndSend("spring-topic", "Hello Spring topic");
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloProducer.class, args);
	}
}
