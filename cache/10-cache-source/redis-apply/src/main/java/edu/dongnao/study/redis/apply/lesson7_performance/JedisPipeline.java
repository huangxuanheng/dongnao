package edu.dongnao.study.redis.apply.lesson7_performance;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * JedisPipeline
 * 
 */
@Service
@Profile("performance")
public class JedisPipeline {
	@Value("${redis_host}")
	private String redisHost;
	@Value("${redis_port}")
	private int redisPort;
	
	/**
	 * 用jedis实现的pipeline，有三个步骤。
	 * @param batchSize
	 */
	public void pipeline(int batchSize) {
		Jedis jedis = new Jedis(redisHost, redisPort);
		Pipeline p = jedis.pipelined();
		List<Response<?>> list = new ArrayList<Response<?>>();
		long s = System.currentTimeMillis();
		for(int i=0; i< batchSize; i++) {
			Response<?> r = p.get("pipeline"+i);
			list.add(r);
		}
		System.out.println("write cost:"+(System.currentTimeMillis() - s));
		s = System.currentTimeMillis();
		p.sync();
		list.forEach((e)->{
			System.out.println(e.get());
		});
		System.out.println("read cost:"+(System.currentTimeMillis() - s));
		p.close();
		jedis.close();
	}
	
}

