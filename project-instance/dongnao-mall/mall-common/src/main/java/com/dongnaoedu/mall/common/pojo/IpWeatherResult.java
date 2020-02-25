package com.dongnaoedu.mall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author alln
 */
public class IpWeatherResult implements Serializable {
	private static final long serialVersionUID = 1L;

	String msg;

	List<City> result;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<City> getResult() {
		return result;
	}

	public void setResult(List<City> result) {
		this.result = result;
	}
}
