package com.study.activemq.le1_helloworld.spring.queue;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;

import com.study.activemq.le1_helloworld.spring.Email;

@SpringBootApplication
public class HelloProducer {

	@Autowired
	private JmsTemplate jmsTemplate;

	// 或者注入 JmsMessagingTemplate, 比JmsTemplate就是多了一下能直接以String表示Destination的方法
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@PostConstruct
	public void sendMessage() {
		// Send a message with a POJO - the template reuse the message converter
		System.out.println("Sending an email message.");
		jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello"));

		// 或者
		jmsMessagingTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello"));
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloProducer.class, args);
	}
}
