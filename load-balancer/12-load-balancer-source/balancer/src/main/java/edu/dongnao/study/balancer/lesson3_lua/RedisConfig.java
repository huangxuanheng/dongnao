package edu.dongnao.study.balancer.lesson3_lua;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 多Redis配置
 */
@Configuration
//开启spring cache注解功能
@EnableCaching
public class RedisConfig {
	// ---- Redis ---- 主缓存
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.redis.main")
	public RedisStandaloneConfiguration mainRedisConfig() {
		return new RedisStandaloneConfiguration();
	}

	@Bean
	@Primary
	public LettuceConnectionFactory mainRedisConnectionFactory(RedisStandaloneConfiguration mainRedisConfig) {
		return new LettuceConnectionFactory(mainRedisConfig);
	}

	@Bean
	public RedisTemplate<String, String> mainRedisTemplate(LettuceConnectionFactory mainRedisConnectionFactory) {
		StringRedisTemplate redisTemplate = new StringRedisTemplate();
		redisTemplate.setConnectionFactory(mainRedisConnectionFactory);
		return redisTemplate;
	}
	
	// 配置Spring Cache注解功能
    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory redisConnectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
        return cacheManager;
    }

	// ---- Redis ---- 备份缓存
	@Bean
	@ConfigurationProperties(prefix = "spring.redis.bak")
	public RedisStandaloneConfiguration bakRedisConfig() {
		return new RedisStandaloneConfiguration();
	}

	@Bean
	public LettuceConnectionFactory baknRedisConnectionFactory(@Qualifier("bakRedisConfig") RedisStandaloneConfiguration bakRedisConfig) {
		return new LettuceConnectionFactory(bakRedisConfig);
	}

	@Bean
	public RedisTemplate<String, String> bakRedisTemplate(@Qualifier("baknRedisConnectionFactory") LettuceConnectionFactory baknRedisConnectionFactory) {
		StringRedisTemplate redisTemplate = new StringRedisTemplate();
		redisTemplate.setConnectionFactory(baknRedisConnectionFactory);
		return redisTemplate;
	}
}
