package com.study.activemq.le2_example.activemq.delay;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

/**
 * 简单生产者
 */
public class Producer {
	public static void main(String[] args) {
		new ProducerThread("tcp://mq.study.com:61616", "delayQueue").start();
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
				connectionFactory.setUseAsyncSend(true);
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

				// 延时、调度消息
				// 【不可用，这是JMS2.0中的方法】设置producer发送的消息的延迟递送时间
				// producer.setDeliveryDelay(60000L);
				// ActiveMQ 中的方案
				// http://activemq.apache.org/delay-and-schedule-message-delivery.html

				// 延时 5秒
				TextMessage message = session.createTextMessage("Delay message - 1!");
				message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5 * 1000L);

				// 延时 5秒，投递3次，间隔10秒 (投递次数=重复次数+默认的一次)
				TextMessage message2 = session.createTextMessage("Delay message  - 2!");
				message2.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5 * 1000L); // 延时
				message2.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 2 * 1000L); // 投递间隔
				message2.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 2); // 重复次数

				// CRON 表达式的方式
				TextMessage message3 = session.createTextMessage("Delay message - 3!");
				message3.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, "18 * * * *");

				// CRON 表达式的方式 以及 和上面参数的组合，CRON表达式指定开始时间
				TextMessage message4 = session.createTextMessage("Delay message - 4!");
				message4.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, "18 * * * *");
				message4.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 1000);
				message4.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 1000);
				message4.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 9);

				// 7、发送消息
				producer.send(message);
				producer.send(message2);
				producer.send(message3);
				producer.send(message4);

				System.out.println("Sent delay message: ok");

				// 8、 清理、关闭连接
				session.close();
				conn.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
