package edu.dongnao.study.redis.apply.lesson5_cluster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Profile("cluster")
public class ClusterService {
    @Autowired
    private StringRedisTemplate template;

    public void set(String userId, String userInfo) {
        template.opsForValue().set(userId, userInfo);
    }
}
