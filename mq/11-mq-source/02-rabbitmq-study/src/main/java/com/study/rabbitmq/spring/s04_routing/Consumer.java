package com.study.rabbitmq.spring.s04_routing;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component
public class Consumer {

	@RabbitListener(queues = "#{autoDeleteQueue1.name}")
	public void receive1(Channel channel, String in) {
		System.out.println("Channel-" + channel.getChannelNumber() + " Received '" + in + "'");
	}

	@RabbitListener(queues = "#{autoDeleteQueue2.name}")
	public void receive2(Channel channel, String in) {
		System.out.println("Channel-" + channel.getChannelNumber() + " Received '" + in + "'");
	}
}
