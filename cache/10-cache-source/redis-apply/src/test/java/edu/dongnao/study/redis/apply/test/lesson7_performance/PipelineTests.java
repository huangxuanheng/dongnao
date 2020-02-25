package edu.dongnao.study.redis.apply.test.lesson7_performance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dongnao.study.redis.apply.lesson7_performance.JedisPipeline;
import edu.dongnao.study.redis.apply.lesson7_performance.RedisPipeline;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("performance") // 设置profile
public class PipelineTests {
    @Autowired
    RedisPipeline redisPipeline;
    @Autowired
    JedisPipeline jedisP;

    //@Test
    public void setTest() {
    	int batchSize = 1000;
    	long s = System.currentTimeMillis();
    	redisPipeline.setCommand(batchSize);
    	long t1 = (System.currentTimeMillis() - s);
    	
    	s = System.currentTimeMillis();
    	redisPipeline.pipeline(batchSize);
    	System.out.println("普通set耗时："+t1+"ms， pipeline耗时："+(System.currentTimeMillis() - s)+"ms");
    }
    
    @Test
    public void testJedisPipeline() {
    	jedisP.pipeline(1000);
    }

}
