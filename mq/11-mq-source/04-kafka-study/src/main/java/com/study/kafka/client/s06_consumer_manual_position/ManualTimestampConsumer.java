package com.study.kafka.client.s06_consumer_manual_position;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;

public class ManualTimestampConsumer {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.100.9:9092");
		props.put("group.id", "test");
		props.put("enable.auto.commit", "true");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);) {
			// 不是订阅Topic
			// consumer.subscribe(Arrays.asList("test", "test-group"));
			// 而是直接分配该消费者读取某些分片
			TopicPartition partition = new TopicPartition("test", 0);
			consumer.assign(Arrays.asList(partition));
			// 指定消费开始的位置
			// consumer.seek(partition, 10);

			// 从某时刻开始消费
			Map<TopicPartition, Long> partitionTimestampMap = new HashMap<TopicPartition, Long>();
			long timestamp = 1566889994418L;
			partitionTimestampMap.put(partition, timestamp);
			// 取分片的某时刻开始的offset
			Map<TopicPartition, OffsetAndTimestamp> offsetMap = consumer.offsetsForTimes(partitionTimestampMap);
			// 指定消费开始的位置
			consumer.seek(partition, offsetMap.get(partition).offset());

			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(200L));
				for (ConsumerRecord<String, String> record : records) {
					System.out.printf("offset = %d, key = %s, timestamp = %d value = %s%n", record.offset(), record.key(), record.timestamp(),
							record.value());

				}
			}
		}
	}

}
