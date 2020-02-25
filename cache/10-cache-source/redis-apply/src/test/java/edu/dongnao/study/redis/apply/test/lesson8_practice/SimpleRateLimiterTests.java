package edu.dongnao.study.redis.apply.test.lesson8_practice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dongnao.study.redis.apply.lesson8_practice.SimpleLimiter;

/**
 * SimpleRateLimiterTests
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("practice") // 设置profile
public class SimpleRateLimiterTests {
	@Autowired
	SimpleLimiter limiter;
	
	@Test
	public void test() {
		System.out.println("测试简单限流，1分钟允许访问10次");
		for(int i = 0; i < 12; i++) {
			// 1分钟允许访问10次
			boolean result = limiter.isActionAllowed("user:hash", "page/limit", 1 * 60, 10);
			System.out.println((i+1)+"次，获取令牌："+result);
			try {
				Thread.sleep(3000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

