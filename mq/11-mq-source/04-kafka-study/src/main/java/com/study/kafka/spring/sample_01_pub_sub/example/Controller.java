package com.study.kafka.spring.sample_01_pub_sub.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.kafka.spring.sample_01_pub_sub.common.Foo1;

@RestController
public class Controller {

	@Autowired
	private KafkaTemplate<Object, Object> template;

	@RequestMapping(path = "/send/foo/{what}")
	public void sendFoo(@PathVariable String what) {
		this.template.send("topic1", new Foo1(what));
	}

}
