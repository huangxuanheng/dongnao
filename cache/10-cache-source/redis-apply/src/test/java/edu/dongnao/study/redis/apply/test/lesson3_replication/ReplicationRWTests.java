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
@ActiveProfiles("replication-rw") // 激活主从集群-读写分离的配置
public class ReplicationRWTests {
    @Autowired
    ReplicationDemoService replicationDemoService;

    @Test
    public void setTest() {
    	// set主库写入
        replicationDemoService.setByCache("hash", "xxxx");
        // get从库读取
        String result = replicationDemoService.getByCache("hash");
        System.out.println("从缓存中读取到数据：" + result);
    }
}
