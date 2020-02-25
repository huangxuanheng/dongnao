package edu.dongnao.study.memcached.lesson1_example;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
@Profile("single")
public class AppConfig {
	@Value("${memcached_host}")
	private String memcachedHost;
	@Value("${memcached_port}")
	private int memcachedPort;

    @Bean
    public MemcachedClient memcachedClient() throws IOException {
        return new XMemcachedClient(memcachedHost, memcachedPort);
    }
}
