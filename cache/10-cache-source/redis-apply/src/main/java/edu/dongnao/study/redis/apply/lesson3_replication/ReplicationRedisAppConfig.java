package edu.dongnao.study.redis.apply.lesson3_replication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@Profile("replication") // 主从模式
public class ReplicationRedisAppConfig {
	@Value("${redis_host}")
	private String redisHost;
	@Value("${redis_port}")
	private int redisPort;
	
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        // master:192.168.1.128    slave:192.168.1.145
        // 默认slave只能进行读取，不能写入
        // 如果你的应用程序需要往redis写数据，建议连接master
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }
}