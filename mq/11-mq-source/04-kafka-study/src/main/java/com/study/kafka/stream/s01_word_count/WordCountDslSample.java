package com.study.kafka.stream.s01_word_count;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

public class WordCountDslSample {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.120.41:9092");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		// 用来创建topology
		StreamsBuilder builder = new StreamsBuilder();
		// KStream是键值对记录流的抽象。例如，<K1:V1>， <K2:V2>。
		// 一个KStream可以是由一个或多个Topic定义的，消费Topic中的消息产生一个个 <k:V>记录；
		// 也可是KStream转换的结果；KTable还可以转换为KStream。
		KStream<String, String> textLines = builder.stream("source-topic");
		// KTable是changelog stream的抽象
		KTable<String, Long> wordCounts = textLines.flatMapValues(textLine -> Arrays.asList(textLine.toLowerCase().split("\\W+")))
				.groupBy((key, word) -> word).count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>> as("counts-store"));

		wordCounts.toStream().to("sink-topic", Produced.with(Serdes.String(), Serdes.Long()));

		KafkaStreams streams = new KafkaStreams(builder.build(), props);
		streams.start();
	}
}
