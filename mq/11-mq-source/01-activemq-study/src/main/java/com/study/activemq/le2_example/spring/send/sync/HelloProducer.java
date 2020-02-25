package com.study.activemq.le2_example.spring.send.sync;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.study.activemq.le1_helloworld.spring.Email;

@Component
public class HelloProducer {

	@Autowired
	private JmsTemplate jmsTemplate;

	@PostConstruct
	public void sendMessage() {
		// Send a message with a POJO - the template reuse the message converter
		System.out.println("Sending an email message.");
		jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello"));
	}

}
