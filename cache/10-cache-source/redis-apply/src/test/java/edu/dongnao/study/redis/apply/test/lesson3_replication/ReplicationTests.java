package edu.dongnao.study.redis.apply.test.lesson3_replication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dongnao.study.redis.apply.lesson3_replication.ReplicationDemoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("replication") // 激活主从复制的配置
public class ReplicationTests {
    @Autowired
    ReplicationDemoService replicationDemoService;

    @Test
    public void setTest() {
        replicationDemoService.setByCache("hash", "hahhhhh");
    }
}
