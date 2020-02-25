package edu.dongnao.study.redis.apply.test.lesson8_practice;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dongnao.study.redis.apply.lesson8_practice.JavaFunnelLimiter;

/**
 * JavaFunnelLimiterTests
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("practice") // 设置profile
public class JavaFunnelLimiterTests {
	
	public static void main(String[] args) {
		testFunnel();
	}
	
	public static void testFunnel() {
		System.out.println("测试漏牌桶算法");
		JavaFunnelLimiter limiter = new JavaFunnelLimiter();
		for(int i = 0; i < 10; i++) {
			// 1s操作一次, 0.001f
			boolean result = limiter.isActionAllowed("user01", "page/limit", 5, 0.001f);
			System.out.println((i+1)+"次，获取令牌："+result);
			try {
				Thread.sleep(300L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}

