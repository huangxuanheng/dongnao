package com.study.kafka.client.s05_consumer_manual_commit;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public class ManualPartitionCommitConsumer {

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
				// 按分片来处理得到的数据
				for (TopicPartition partition : records.partitions()) {
					System.out.println("************* partition:" + partition);
					// 遍历处理分片的数据
					List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
					for (ConsumerRecord<String, String> record : partitionRecords) {
						System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
					}
					// 取到的分片的最后一条数据的offset值
					long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
					// 提交该分片的消费offset = lastOffset + 1 .
					consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
				}
			}
		}
	}
}
