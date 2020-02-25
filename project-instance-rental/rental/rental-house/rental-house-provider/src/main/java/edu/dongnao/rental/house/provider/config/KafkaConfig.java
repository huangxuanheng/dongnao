package edu.dongnao.rental.house.provider.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
	public static final String INDEX_TOPIC = "house_build";
    
    @Bean
    public NewTopic initTopic() {
    	 return TopicBuilder.name(INDEX_TOPIC)
    	            .partitions(1)
    	            .replicas(1)
    	            .build();
    }
}
