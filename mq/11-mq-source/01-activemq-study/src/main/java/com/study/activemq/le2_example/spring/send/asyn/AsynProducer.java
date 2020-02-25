package com.study.activemq.le2_example.spring.send.asyn;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.AsyncCallback;
import org.messaginghub.pooled.jms.JmsPoolMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.study.activemq.le1_helloworld.spring.Email;

//很晦涩，不推荐使用
@Component
public class AsynProducer {

	@Autowired
	private JmsTemplate jmsTemplate;

	@PostConstruct
	public void sendMessage() {

		jmsTemplate.execute("mailbox", (session, producer) -> {
			// Send a message with a POJO - the template reuse the message
			// converter

			Message message = jmsTemplate.getMessageConverter().toMessage(new Email("info@example.com", "async send"),
					session);
			((ActiveMQMessageProducer) ((JmsPoolMessageProducer) producer).getDelegate()).send(
					jmsTemplate.getDestinationResolver().resolveDestinationName(session, "mailbox", false), message,
					new AsyncCallback() {

						@Override
						public void onException(JMSException exception) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess() {
							try {
								System.out.println(Thread.currentThread().getName() + " 异步发送完成：messageId: "
										+ message.getJMSMessageID());
							} catch (JMSException e) {
							}
						}
					});

			return null;
		});

	}

}
