package edu.dongnao.study.balancer.lesson3_lua.goods.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * 模拟商品服务，数据聚合服务。
 * GoodsServices
 * 
 */
@Service
public class GoodsServices {
	
	/**
	 * 商品基本信息
	 * @param goodsId
	 * @return
	 */
	public Map<String, Object> goodsBasicInfo(String goodsId){
		Map<String, Object> basicInfo = new HashMap<String, Object>();
		basicInfo.put("name", "动脑学院定制茶杯水杯套餐");
		basicInfo.put("price", "389.00");
		basicInfo.put("promotionPrice", "69.00");
		basicInfo.put("ad", "购物不满两件——打骨折");
		basicInfo.put("describe", "创意造型 浓浓文艺气息 闲暇时光 与好友分享");
		return basicInfo;
	}
	
	/**
	 * 商品图片列表
	 * @param goodsId
	 * @return
	 */
	public List<String> goodsImages(String goodsId) {
		List<String> imgs = new ArrayList<String>();
		imgs.add("/images/goods/img01.png");
		imgs.add("/images/goods/img02.png");
		imgs.add("/images/goods/img03.png");
		imgs.add("/images/goods/img04.png");
		imgs.add("/images/goods/img05.png");
		return imgs;
	}
	
	/**
	 * 商品款式
	 * @param goodsId
	 * @return
	 */
	public List<Map<String, String>> goodsStyles(String goodsId) {
		List<Map<String, String>> styles = new ArrayList<Map<String, String>>();
		
		Map<String, String> styleItem = new HashMap<String, String>();
		styleItem.put("name", "熊猫套装");
		styleItem.put("imgSrc", "/images/goods/kuanshi01.jpg");
		styles.add(styleItem);
		
		styleItem = new HashMap<String, String>();
		styleItem.put("name", "铁塔套装");
		styleItem.put("imgSrc", "/images/goods/kuanshi02.jpg");
		styles.add(styleItem);
		
		styleItem = new HashMap<String, String>();
		styleItem.put("name", "创意胡子");
		styleItem.put("imgSrc", "/images/goods/kuanshi03.jpg");
		styles.add(styleItem);
		
		styleItem = new HashMap<String, String>();
		styleItem.put("name", "四色小猫");
		styleItem.put("imgSrc", "/images/goods/kuanshi04.jpg");
		styles.add(styleItem);
		
		return styles;
	}
	
	public List<String> goodsAllStyle(String goodsId) {
		List<String> imgs = new ArrayList<String>();
		imgs.add("/images/goods/evaluate101.jpg");
		imgs.add("/images/goods/evaluate102.jpg");
		imgs.add("/images/goods/evaluate103.jpg");
		imgs.add("/images/goods/evaluate104.jpg");
		imgs.add("/images/goods/evaluate105.jpg");
		imgs.add("/images/goods/evaluate106.jpg");
		return imgs;
	}
	
	/**
	 * 热门商品
	 * @param goodsId
	 * @return
	 */
	public List<String> goodsHot(String goodsId) {
		List<String> imgs = new ArrayList<String>();
		imgs.add("/images/goods/reimai02.jpg");
		imgs.add("/images/goods/reimai01.jpg");
		imgs.add("/images/goods/reimai03.jpg");
		return imgs;
	}
	
	/**
	 * 店铺商品分类
	 * @param shopId
	 * @return
	 */
	public List<String> shopSubject(String goodsId) {
		List<String> subject = new ArrayList<String>();
		subject.add("全部商品");
		subject.add("木质商品");
		subject.add("石制商品");
		subject.add("陶制商品");
		subject.add("家居厨房");
		subject.add("欧式混搭");
		subject.add("桌面摆件");
		subject.add("书香文房");
		return subject;
	}
	/**
	 * ..... //TODO 看了又看
	 * 
	 * 
	 */
	
}

