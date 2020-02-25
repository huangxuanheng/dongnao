package com.study.activemq.le4_cluster.master_slave;

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
// http://activemq.apache.org/failover-transport-reference.html
public class FailoverConsumer {
	public static void main(String[] args) throws Exception {
		// 非failover的公共参数配置通过nested.*，例如
		// failover:(...)?nested.wireFormat.maxInactivityDuration=1000
		// ?randomize=false 随机选择，默认是顺序
		// 指定优先切换
		// failover:(tcp://host1:61616,tcp://host2:61616,tcp://host3:61616)?priorityBackup=true&priorityURIs=tcp://local1:61616,tcp://local2:61616
		// maxReconnectDelay重连的最大间隔时间(毫秒)
		new ConsumerThread("failover:(tcp://mq.study.com:61616,tcp://mq.study.com:61617)", "queue3").start();
		System.in.read();
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
							System.out.println(
									Thread.currentThread().getName() + " 收到文本消息：" + ((TextMessage) message).getText());
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
