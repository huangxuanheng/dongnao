package com.dongnaoedu.mall.miaosha.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongnaoedu.mall.common.utils.SnowflakeIdWorker;

/**
 * 数据库操作
 * 
 * @author 动脑学院
 *
 */
@Service
@Transactional
public class GoodsService {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	MQService mqService;

	SnowflakeIdWorker idFactory;

	/** 初始化一个ID构建工具 */
	@PostConstruct
	public void init() {
		idFactory = new SnowflakeIdWorker();
	}

	/**
	 * 把秒杀需要的商品信息和库存，存在数据库
	 */
	public boolean insertGoodsInfo(String goodsCode, String userId) {
		// 商品数量 减1
		String sql = "update tb_miaosha set goods_nums = goods_nums - 1 where goods_code = ? and goods_nums > 0";
		int count = jdbcTemplate.update(sql, goodsCode);
		if (count != 1) {
			// 代表秒杀失败
			return false;
		}

		// 添加记录
		long recordsId = idFactory.nextId();
		String insertSql = "INSERT INTO tb_records(records_id, goods_code, user_id, status) VALUES(?, ?, ?, 0)";
		jdbcTemplate.update(insertSql, recordsId, goodsCode, userId);

		// 提交线程任务， 发往订单系统，作为一个待付款订单
		mqService.sendMsg(recordsId, userId, goodsCode);

		return true;
	}
}
