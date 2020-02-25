package com.dongnaoedu.mall.job.jobhandler;

import com.xxl.job.core.log.XxlJobLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dongnaoedu.mall.manager.service.OrderService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

/**
 * 定时清理失效订单
 * 
 * @author allen
 *
 */
@JobHandler(value = "cancelOrderJobHandler")
@Component
public class CancelOrderJobHandler extends IJobHandler {
	private final static Logger log = LoggerFactory.getLogger(CancelOrderJobHandler.class);

	@Autowired
	private OrderService orderService;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		
		log.info("执行了自动取消订单定时任务");
		XxlJobLogger.log("执行了自动取消订单定时任务");
		orderService.cancelOrder();
		
		return SUCCESS;
	}

}
