package com.study.activemq.le1_helloworld.spring.topic;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class HelloConsumer {

	@JmsListener(destination = "spring-topic", containerFactory = "myFactory")
	public void receive(String text) {
		System.out.println(Thread.currentThread().getName() + " Received <" + text + ">");
	}

}
