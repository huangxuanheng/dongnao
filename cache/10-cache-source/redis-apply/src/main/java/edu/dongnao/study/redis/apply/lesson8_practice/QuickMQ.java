package edu.dongnao.study.redis.apply.lesson8_practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * QuickMQ
 * 高速队列
 * stream
 * 
 */
@Service
@Profile("practice")
public class QuickMQ {
	private int capacity;
	@Autowired
    StringRedisTemplate stringRedisTemplate; // redis客户端
	
	public void put() {
		
	}
	
	public void remove() {
		
	}
}

