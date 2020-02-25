package com.study.kafka.stream.s01_word_count;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.processor.Punctuator;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

// 1 编写我们的流处理器（实现 Processor接口）
public class WordCountProcessor implements Processor<String, String> {

	private ProcessorContext context;
	private KeyValueStore<String, Long> kvStore;

	/*
	 * init(ProcessorContext context)
	 * 使用给定ProcessorContext上下文初始化此处理器实例。框架确保在初始化包含它的topology时，调用每个处理器init()一次。
	 * 当框架使用处理器完成时，将调用close();稍后，框架可以通过再次调用init()重用处理器。 给入的ProcessorContext上下文
	 * 可用来访问流处理流程的topology以及record meta data、调度要定期调用的方法以及访问附加的StateStore状态存储。
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init(ProcessorContext context) {
		// keep the processor context locally because we need it in punctuate()
		// and commit()
		this.context = context;

		/**
		 * 调度processors处理器的定期操作。处理器可以在初始化或处理过程中调用此方法来调度一个周期性回调(称为标点)
		 * 重点是搞清楚它的type参数的含义： PunctuationType type 参数用来说明时间概念，可选的值：
		 * PunctuationType.STREAM_TIME 流时间,多长时间后，流中有新数据流入，计算后执行标记逻辑
		 * PunctuationType.WALL_CLOCK_TIME 表示时间是参照系统时间。间隔多长时间就标记一次。
		 */
		// schedule a punctuation method every 1000 milliseconds.
		// this.context.schedule(30000, PunctuationType.WALL_CLOCK_TIME, new
		// Punctuator() {
		this.context.schedule(30000, PunctuationType.STREAM_TIME, new Punctuator() {
			@Override
			public void punctuate(long timestamp) {
				KeyValueIterator<String, Long> iter = kvStore.all();

				while (iter.hasNext()) {
					KeyValue<String, Long> entry = iter.next();
					context.forward(entry.key, entry.value.toString());
				}

				// it is the caller's responsibility to close the iterator on
				// state store;
				// otherwise it may lead to memory and file handlers leak
				// depending on the
				// underlying state store implementation.
				iter.close();

				// commit the current processing progress
				context.commit();
			}
		});

		// retrieve the key-value store named "Counts"
		this.kvStore = (KeyValueStore<String, Long>) context.getStateStore("Counts");
	}

	/*
	 * （K-V结构）流数据的处理方法，一次处理一条数据，计算逻辑写在该方法中
	 */
	@Override
	public void process(String key, String value) {

		String[] words = value.toLowerCase().split(" ");

		for (String word : words) {
			Long oldValue = this.kvStore.get(word);

			if (oldValue == null) {
				this.kvStore.put(word, 1L);
			} else {
				this.kvStore.put(word, oldValue + 1L);
			}
		}
	}

	/*
	 * 资源释放方法 当Processor被流处理框架使用完后后，框架将调用其close来进行资源释放。
	 * 注意:不要在此方法中关闭任何流管理资源，比如这里的StateStore，因为它们是由框架管理的。
	 */
	@Override
	public void close() {
		// close any resources managed by this processor.
		// Note: Do not close any StateStores as these are managed
		// by the library
	}

	public static void main(String[] args) {

		// 2 定义stateStore
		Map<String, String> changelogConfig = new HashMap();

		KeyValueBytesStoreSupplier countStoreSupplier = Stores.inMemoryKeyValueStore("Counts");
		StoreBuilder<KeyValueStore<String, Long>> builder = Stores.keyValueStoreBuilder(countStoreSupplier, Serdes.String(), Serdes.Long())
				.withLoggingEnabled(changelogConfig); // enable
														// changelogging,with
														// custom changelog
														// settings

		// 3 定义处理流程Topology
		Topology topology = new Topology();

		// add the source processor node that takes Kafka topic "source-topic"
		// as input
		topology.addSource("Source", "source-topic")

				// add the WordCountProcessor node which takes the source
				// processor as its upstream processor
				.addProcessor("Process", () -> new WordCountProcessor(), "Source")

				// add the count store associated with the WordCountProcessor
				// processor
				.addStateStore(builder, "Process")

				// add the sink processor node that takes Kafka topic
				// "sink-topic" as output
				// and the WordCountProcessor node as its upstream processor
				.addSink("Sink", "sink-topic", "Process");

		// 4 定义流属性
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-processing-application");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.100.9:9092");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		// 指定计算状态本地持久存储目录
		// props.put(StreamsConfig.STATE_DIR_CONFIG,
		// "/data/kafka-stream/state");

		// 5 创建流处理
		KafkaStreams streams = new KafkaStreams(topology, props);
		// streams.setGlobalStateRestoreListener(new
		// ConsoleGlobalRestoreListerner());
		// 6 开启流处理
		streams.start();
	}
}
