package com.study.kafka.client.s03_transaction;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ConsumerDemo {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.100.16:9092");
		props.put(CommonClientConfigs.CLIENT_ID_CONFIG, "client-1");
		props.put("group.id", "test");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		// 事务模式下，设置事务隔离级别：读提交
		props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

		String topic = "test";
		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);) {
			// 订阅topics
			consumer.subscribe(Arrays.asList(topic));

			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1L));
				for (ConsumerRecord<String, String> record : records)
					System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
			}
		}
	}
}
