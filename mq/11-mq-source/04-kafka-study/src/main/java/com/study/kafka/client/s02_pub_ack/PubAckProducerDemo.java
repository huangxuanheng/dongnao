package com.study.kafka.client.s02_pub_ack;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class PubAckProducerDemo {

	// 【说明】 这个示例中用Callback,也用了 ProducerInterceptor ，ProducerInterceptor 在
	// CallBack前执行。

	public static void main(String[] args) throws Exception {
		// Producer的配置参数含义说明：http://kafka.apachecn.org/documentation.html#configuration
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.100.16:9092");
		// 客户端标识,让Broker可以更好地区分客户端，非必需。
		props.put(CommonClientConfigs.CLIENT_ID_CONFIG, "client-1");
		// 配置 ProducerInterceptor
		props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,
				"com.study.kafka.client.s02_pub_ack.MyProducerInterceptor1," + "com.study.kafka.client.s02_pub_ack.MyProducerInterceptor2");
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

		try (Producer<String, String> producer = new KafkaProducer<>(props);) {

			for (int i = 0; i < 100; i++) {
				String message = "message-" + i;
				// 非阻塞方式来处理发送结果。
				producer.send(new ProducerRecord<String, String>("test", Integer.toString(i), message), new Callback() {

					@Override
					public void onCompletion(RecordMetadata metadata, Exception exception) {
						if (exception != null) {
							// 发送时发送了异常,异常的处理逻辑：重发几次/记录
						} else {
							System.out.println("The offset of the record we just sent is: " + metadata.offset() + " message: " + message);

						}
					}
				});

				TimeUnit.SECONDS.sleep(1L);
			}
		}
	}
}
