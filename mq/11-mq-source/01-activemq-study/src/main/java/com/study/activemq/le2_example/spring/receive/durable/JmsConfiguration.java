package com.study.activemq.le2_example.spring.receive.durable;

import javax.jms.ConnectionFactory;

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

@Configuration
public class JmsConfiguration {

	@Bean
	public DefaultJmsListenerContainerFactory myFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		// This provides all boot's default to this factory, including the
		// message converter
		configurer.configure(factory, connectionFactory);
		// You could still override some of Boot's default if necessary.
		// factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
		// factory.setSessionTransacted(true);

		// 持久订阅设置
		// 1、设置为topic方式目标
		factory.setPubSubDomain(true);
		// 2、设置客户端id (区分到客户端级别)
		factory.setClientId("client-2");
		// 3、设置为持久订阅
		factory.setSubscriptionDurable(true);

		return factory;
	}

}
