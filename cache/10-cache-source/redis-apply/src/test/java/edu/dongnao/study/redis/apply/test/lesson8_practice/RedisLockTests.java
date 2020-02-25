package edu.dongnao.study.redis.apply.test.lesson8_practice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dongnao.study.redis.apply.lesson8_practice.RedisDistributedLock;

/**
 * RedisLockTests
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("practice") // 设置profile
public class RedisLockTests {
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Test
	public void test() {
		RedisDistributedLock lock = new RedisDistributedLock("payLock001", 30, stringRedisTemplate);
		lock.lock();
		try {
			// 执行业务
			Thread.sleep(3000L);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
}

