package com.study.rabbitmq.java.s09_publisher_confirm;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

// mandatory 示例，默认是false,消息不可以路由则会丢弃（或配置备用策略）；true 则返回给发布者
public class RoutingAlteExchange {

	public static void main(String[] argss) throws Exception {

		// 1、创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 2、设置连接属性
		factory.setHost("localhost");
		factory.setPort(5672);
		factory.setUsername("admin");
		factory.setPassword("admin");

		try (
				// 3、从连接工厂获取连接 //可以给连接命个名
				Connection connection = factory.newConnection("生产者");
				// 4、从链接中创建通道
				Channel channel = connection.createChannel();) {

			Map<String, Object> args = new HashMap<String, Object>();
			args.put("alternate-exchange", "my-ae"); // 备用交换参数指定
			channel.exchangeDeclare("my-direct", "direct", false, false, args);
			channel.exchangeDeclare("my-ae", "fanout");
			channel.queueDeclare("routedss", true, false, false, null);
			channel.queueBind("routedss", "my-direct", "key1");
			channel.queueDeclare("unroutedss", true, false, false, null);
			channel.queueBind("unroutedss", "my-ae", "");
			// 消息内容
			String message = "一条消息";
			// 6、发送消息
			channel.basicPublish("my-direct", "key1", false, null, message.getBytes());
			System.out.println("发送消息：" + message);

			channel.basicPublish("my-direct", "key2", false, null, message.getBytes());

			// 按任意键退出程序
			System.in.read();
		}

	}

}
