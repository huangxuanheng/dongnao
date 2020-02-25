package com.study.activemq.le2_example.activemq.destination.composite;

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
		// 在消费时，并不可以复合对列与主题
		new ConsumerThread("tcp://mq.study.com:61616", "queue://queue2,topic2").start();
		// 在消费时，可以复合消费多个队列的消息
		//new ConsumerThread("tcp://mq.study.com:61616", "queue2,queue3").start();
		// 可以订阅多个主题
		//new ConsumerThread("tcp://mq.study.com:61616", "topic1,topic2").start();
		// 注意：如果同一条消息发给了多个队列或主题，消费者只会收到一次。
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
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// 4、创建消息消费目标(Topic or Queue)
			//Destination destination = session.createQueue(destinationUrl);
			Destination destination = session.createTopic(destinationUrl);

			// 5、创建消息消费者 http://activemq.apache.org/destination-options.html
			MessageConsumer consumer = session.createConsumer(destination);

			// 6、异步接收消息
			consumer.setMessageListener(new MessageListener() {

				@Override
				public void onMessage(Message message) {
					if (message instanceof TextMessage) {
						try {
							System.out.println(Thread.currentThread().getName() + " Time: " + System.currentTimeMillis()
									+ " 收到文本消息：" + ((TextMessage) message).getText() + " id: " + message.getJMSMessageID());
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
