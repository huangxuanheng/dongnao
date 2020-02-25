package com.study.activemq.le2_example.spring.receive.durable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;

@SpringBootApplication
public class DurableConsumer {

	@JmsListener(containerFactory = "myFactory", destination = "durableTopic", subscription = "I love Durable")
	public void receive(String text) {
		System.out.println(Thread.currentThread().getName() + " Received <" + text + ">");
	}

	public static void main(String[] args) {
		SpringApplication.run(DurableConsumer.class, args);
	}
}
