package com.study.rabbitmq.java.s18_federation;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {

	public static void main(String[] args) throws Exception {
		// 1、创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setUsername("admin");
		factory.setPassword("admin");
		try (
				// 3、从连接工厂获取连接
				Connection connection = factory.newConnection("生产者");
				// 4、从链接中创建通道
				Channel channel = connection.createChannel();) {

			for (int i = 0; i < 100; i++) {
				// 消息内容
				String message = "message task" + i;
				// 6、发送消息
				channel.basicPublish("amq.fanout", "", null, message.getBytes());
				System.out.println("发送消息：" + message);
				Thread.sleep(1000L);
			}

		}
	}
}
