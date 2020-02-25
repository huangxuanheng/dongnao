package edu.dongnao.study.redis.apply.lesson3_replication;

import io.lettuce.core.ReadFrom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@Profile("replication-rw") // 主从 - 读写分离模式
public class ReplicationRWRedisAppConfig {
	@Value("${redis_host}")
	private String redisHost;
	@Value("${redis_port}")
	private int redisPort;
	
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        System.out.println("使用读写分离版本");
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.SLAVE_PREFERRED)
                .build();
        
        // master:192.168.1.128    slave:192.168.1.145
        // 默认slave只能进行读取，不能写入
        // 如果你的应用程序需要往redis写数据，建议连接master
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(serverConfig, clientConfig);
    }
}