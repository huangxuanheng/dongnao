package edu.dongnao.study.balancer.lesson3_lua.goods;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dongnao.study.balancer.lesson3_lua.goods.services.GoodsServices;

/**
 * GoodsController
 * 
 */
@Controller
public class GoodsController {
	private Logger logger = LoggerFactory.getLogger(GoodsController.class);
	
	@Autowired
	private GoodsServices services;
	
	@Resource(name = "mainRedisTemplate")
    StringRedisTemplate mainRedisTemplate;
	
	@RequestMapping("goods")
	public String toGoodsDetail() {
		return "goods.html";
	}
	
	@ResponseBody
	@RequestMapping("goods/info")
	//@Cacheable(cacheManager = "cacheManager", value = "goodsId", key = "#goodsId")
	public Map<String, Object> goodsInfo(@RequestParam(value = "goodsId", required = true) String goodsId) {
		logger.info("get request. goodsId: "+goodsId);
		
		Map<String, Object> detail = new HashMap<String, Object>();
		detail.put("basic", services.goodsBasicInfo(goodsId));
		detail.put("allStyle", services.goodsAllStyle(goodsId));
		detail.put("hot", services.goodsHot(goodsId));
		detail.put("imgs", services.goodsImages(goodsId));
		detail.put("style", services.goodsStyles(goodsId));
		detail.put("shopSubject", services.shopSubject(goodsId));
		
		ObjectMapper json = new ObjectMapper();
		try {
			final String cacheKey = "goodsId::"+goodsId;
			final String v = json.writeValueAsString(detail);
			mainRedisTemplate.execute((RedisCallback<Boolean>) connection -> {
				return connection.setEx(cacheKey.getBytes(), 120, v.getBytes());
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return detail;
	}
	
}

