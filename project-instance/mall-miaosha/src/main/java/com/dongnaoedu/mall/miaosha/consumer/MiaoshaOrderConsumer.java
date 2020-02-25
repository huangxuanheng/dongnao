package com.dongnaoedu.mall.miaosha.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.dongnaoedu.mall.miaosha.service.GoodsService;
import com.rabbitmq.client.Channel;

/**
 * 消费者，取调度队列
 * 
 * @author Tony
 *
 */
@Component
public class MiaoshaOrderConsumer {
	private final Logger logger = LoggerFactory.getLogger(MiaoshaOrderConsumer.class);

	@Autowired
	GoodsService goodsService;

	@RabbitListener(queues = "miaoshaOrderQueue")
	public void messageConsumer(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag)
			throws Exception {
		try {
			String[] args = message.split(":");
			String userId = args[0];
			String itemId = args[1];
			goodsService.insertGoodsInfo(itemId, userId);
		} catch (Exception e) {
			logger.error("出错，丢弃", e);
			logger.error("这个错误是开发人员也没意料到，所以不用重发，我通过监控系统通知人工处理", e);
			// Nack - 不可能天天有异常的
			channel.basicNack(tag, false, false);
		}
		// 如果不给回复，就等这个consumer断开链接后再继续

	}
}
