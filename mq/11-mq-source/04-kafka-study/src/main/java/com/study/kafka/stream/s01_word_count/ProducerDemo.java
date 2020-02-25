package com.study.kafka.stream.s01_word_count;

import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class ProducerDemo {

	public static void main(String[] args) throws Exception {
		// Producer的配置参数含义说明：http://kafka.apachecn.org/documentation.html#configuration
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.120.41:9092");
		// 客户端标识,让Broker可以更好地区分客户端，非必需。
		props.put(CommonClientConfigs.CLIENT_ID_CONFIG, "client-1");
		// broker回复发布确认的方式
		props.put("acks", "all");
		// 当发送失败时重试几次
		props.put("retries", 0);
		// Producer是采用批量的方式来提高发送的吞吐量量的，这里指定批大小，单位字节
		props.put("batch.size", 16384);
		// 批量发送的等待时长（即有一条数据后等待多长时间接受其他要发送的数据组成一批发送），这个是对上面大小的补充。
		props.put("linger.ms", 1);
		// 存放数据的buffer的大小
		props.put("buffer.memory", 33554432);
		// key的序列化器
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		// 消息数据的序列化器
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		try (Producer<String, String> producer = new KafkaProducer<>(props); Scanner sc = new Scanner(System.in)) {

			for (int i = 0; i < 100; i++) {
				String message = sc.nextLine();

				Future<RecordMetadata> resultFuture = producer.send(new ProducerRecord<String, String>("source-topic", Integer.toString(i), message));

				// 如果你想要同步阻塞等待结果
				RecordMetadata rm = resultFuture.get();

				System.out.println("发送：" + message + " hasOffset: " + rm.hasOffset() + " partition: " + rm.partition() + " offset: " + rm.offset());
				TimeUnit.SECONDS.sleep(1L);
			}
		}
	}
}
