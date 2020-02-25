package edu.dongnao.study.redis.apply.test.lesson1.custom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dongnao.study.redis.apply.lesson1.custom.CustomAnnoDemoService;
import edu.dongnao.study.redis.apply.pojo.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("custom") // 设置profile
public class CustomCacheTests {

    @Autowired
    CustomAnnoDemoService customDemoService;

    // get
    @Test
    public void springCacheTest() throws Exception {
        User user = customDemoService.findUserById("wahaha");
        System.out.println(user);
    }
}
