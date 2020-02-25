package com.study.kafka.client.s03_transaction;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerDemo {

	public static void main(String[] args) throws Exception {
		// Producer的配置参数含义说明：http://kafka.apachecn.org/documentation.html#configuration
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.100.16:9092");
		// 客户端标识,让Broker可以更好地区分客户端，非必需。
		props.put(CommonClientConfigs.CLIENT_ID_CONFIG, "client-2");
		// broker回复发布确认的方式
		props.put("acks", "all");
		// Producer是采用批量的方式来提高发送的吞吐量量的，这里指定批大小，单位字节
		props.put("batch.size", 16384);
		// 批量发送的等待时长（即有一条数据后等待多长时间接受其他要发送的数据组成一批发送），这个是对上面大小的补充。
		props.put("linger.ms", 1);
		// 存放数据的buffer的大小
		props.put("buffer.memory", 33554432);
		// 设置事务id
		props.put("transactional.id", "my-transactional-id");

		try (Producer<String, String> producer = new KafkaProducer<>(props, new StringSerializer(), new StringSerializer());) {
			// 初始化事务
			producer.initTransactions();

			try {
				// 开始事务
				producer.beginTransaction();
				for (int i = 0; i < 20; i++) {
					String message = "message-" + i;
					producer.send(new ProducerRecord<String, String>("test", Integer.toString(i), message));
					System.out.println("发送：" + message);
					TimeUnit.SECONDS.sleep(1L);
				}
				// 提交事务
				producer.commitTransaction();
			} catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
				// We can't recover from these exceptions, so our only option is
				// to close the producer and exit.
				producer.close();
			} catch (KafkaException e) {
				// For all other exceptions, just abort the transaction and try
				// again.
				// 取消事务
				producer.abortTransaction();
			}
		}
	}
}
