package com.study.activemq.le2_example.activemq.acknowledge;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer {
	public static void main(String[] args) {
		new ConsumerThread("tcp://mq.study.com:61616", "acknowledge-test").start();

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
			// 自动回复消息确认
			// session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// 懒惰回复消息确认（在可以容忍重复消息的情况下使用，可提高效率）
			// session = conn.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
			// 用户手动回复消息确认
			session = conn.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			// 开启事务，由事务控制来回复消息确认
			// session = conn.createSession(false, Session.SESSION_TRANSACTED);

			// 4、创建消息消费目标(Topic or Queue)
			Destination destination = session.createQueue(destinationUrl);

			// 5、创建消息消费者 http://activemq.apache.org/destination-options.html
			MessageConsumer consumer = session.createConsumer(destination);

			// 6、异步接收消息
			consumer.setMessageListener(new MessageListener() {

				@Override
				public void onMessage(Message message) {
					try {
						if (message instanceof TextMessage) {
							System.out.println(Thread.currentThread().getName() + " Time: " + System.currentTimeMillis()
									+ " 收到文本消息：" + ((TextMessage) message).getText());
						} else {
							System.out.println(message);
						}

						// 手动方式回复消息确认
						// message.acknowledge();

					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});

			// consumer.close();
			// session.close();
			// conn.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
