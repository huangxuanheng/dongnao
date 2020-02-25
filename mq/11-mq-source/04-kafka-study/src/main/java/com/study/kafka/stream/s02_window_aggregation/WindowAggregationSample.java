package com.study.kafka.stream.s02_window_aggregation;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.kstream.WindowedSerdes;

public class WindowAggregationSample {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "order-amount-application");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.120.41:9092");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		StreamsBuilder builder = new StreamsBuilder();
		KStream<String, String> textLines = builder.stream("source-topic");
		KTable<Windowed<String>, Long> aggregatedTable = textLines.groupByKey()..windowedBy(TimeWindows.of(2 * 60 * 1000)).count();

		aggregatedTable.toStream().to("window-sink-topic", Produced.with(WindowedSerdes.timeWindowedSerdeFrom(String.class), Serdes.Long()));

		KafkaStreams streams = new KafkaStreams(builder.build(), props);
		streams.start();
	}
}
