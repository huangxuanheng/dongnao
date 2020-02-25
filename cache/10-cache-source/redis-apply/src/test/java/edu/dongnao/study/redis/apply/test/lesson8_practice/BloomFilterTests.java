package edu.dongnao.study.redis.apply.test.lesson8_practice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dongnao.study.redis.apply.lesson8_practice.RedisBloomFilter;

/**
 * BloomFilterTests
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("practice") // 设置profile
public class BloomFilterTests {
	@Autowired
	RedisBloomFilter filter;
	
	@Test
	public void test() {
		for(int i = 0; i< 100000; i++) {
			filter.addElement("myBloomFilter", "user:"+i);
			boolean result = filter.exists("myBloomFilter", "user:"+i);
			System.out.println("user:"+i+"是否存在："+result);
		}
		
	}
}

