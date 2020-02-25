/**
 * 
 */
package com.dongnaoedu.mall.miaosha.schedule;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dongnaoedu.mall.common.pojo.DataTablesResult;
import com.dongnaoedu.mall.manager.pojo.TbItem;
import com.dongnaoedu.mall.manager.service.ItemService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

/**
 * 每半小时加载商品余量信息
 * 
 * @author 动脑学院.Tony老师
 * @see 专注在职IT人员能力提升，咨询顾问QQ: 2729 772 006
 */
@JobHandler(value = "loadItemScheduler")
@Service
public class LoadItemScheduler extends IJobHandler {
	private Logger logger = LoggerFactory.getLogger(LoadItemScheduler.class);

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Reference
	ItemService itemService;

	@Autowired
	JdbcTemplate jdbcTemplate;

	/** 秒杀类型 */
	public static int CID = -2;

	@Override
	@SuppressWarnings("unchecked")
	public ReturnT<String> execute(String arg0) throws Exception {
		// 循环加载秒杀商品数量到redis中,每次加载500
		DataTablesResult result = itemService.getItemList(0, 1, 1000, CID, "", "created", "desc");
		List<TbItem> data = (List<TbItem>) result.getData();
		for (TbItem tbItem : data) {
			try {
				// 插入数据库存储
				jdbcTemplate.update("insert into tb_miaosha values(?, ?)", tbItem.getId(), tbItem.getNum());
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			String key = "token_" + tbItem.getId();
			stringRedisTemplate.delete(key);
			
			for (int i = 0; i < tbItem.getNum(); i++) {
				// 每种商品都有对应的可抢购数值，这里的redis，是集群的
				stringRedisTemplate.opsForList().rightPush(key, String.valueOf(tbItem.getNum()));
			}
		
		}
		logger.info(">>>执行加载逻辑,加载商品数量：{}", result.getRecordsFiltered());
		return new ReturnT<String>("ok");
	}

}
