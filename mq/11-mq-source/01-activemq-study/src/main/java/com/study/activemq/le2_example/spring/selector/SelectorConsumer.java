package com.study.activemq.le2_example.spring.selector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;

@SpringBootApplication
public class SelectorConsumer {

	@JmsListener(destination = "queue1", selector = "subtype='bbb'")
	public void receive(String text, @Header("subtype") String subtype) {
		System.out.println(Thread.currentThread().getName() + " Received <" + text + "> subtype=" + subtype);
	}

	public static void main(String[] args) {
		SpringApplication.run(SelectorConsumer.class, args);
	}
}
