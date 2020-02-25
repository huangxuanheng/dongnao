/**
 * 
 */
package com.dongnaoedu.mall.miaosha.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 秒杀实现类。 半小时预热一次
 * 
 * @author 动脑学院
 */
@Service
public class MiaoshaService {
	@Autowired
	RabbitTemplate rabbitTemplate;

	@Value("${mall.miaosha.async_threshold:-1}")
	private int asyncThreshold;

	@Autowired
	RedisTemplate<String, String> redisTemplate;

	public String miaosha(String itemId, String userId) {
		// 0、 去重筛选， 购买一次
//		Boolean success = redisTemplate.opsForValue().setIfAbsent(userId + ":" + itemId,
//				String.valueOf(System.currentTimeMillis()));
//		if (!success.booleanValue()) {
//			return "0";
//		}

		// 1、令牌桶策略，过滤无效用户
		// 这个系统中，token的内容，就是该商品的数量
		String itemNum = redisTemplate.opsForList().leftPop("token_" + itemId);
		if (itemNum == null || "".equals(itemNum)) {
			return "0";
		}

		// 2、MQ保存秒杀记录。
		// 这里设置一个阈值，如果这个量比较小，就判断一下，不需要走MQ。 怎么知道量比较小？你可以在token里面存一个量就行了。
		boolean async = asyncThreshold == 0 || Integer.parseInt(itemNum) > asyncThreshold;
		if (async) {
			// 保存之后，就是异步处理了，告诉前台，秒杀派对中，页面提示结果就好了。 如果页面关闭了，就去未完成订单里面找
			// 等待一段时间还没处理好，后台最好是直接干掉
			rabbitTemplate.convertAndSend("miaoshaOrderExchange", "", userId + ":" + itemId);
			return "wait";
		}
		return "0";
	}
}
