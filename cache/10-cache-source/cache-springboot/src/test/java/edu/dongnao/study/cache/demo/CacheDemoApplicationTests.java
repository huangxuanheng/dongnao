package edu.dongnao.study.cache.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.dongnao.study.cache.demo.service.GoodsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheDemoApplicationTests {

    long timed = 0L;

    @Before
    public void start() {
        System.out.println("开始测试");
        timed = System.currentTimeMillis();
    }

    @After
    public void end() {
        System.out.println("结束测试,执行时长：" + (System.currentTimeMillis() - timed));
    }

    @Autowired
    GoodsService service;

    // 商品
    private static final String Goods_ID = "apple";

    @Test
    public void benchmark() throws InterruptedException {
        
    	service.queryStock(Goods_ID);
    }

}
