package edu.dongnao.study.redis.apply.lesson2_othertype.pusub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 接收消息通知，直接用客户端的方式
 */
@Component
@Profile("pubsub")
public class MsgChannelListener {
	@Autowired
	@SuppressWarnings("rawtypes")
    RedisTemplate redisTemplate;

	@PostConstruct
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public void setup() {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.subscribe((message, pattern) -> {
                    System.out.println("使用redisTemplate收到消息：" + message);
                }, PubsubRedisConfig.PUBSUB_CHANNEL_NAME.getBytes());
                return null;
            }
        });
    }
}
