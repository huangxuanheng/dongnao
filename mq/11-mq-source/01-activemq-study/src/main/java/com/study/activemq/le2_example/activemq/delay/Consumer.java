package com.study.activemq.le2_example.activemq.delay;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 简单消费者
 */
// http://activemq.apache.org/consumer-features.html
public class Consumer {
	public static void main(String[] args) {
		new ConsumerThread("tcp://mq.study.com:61616", "delayQueue").start();
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
		MessageConsumer consumer;

		try {
			// brokerURL
			// http://activemq.apache.org/connection-configuration-uri.html
			// 1、创建连接工厂
			connectionFactory = new ActiveMQConnectionFactory(this.brokerUrl);

			// 2、创建连接对象
			conn = connectionFactory.createConnection();
			conn.start(); // 一定要启动

			// 3、创建会话（可以创建一个或者多个session）
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// 4、创建消息消费目标(Topic or Queue)
			Destination destination = session.createQueue(destinationUrl);

			// 5、创建消息消费者 http://activemq.apache.org/destination-options.html
			consumer = session.createConsumer(destination);

			// 6、异步接收消息
			consumer.setMessageListener(new MessageListener() {

				@Override
				public void onMessage(Message message) {
					if (message instanceof TextMessage) {
						try {
							System.out.println("Time: " + System.currentTimeMillis() + " 收到文本消息："
									+ ((TextMessage) message).getText());
						} catch (JMSException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println(message);
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
