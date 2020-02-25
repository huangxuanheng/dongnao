package com.study.rabbitmq.java.s03_pub_sub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Publisher {
	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setPort(5672);
		factory.setUsername("admin");
		factory.setPassword("admin");

		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			// 创建交换器
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

			for (int i = 0; i < 100; i++) {

				String message = "message task" + i;

				channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
				System.out.println("发送消息：" + message);
				Thread.sleep(1000L);
			}

		}
	}
}
