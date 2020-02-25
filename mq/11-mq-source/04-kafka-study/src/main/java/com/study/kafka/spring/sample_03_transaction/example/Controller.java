package com.study.kafka.spring.sample_03_transaction.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.kafka.spring.sample_01_pub_sub.common.Foo1;

@RestController
public class Controller {

	@Autowired
	private KafkaTemplate<Object, Object> template;

	@RequestMapping(path = "/send/foos/{what}")
	public void sendFoo(@PathVariable String what) {
		this.template.executeInTransaction(kafkaTemplate -> {
			StringUtils.commaDelimitedListToSet(what).stream().map(s -> new Foo1(s)).forEach(foo -> kafkaTemplate.send("topic2", foo));
			return null;
		});
	}

}
