package edu.dongnao.study.cache.demo.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 从数据库中查询
 * 
 */
@Component
public class DatabaseService {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public String queryFromDatabase(String goodsId) {
		String sql = "SELECT goods_nums FROM tb_goods WHERE goods_code =  '" + goodsId + "'";

		Map<String, Object> result = jdbcTemplate.queryForMap(sql);

		return result.get("goods_nums").toString();
	}
}
