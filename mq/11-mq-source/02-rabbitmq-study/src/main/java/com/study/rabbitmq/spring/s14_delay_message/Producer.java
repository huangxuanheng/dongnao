package com.study.rabbitmq.spring.s14_delay_message;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling // spring 中的定时功能，此处只是为了多次发送消息
public class Producer {

	@Bean
	public FanoutExchange fanout() {
		FanoutExchange ex = new FanoutExchange("my-exchange");
		ex.setDelayed(true);
		return ex;
	}

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private FanoutExchange fanout;

	@Scheduled(fixedDelay = 1000) // 定时多次发送消息
	public void send() {
		String message = "Hello World!-" + System.currentTimeMillis() / 1000;

		template.convertAndSend(fanout.getName(), "", message, new MessagePostProcessor() {

			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				message.getMessageProperties().setDelay(5000);
				return message;
			}

		});

		System.out.println(" [x] Sent '" + message + "'");
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Producer.class, args);
		System.in.read();
	}
}
