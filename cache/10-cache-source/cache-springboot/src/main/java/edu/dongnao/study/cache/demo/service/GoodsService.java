package edu.dongnao.study.cache.demo.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class GoodsService {

    private final Logger logger = LoggerFactory.getLogger(GoodsService.class);

    @Resource(name = "mainRedisTemplate")
    StringRedisTemplate mainRedisTemplate;

    @Autowired
    DatabaseService databaseService;

    /**
     * 查询商品库存数
     *
     * @param goodsId 商品ID
     * @return 商品库存数
     */
    public String queryStock(final String goodsId) {
        String value = databaseService.queryFromDatabase(goodsId);
        logger.info(Thread.currentThread().getName() + "从数据库中取得数据==============>" + value);
        return value;
    }
}
