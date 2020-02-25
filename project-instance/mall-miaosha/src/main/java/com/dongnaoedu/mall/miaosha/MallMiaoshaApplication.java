package com.dongnaoedu.mall.miaosha;

import com.dongnaoedu.cas.client.springboot.config.EnableCasClientSLO;
import net.unicon.cas.client.configuration.EnableCasClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCasClient
@EnableCasClientSLO
public class MallMiaoshaApplication {

	public static void main(String[] args) {
		// 秒杀有排期：
		// http://mjbbs.jd.com/forum.php?mod=viewthread&action=printable&tid=79147
		// 秒杀预热机制，半小时一次。
		// 1、 job任务加载秒杀商品信息到redis集群。通过dubbo服务，查询秒杀商品信息
		// 2、 使用令牌桶策略针对数据库处理进行限流，每种商品对应一个令牌桶
		// 3、 商品量大的，走MQ。
		SpringApplication.run(MallMiaoshaApplication.class, args);
	}

}
