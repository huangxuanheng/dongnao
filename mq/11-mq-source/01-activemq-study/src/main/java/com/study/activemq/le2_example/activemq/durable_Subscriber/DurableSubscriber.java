package com.study.activemq.le2_example.activemq.durable_Subscriber;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;

//持久订阅时，客户端向JMS 注册一个识别自己身份的ID（clientId必须有）
//当这个客户端处于离线时，JMS Provider 会为这个ID 保存所有发送到主题的消息
//当客户再次连接到JMS Provider时，会根据自己的ID 得到所有当自己处于离线时发送到主题的消息。
//tips： ActiveMQ.Advisory开头的消息是activemq提供的一个管理消息推送
//http://activemq.apache.org/advisory-message.html
//虚拟主题：https://www.cnblogs.com/jiangxiaoyaoblog/p/5659734.html
//http://activemq.apache.org/what-is-the-difference-between-a-virtual-topic-and-a-composite-destination.html
public class DurableSubscriber {
	public static void main(String[] args) {
		new ConsumerThread("tcp://mq.study.com:61616", "durableTopic").start();
		// 通过brokerurl上指定clientid
		// new ConsumerThread("tcp://mq.study.com:61616?jms.clientID=x",
		// "durableTopic").start();
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

			// 通过conn对象，持久订阅需指定ClientId
			conn.setClientID("client-1");

			conn.start(); // 一定要启动

			// 3、创建会话（可以创建一个或者多个session）
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// 4、创建消息消费目标(Topic or Queue)
			Topic destination = session.createTopic(destinationUrl);

			// 5、创建消息消费者 http://activemq.apache.org/destination-options.html
			TopicSubscriber consumer = session.createDurableSubscriber(destination, "I love Durable");

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
