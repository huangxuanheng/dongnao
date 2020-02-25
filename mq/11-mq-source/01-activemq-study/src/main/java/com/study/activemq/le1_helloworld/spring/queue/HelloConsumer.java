package com.study.activemq.le1_helloworld.spring.queue;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.study.activemq.le1_helloworld.spring.Email;

@Component
public class HelloConsumer {

	@JmsListener(destination = "mailbox")
	public void receive(Email email) {
		System.out.println(Thread.currentThread().getName() + " Received <" + email + ">");
	}

}
