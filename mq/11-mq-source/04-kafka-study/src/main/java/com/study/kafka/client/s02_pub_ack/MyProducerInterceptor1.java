package com.study.kafka.client.s02_pub_ack;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class MyProducerInterceptor1 implements ProducerInterceptor {

	@Override
	public void configure(Map<String, ?> configs) {
		// TODO Auto-generated method stub

	}

	@Override
	public ProducerRecord onSend(ProducerRecord record) {
		// 对record进行一些统一的设置
		return record;
	}

	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
		if (exception != null) {
			exception.printStackTrace();
		} else {
			System.out.println("MyProducerInterceptor1 -- The offset of the record we just sent is: " + metadata.offset());

		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
