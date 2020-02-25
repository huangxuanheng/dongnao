package com.study.rabbitmq.spring.s08_expiration_message;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;

//@Component
public class Consumer {

	@RabbitListener(queues = "spring-queue8")
	public void receive(String in, @Headers Map<String, Object> headers,
			@Header(AmqpHeaders.EXPIRATION) String expiration, @Header(AmqpHeaders.REPLY_TO) String replyTo,
			@Header(AmqpHeaders.CONTENT_TYPE) String contentType) {
		System.out.println(" [x] Received '" + in + "'");
		System.out.println(headers);
		System.out.println(expiration);
		System.out.println(replyTo);
		System.out.println(contentType);
	}
}
