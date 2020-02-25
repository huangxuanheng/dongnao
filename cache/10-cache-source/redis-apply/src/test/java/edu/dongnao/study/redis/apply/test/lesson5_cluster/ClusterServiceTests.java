package edu.dongnao.study.redis.apply.test.lesson5_cluster;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dongnao.study.redis.apply.lesson5_cluster.ClusterService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("cluster") // 设置profile
// 集群对于客户端而言，基本是无感知的
public class ClusterServiceTests {
    @Autowired
    ClusterService clusterService;

    //@Test
    public void setTest() {
        clusterService.set("hash", "hahhhhh");
        clusterService.set("a", "1");
        clusterService.set("foo", "bar");
    }

    // 测试cluster集群故障时的反应
    @Test
    public void failoverTest() {
        while (true) {
            try {
                long i = System.currentTimeMillis();
                clusterService.set("hash", i + "");
                // delay 10ms
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
    }
}
