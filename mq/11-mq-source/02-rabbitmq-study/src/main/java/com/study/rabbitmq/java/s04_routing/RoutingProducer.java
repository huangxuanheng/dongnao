package com.study.rabbitmq.java.s04_routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RoutingProducer {
	private static final String EXCHANGE_NAME = "routing-sample";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setPort(5672);
		factory.setUsername("admin");
		factory.setPassword("admin");

		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			// 创建direct交换器
			channel.exchangeDeclare(EXCHANGE_NAME, "direct");

			String routingKey = "black";

			for (int i = 0; i < 100; i++) {

				switch (i % 3) {
				case 0:
					routingKey = "black";
					break;
				case 1:
					routingKey = "orange";
					break;
				default:
					routingKey = "green";
				}
				String message = "message task-" + i + " routingKey=" + routingKey;
				channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
				System.out.println("发送消息：" + message);
				Thread.sleep(1000L);
			}

		}
	}
}
