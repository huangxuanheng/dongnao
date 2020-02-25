package com.study.kafka.client.s04_consumer_group;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ConsumerDemo2 {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.120.41:9092");
		props.put(CommonClientConfigs.CLIENT_ID_CONFIG, "消费者-2");
		// 设置消费组
		props.put("group.id", "test");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		String topic = "test-group";

		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);) {
			// 订阅topics
			consumer.subscribe(Arrays.asList(topic));

			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1L));
				for (ConsumerRecord<String, String> record : records)
					System.out.printf("消费者-2：partition = %d,offset = %d, key = %s, value = %s%n", record.partition(), record.offset(), record.key(),
							record.value());
			}
		}
	}
}
