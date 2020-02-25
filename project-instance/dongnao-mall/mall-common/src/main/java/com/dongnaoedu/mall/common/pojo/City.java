package com.dongnaoedu.mall.common.pojo;

import java.io.Serializable;

/**
 * @author allen
 */
public class City implements Serializable {
	private static final long serialVersionUID = 1L;

	String city;

	String distrct;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrct() {
		return distrct;
	}

	public void setDistrct(String distrct) {
		this.distrct = distrct;
	}
}
