package com.study.rabbitmq.java.s17_cluster;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {

	public static void main(String[] args) throws Exception {
		// 1、创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("admin");
		factory.setPassword("admin");
		// 开启自动恢复
		factory.setAutomaticRecoveryEnabled(true);
		// 设置网络恢复间隔时间
		// factory.setNetworkRecoveryInterval(3000); // 默认是5秒
		// 集群多个可连接点
		Address[] addresses = { new Address("192.168.100.15"), new Address("192.168.100.16"), new Address("192.168.100.18") };

		try (
				// 3、从连接工厂获取连接
				Connection connection = factory.newConnection(addresses, "生产者");
				// 4、从链接中创建通道
				Channel channel = connection.createChannel();) {

			for (int i = 0; i < 100; i++) {
				// 消息内容
				String message = "message task" + i;
				// 6、发送消息
				channel.basicPublish("my-exchange", "", null, message.getBytes());
				System.out.println("发送消息：" + message);
				Thread.sleep(1000L);
			}

		}
	}
}
