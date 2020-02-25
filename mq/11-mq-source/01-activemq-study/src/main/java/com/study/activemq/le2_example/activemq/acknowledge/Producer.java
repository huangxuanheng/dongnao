package com.study.activemq.le2_example.activemq.acknowledge;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 简单生产者
 */
public class Producer {
	public static void main(String[] args) {
		new ProducerThread("tcp://mq.study.com:61616", "acknowledge-test").start();
	}

	static class ProducerThread extends Thread {
		String brokerUrl;
		String destinationUrl;

		public ProducerThread(String brokerUrl, String destinationUrl) {
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
				connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

				// 2、创建连接
				conn = connectionFactory.createConnection();
				conn.start(); // 一定要start

				// 3、创建会话（可以创建一个或者多个session）
				session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

				// 4、创建消息发送目标 (Topic or Queue)
				Destination destination = session.createQueue(destinationUrl);

				// 5、用目的地创建消息生产者
				MessageProducer producer = session.createProducer(destination);
				// 设置递送模式(持久化 / 不持久化)
				producer.setDeliveryMode(DeliveryMode.PERSISTENT);

				// 6、创建文本消息
				for (int i = 0; i < 2; i++) {
					String text = "VirtualTopic.order message! From: " + Thread.currentThread().getName() + " : "
							+ System.currentTimeMillis();
					TextMessage message = session.createTextMessage(text);

					// 7、通过producer 发送消息
					System.out.println("Sent message: " + text);
					producer.send(message);
					Thread.sleep(1000L);
				}

				// 8、 清理、关闭连接
				session.close();
				conn.close();
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
