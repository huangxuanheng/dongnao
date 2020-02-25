package com.study.kafka.client.s05_consumer_manual_commit;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ManualCommitConsumerDemo {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.120.41:9092");
		props.put("group.id", "test");
		// 设置手动提交消费offset
		props.put("enable.auto.commit", "false");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);) {
			consumer.subscribe(Arrays.asList("test", "test-group"));
			final int minBatchSize = 10;
			List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(200L));

				for (ConsumerRecord<String, String> record : records) {
					System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
					buffer.add(record);
				}
				if (buffer.size() >= minBatchSize) {
					insertIntoDb(buffer);
					// 手动同步提交消费者offset到zookeeper
					consumer.commitSync();
					buffer.clear();
				}
			}
		}
	}

	private static void insertIntoDb(List<ConsumerRecord<String, String>> buffer) {
		// Insert into db
	}

}
