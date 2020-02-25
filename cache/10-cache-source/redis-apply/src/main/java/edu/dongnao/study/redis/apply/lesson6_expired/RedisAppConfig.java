package edu.dongnao.study.redis.apply.lesson6_expired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Profile("expired")
public class RedisAppConfig {
	// 主
	@Value("${redis_host}")
	private String redisHost;
	@Value("${redis_port}")
	private int redisPort;
	// 备份
	@Value("${back.redis_host}")
	private String backRedisHost;
	@Value("${back.redis_port}")
	private int backRedisPort;
	
	//========================main redis========================
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        System.out.println("使用单机版本");
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }
    
    @Bean
    public LettuceConnectionFactory redisConnectionFactory1() {
        System.out.println("使用哨兵版本");
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master("mymaster")
                // 哨兵地址
                .sentinel("192.168.100.12", 26379)
                .sentinel("192.168.100.11", 26380)
                .sentinel("192.168.100.11", 26381);
        return new LettuceConnectionFactory(sentinelConfig);
    }

    @Bean
    public StringRedisTemplate mainRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
    	StringRedisTemplate redisTemplate = new StringRedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
    
	@Bean
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 可以配置对象的转换规则，比如使用json格式对object进行存储。
        // Object --> 序列化 --> 二进制流 --> redis-server存储
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }
    
    
    // ---- Redis ---- 备份缓存

 	@Bean
 	public LettuceConnectionFactory backRedisConnectionFactory() {
 		return new LettuceConnectionFactory(
 				new RedisStandaloneConfiguration(backRedisHost, backRedisPort));
 	}

 	@Bean
 	public StringRedisTemplate backRedisTemplate(LettuceConnectionFactory backRedisConnectionFactory) {
 		StringRedisTemplate redisTemplate = new StringRedisTemplate();
 		redisTemplate.setConnectionFactory(backRedisConnectionFactory);
 		return redisTemplate;
 	}
}