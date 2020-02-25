package com.study.kafka.client.s01_pub_sub;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class AutomaticOffsetCommitConsumerDemo {

	public static void main(String[] args) {
		// 消费者参数详见：http://kafka.apachecn.org/documentation.html#newconsumerconfigs
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put(CommonClientConfigs.CLIENT_ID_CONFIG, "client-1");
		// 设置消费组
		props.put("group.id", "test");
		// 开启自动消费offset提交
		// 如果此值设置为true，consumer会周期性的把当前消费的offset值保存到zookeeper。当consumer失败重启之后将会使用此值作为新开始消费的值。
		props.put("enable.auto.commit", "true");
		// 自动消费offset提交的间隔时间
		props.put("auto.commit.interval.ms", "1000");
		// key 的反序列化器
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		// 消息的反序列化器
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		String topic = "test";

		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);) {
			// 订阅topics
			consumer.subscribe(Arrays.asList(topic));

			while (true) {
				// kafka中是拉模式，poll的时间参数是告诉Kafka:如果当前没有数据，等待多久再响应
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1L));
				for (ConsumerRecord<String, String> record : records)
					System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());

				// 设定从哪里开始拉取消息 （需要时设置）
				// consumer.seek(new TopicPartition(topic, 0), 0);
			}
		}
	}
}
