package edu.dongnao.study.redis.apply.test.lesson7_performance;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dongnao.study.redis.apply.lesson7_performance.RedisClient;

/**
 * redis客户端测试
 * 
 * RedisClientTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("performance") // 设置profile
public class RedisClientTest {
	@Value("${redis_host}")
	private String redisHost;
	@Value("${redis_port}")
	private int redisPort;
	
	@Test
    public void test() throws IOException {
        RedisClient jedis = new RedisClient(redisHost, redisPort);
        jedis.set("hello","dongnao!");
        System.out.println("设置完成####################");

        String value = jedis.get("hello");
        System.out.println(value);
    }
}
