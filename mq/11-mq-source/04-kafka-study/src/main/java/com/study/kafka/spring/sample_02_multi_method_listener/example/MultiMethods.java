package com.study.kafka.spring.sample_02_multi_method_listener.example;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.study.kafka.spring.sample_01_pub_sub.common.Foo2;
import com.study.kafka.spring.sample_02_multi_method_listener.common.Bar2;

@Component
@KafkaListener(id = "multiGroup", topics = { "foos", "bars" })
public class MultiMethods {

	@KafkaHandler
	public void foo(Foo2 foo) {
		System.out.println("Received: " + foo);
	}

	@KafkaHandler
	public void bar(Bar2 bar) {
		System.out.println("Received: " + bar);
	}

	@KafkaHandler(isDefault = true)
	public void unknown(Object object) {
		System.out.println("Received unknown: " + object);
	}

}
