package edu.dongnao.study.redis.apply.test.lesson2_othertype;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dongnao.study.redis.apply.lesson2_othertype.CommDataTypeDemo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("commType") // 设置profile
public class CommTypeTests {
	@Autowired
	CommDataTypeDemo service;
	
	// 类似：在redis里面存储一个hashmap
    //@Test
    public void hashTest() {
    	service.hash();
    }

    // 列表~ 集合数据存储~ java.util.List，java.util.Stack
    //@Test
    public void list() {
    	service.list();
    }

    // 用set实现（交集 并集）
    //@Test
    public void setTest() {
    	service.set();
    }

    // 游戏排行榜
    @Test
    public void zsetTest() {
    	service.zset();
    }
}

