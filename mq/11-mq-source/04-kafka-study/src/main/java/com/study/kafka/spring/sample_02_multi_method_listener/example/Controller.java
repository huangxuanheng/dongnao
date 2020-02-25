package com.study.kafka.spring.sample_02_multi_method_listener.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.kafka.spring.sample_01_pub_sub.common.Foo1;
import com.study.kafka.spring.sample_02_multi_method_listener.common.Bar1;

@RestController
public class Controller {

	@Autowired
	private KafkaTemplate<Object, Object> template;

	@RequestMapping(path = "/send/foo/{what}")
	public void sendFoo(@PathVariable String what) {
		this.template.send("foos", new Foo1(what));
	}

	@RequestMapping(path = "/send/bar/{what}")
	public void sendBar(@PathVariable String what) {
		this.template.send("bars", new Bar1(what));
	}

	@RequestMapping(path = "/send/unknown/{what}")
	public void sendUnknown(@PathVariable String what) {
		this.template.send("bars", what);
	}

}
