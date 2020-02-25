package com.dongnaoedu.mall.manager.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis 缓存管理器
 * 
 * @author allen
 */
public class RedisCacheManager implements CacheManager {
	private static final Logger log = LoggerFactory.getLogger(RedisCacheManager.class);

	private RedisTemplate redisTemplate;

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		return new RedisCache<K, V>(name, redisTemplate);
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
