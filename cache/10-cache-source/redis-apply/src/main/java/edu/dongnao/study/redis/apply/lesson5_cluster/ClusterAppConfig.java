package edu.dongnao.study.redis.apply.lesson5_cluster;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.util.Arrays;

@Configuration
// 在cluster环境下生效
@Profile("cluster")
class ClusterAppConfig {
	
    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        System.out.println("加载cluster环境下的redis client配置");
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(Arrays.asList(
        		"192.168.100.30:7000",
                "192.168.100.30:7001",
                "192.168.100.30:7002",
                "192.168.100.30:7003",
                "192.168.100.30:7004",
                "192.168.100.30:7005"
        ));
        // 自适应集群变化
        return new JedisConnectionFactory(redisClusterConfiguration);
    }
}