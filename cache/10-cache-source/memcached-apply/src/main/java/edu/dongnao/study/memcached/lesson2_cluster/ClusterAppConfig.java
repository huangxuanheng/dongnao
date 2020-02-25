package edu.dongnao.study.memcached.lesson2_cluster;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
@Profile("cluster")
public class ClusterAppConfig {
	@Value("${memcached_cluster}")
	private String clusterConfig;
	
    @Bean
    public MemcachedClient memcachedClient() throws IOException {
        String servers = clusterConfig;
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil
                .getAddresses(servers));
        // 默认的客户端计算就是 key的哈希值模以连接数
        // KetamaMemcachedSessionLocator 一致性hash算法
        builder.setSessionLocator(new KetamaMemcachedSessionLocator());
        MemcachedClient client = builder.build();
        return client;
    }
}
