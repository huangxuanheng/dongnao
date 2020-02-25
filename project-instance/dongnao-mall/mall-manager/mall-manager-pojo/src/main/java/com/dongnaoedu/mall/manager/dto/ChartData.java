package com.dongnaoedu.mall.manager.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author allen
 */
public class ChartData implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Object> xDatas;

	private List<Object> yDatas;

	private Object countAll;

	public Object getCountAll() {
		return countAll;
	}

	public void setCountAll(Object countAll) {
		this.countAll = countAll;
	}

	public List<Object> getxDatas() {
		return xDatas;
	}

	public void setxDatas(List<Object> xDatas) {
		this.xDatas = xDatas;
	}

	public List<Object> getyDatas() {
		return yDatas;
	}

	public void setyDatas(List<Object> yDatas) {
		this.yDatas = yDatas;
	}
}
