package com.study.activemq.le2_example.spring.transaction;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class TransactionSample {

	@Primary
	@Bean
	public JmsTemplate queueJmsTemplate(ConnectionFactory connectionFactory) {
		PropertyMapper map = PropertyMapper.get();
		JmsTemplate template = new JmsTemplate(connectionFactory);
		// template.setDestinationResolver(destinationResolver);
		template.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
		return template;
	}

	@Bean
	public JmsTemplate topicJmsTemplate(ConnectionFactory connectionFactory) {
		PropertyMapper map = PropertyMapper.get();
		JmsTemplate template = new JmsTemplate(connectionFactory);
		// template.setDestinationResolver(destinationResolver);

		return template;
	}

	@Autowired
	@Qualifier("queueJmsTemplate")
	private JmsTemplate jmsTemplate;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public void saveDbAndSendMessage(String data) {
		this.jmsTemplate.convertAndSend("transaction-test", data);
		System.out.println("发送消息完成：" + data);
		this.jdbcTemplate.update("insert t_log(id,log) values(?,?)", System.currentTimeMillis(), data);
	}

	@Transactional
	public void reciveMessageAndSaveDb() {
		String data = (String) this.jmsTemplate.receiveAndConvert("transaction-test");
		this.jdbcTemplate.update("insert t_log(id,log) values(?,?)", System.currentTimeMillis(), data);
	}

	// @Transactional 异步方式下，加 @Transactional 无效，而是走异步异常根据消息确认机制来处理 ，
	// 默认的消息确认模式 为
	// AUTO_ACKNOWLEDGE,抛出异常，则会立马重发，重发provider指定的次数（这里可看出默认配置的重发次数为6），
	// 重发指定次数后还是不成功，则消息将被转移到死信队列 ActiveMQ.DLQ
	@JmsListener(destination = "transaction-test")
	public void reciveMessageAndSaveDb2(String data) {
		System.out.println("收到消息：" + data);
		this.jdbcTemplate.update("insert t_log(id,log) values(?,?)", System.currentTimeMillis(), data);
	}

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(TransactionSample.class, args);
		TransactionSample ts = context.getBean(TransactionSample.class);
		// 发送的示例
		// ts.saveDbAndSendMessage("aaaaaaaaaa aaaaaaaaaa");
		// 接收的示例，请把异步接收注释掉先
		// ts.reciveMessageAndSaveDb();
	}
}
