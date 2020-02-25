package com.dongnaoedu.mall.miaosha.service;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 这是一个发送MQ消息，修改消息表的地方
 * 
 * @author 动脑学院
 *
 */
@Service
public class MQService {
	private final Logger logger = LoggerFactory.getLogger(MQService.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/** mq发送，线程池 */
	private ThreadPoolExecutor mqSendExecutor = new ThreadPoolExecutor(500, 500, 60, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(1024));

	@PostConstruct
	public void setup() {
		// 消息发送完毕后，则回调此方法 ack代表发送是否成功
		rabbitTemplate.setConfirmCallback(new ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				// ack为true，代表MQ已经准确收到消息
				if (!ack || correlationData == null) {
					return;
				}
				try {
					// 2. 修改本地消息表的状态为“已发送”。
					String sql = "update tb_records set status=1 where records_id=?";
					int count = jdbcTemplate.update(sql, correlationData.getId());

					if (count != 1) {
						logger.warn("警告：本地消息表的状态修改不成功");
					}
				} catch (Exception e) {
					logger.warn("警告：修改本地消息表的状态时出现异常", e);
				}
			}
		});
	}

	/**
	 * 发送MQ消息，修改本地记录表的状态
	 * 
	 * @throws Exception
	 */
	public void sendMsg(long recordsId, String userId, String goodsCode) {
		mqSendExecutor.execute(() -> {
			String data = userId + ":" + goodsCode;
			// 1. 发送消息到MQ
			// CorrelationData 当收到消息回执时，会附带上这个参数
			rabbitTemplate.convertAndSend("orderExchange", "*", data, new CorrelationData(String.valueOf(recordsId)));
		});
	}
}
