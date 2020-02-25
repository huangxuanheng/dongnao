package com.study.rabbitmq.java.s18_federation;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * 简单队列消费者
 */
public class Consumer {

	public static void main(String[] args) throws Exception {
		// 1、创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.120.90");
		factory.setUsername("admin");
		factory.setPassword("admin");

		String queueName = "test.queue";

		try (
				// 3、从连接工厂获取连接
				Connection connection = factory.newConnection("消费者1");
				// 4、从链接中创建通道
				Channel channel = connection.createChannel();
				Channel channel2 = connection.createChannel();) {

			// 6、定义收到消息后的回调
			DeliverCallback callback = (consumerTag, message) -> {
				System.out.println(consumerTag + " 收到消息：" + new String(message.getBody(), "UTF-8") + " at " + System.currentTimeMillis() / 1000);
			};

			// 7、开启队列消费
			channel.basicConsume(queueName, true, callback, consumerTag -> {
			});

			System.out.println("开始接收消息 at " + System.currentTimeMillis() / 1000);
			// 按任意键退出程序
			System.in.read();

		}
	}
}
