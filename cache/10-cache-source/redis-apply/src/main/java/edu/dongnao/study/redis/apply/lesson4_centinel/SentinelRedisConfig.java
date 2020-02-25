package edu.dongnao.study.redis.apply.lesson4_centinel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@Profile("sentinel")
public class SentinelRedisConfig {
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        System.out.println("使用哨兵版本");
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master("mymaster")
                // 哨兵地址
                .sentinel("192.168.100.12", 26379)
                .sentinel("192.168.100.11", 26380)
                .sentinel("192.168.100.11", 26381);
        return new LettuceConnectionFactory(sentinelConfig);
    }
}