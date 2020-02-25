package com.study.rabbitmq.java.s08_message_attr;

import java.io.IOException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * 简单队列消费者
 */
public class Consumer {

	public static void main(String[] args) throws Exception {
		// 1、创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 2、设置连接属性
		factory.setHost("rabbitmq.study.com");
		factory.setUsername("admin");
		factory.setPassword("admin");

		String queueName = "queue8";

		try (
				// 3、从连接工厂获取连接
				Connection connection = factory.newConnection("消费者");
				// 4、从链接中创建通道
				Channel channel = connection.createChannel();) {

			// 定义收到消息后的回调
			DeliverCallback callback = new DeliverCallback() {
				public void handle(String consumerTag, Delivery message) throws IOException {
					System.out.println("收到消息：" + new String(message.getBody(), "UTF-8"));
					System.out.println("ContentType = " + message.getProperties().getContentType());
					System.out.println("ReplyTo = " + message.getProperties().getReplyTo());
				}
			};

			// 开启队列消费
			channel.basicConsume(queueName, true, callback, new CancelCallback() {
				public void handle(String consumerTag) throws IOException {
				}
			});

			System.out.println("开始接收消息");
			System.in.read();

		}
	}
}
