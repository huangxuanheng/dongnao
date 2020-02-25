package edu.dongnao.study.redis.apply.lesson3_replication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Profile("replication")
public class ReplicationDemoService {
    @Autowired
    private StringRedisTemplate template;
    
    
    public void setByCache(String userId, String userInfo) {
        template.opsForValue().set(userId, userInfo);
    }
    
    
    public String getByCache(String userId) {
        return template.opsForValue().get(userId);
    }
}