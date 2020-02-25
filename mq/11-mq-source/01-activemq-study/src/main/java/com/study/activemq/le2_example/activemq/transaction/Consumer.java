package com.study.activemq.le2_example.activemq.transaction;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer {
	public static void main(String[] args) {
		new ConsumerThread("tcp://mq.study.com:61616", "transaction-test").start();

	}
}

class ConsumerThread extends Thread {

	String brokerUrl;

	String destinationUrl;

	public ConsumerThread(String brokerUrl, String destinationUrl) {
		this.brokerUrl = brokerUrl;
		this.destinationUrl = destinationUrl;
	}

	@Override
	public void run() {
		ActiveMQConnectionFactory connectionFactory;
		Connection conn;
		Session session;

		try {
			// 1、创建连接工厂
			connectionFactory = new ActiveMQConnectionFactory(this.brokerUrl);

			// 2、创建连接对象
			conn = connectionFactory.createConnection();

			conn.start(); // 一定要启动

			// 3、创建会话（可以创建一个或者多个session）
			// 自动回复消息确认 // 开启事务
			session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);

			// 4、创建消息消费目标(Topic or Queue)
			Destination destination = session.createQueue(destinationUrl);

			// 5、创建消息消费者 http://activemq.apache.org/destination-options.html
			MessageConsumer consumer = session.createConsumer(destination);

			// 6 同步接收消息
			for (int i = 0; i < 5; i++) {
				Message message = consumer.receive();
				if (message instanceof TextMessage) {
					System.out.println(Thread.currentThread().getName() + " Time: " + System.currentTimeMillis()
							+ " 收到文本消息：" + ((TextMessage) message).getText());
				} else {
					System.out.println(message);
				}
			}

			// 提交事务
			session.commit();

			consumer.close();
			session.close();
			conn.close();

			// // 6、异步接收消息 异步时不方便使用事务方式 （消费消息的方式不是一次要消费多条了，通过消息确认方式来控制）
			// consumer.setMessageListener(new MessageListener() {
			//
			// @Override
			// public void onMessage(Message message) {
			// try {
			// if (message instanceof TextMessage) {
			// System.out.println(Thread.currentThread().getName() + " Time: " +
			// System.currentTimeMillis()
			// + " 收到文本消息：" + ((TextMessage) message).getText());
			// } else {
			// System.out.println(message);
			// }
			//
			// // 手动方式回复消息确认
			// // message.acknowledge();
			//
			// } catch (JMSException e) {
			// e.printStackTrace();
			// }
			// }
			// });
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
