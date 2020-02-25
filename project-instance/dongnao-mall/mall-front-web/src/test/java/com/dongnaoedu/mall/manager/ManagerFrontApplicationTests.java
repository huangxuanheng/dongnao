package com.dongnaoedu.mall.manager;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManagerFrontApplicationTests {

	@Autowired
	private JedisPool jedisPool;
	
	@Test
	public void test() {
		String uuid = UUID.randomUUID().toString();
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(uuid, 1000, "allen");
        }
		
	}

}
