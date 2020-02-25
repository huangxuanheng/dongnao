package edu.dongnao.study.redis.apply.test.lesson2_othertype;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import edu.dongnao.study.redis.apply.lesson2_othertype.pusub.PubsubRedisConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("pubsub") // 设置profile
public class PubSubTests {
	@Autowired
	@SuppressWarnings("rawtypes")
    RedisTemplate redisTemplate;

	//@Test
	@SuppressWarnings("unchecked")
    public void testPusb() throws InterruptedException {
        System.out.println("开始测试发布订阅机制，6秒后发布一条消息");
        Thread.sleep(6000L);
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                // 发送通知
                Long received = connection.publish(PubsubRedisConfig.PUBSUB_CHANNEL_NAME.getBytes(), "{手机号码10086~短信内容~~}".getBytes());
                return received;
            }
        });
    }

    // 隐藏功能~~黑科技~~当key被删除，或者key过期之后，也会有通知~
	@Test
	@SuppressWarnings("unchecked")
    public void testKeyDelEventChannel() throws InterruptedException {
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.subscribe((message, pattern) -> {
                    System.out.println("通过Key删除事件通道，收到消息：" + message);
                }, "__keyspace@0__:hkkkk del".getBytes());
                return null;
            }
        });

        redisTemplate.opsForValue().set("hkkkk", "hash");
        Thread.sleep(1000L);
        
        redisTemplate.delete("hkkkk");
    }
}
