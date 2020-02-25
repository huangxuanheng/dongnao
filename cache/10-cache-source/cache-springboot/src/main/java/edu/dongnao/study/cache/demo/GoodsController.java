package edu.dongnao.study.cache.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.dongnao.study.cache.demo.service.GoodsService;

/**
 * GoodsController
 * 
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
	@Autowired
    GoodsService service;
	
	@RequestMapping("/query/{id}")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public Map query(@PathVariable("id") String id) {
		String stock = service.queryStock(id);
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("id", id);
		result.put("stock", stock);
		return result;
	}
}

