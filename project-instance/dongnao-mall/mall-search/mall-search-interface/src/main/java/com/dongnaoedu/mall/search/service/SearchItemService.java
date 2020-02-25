package com.dongnaoedu.mall.search.service;

import com.dongnaoedu.mall.manager.dto.EsInfo;

/**
 * @author allen
 */
public interface SearchItemService {

	/**
	 * 同步索引
	 * 
	 * @return
	 */
	int importAllItems();

	/**
	 * 获取ES基本信息
	 * 
	 * @return
	 */
	EsInfo getEsInfo();
}
